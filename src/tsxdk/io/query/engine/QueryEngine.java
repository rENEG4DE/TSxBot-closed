package tsxdk.io.query.engine;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import tsxdk.entity.TsEntity;
import tsxdk.entity.TsReturn;
import tsxdk.io.query.TSxQuery;
import utility.bulletin.AdvancedGlobalBulletin;
import utility.systemInfo.SystemInfo;
import utility.systemInfo.SystemInfoElement;
import api.data.TsEntityType;
import api.query.Query;

public class QueryEngine implements QueryAgent {
	private final static class SingletonHolder {
		private final static QueryAgent INSTANCE = new QueryEngine();
	}

	public static final QueryAgent getSharedInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private final AdvancedGlobalBulletin bulletin = AdvancedGlobalBulletin.getSharedInstance();
//	private final ReclaimableCollection<String>	queryCache;	
	private final Queue<TSxQuery>				deployQueue;
	private final QueryAnswerReceiver			answerReceiver;

	private QueryEngine () {
//		queryCache = new ProdigalPool<>(TSxQuery.class);
		answerReceiver = new QueryAnswerReceiver();
		deployQueue = new ConcurrentLinkedQueue<>();
		SystemInfo.getSharedInstance().registerSystemObject(this);
	}
	
	
	@Override
	public SystemInfoElement getSystemInfo() {
		return new SystemInfoElement("QueryEngine", "state", 
				"deployQueue: " +  deployQueue.size(),
//				"queryCache: " + queryCache.getCapacity(),
				"stateView: " + getStateView());
	}

	@Override
	public  synchronized TSxQuery instantQuery(String query, TsEntityType resultType) {
		return pushNewQuery(query, resultType);
	}


	private void registerQueryReceivingAnswer(TSxQuery qry) {
		TsEntityType typeExpected = qry.getExpectedType();
		if (typeExpected == TsEntityType.TSRETURN) {
			answerReceiver.add(new QueryAnswerRequest(qry, new AnswerExpectation(TsEntityType.TSRETURN)));
		} else {
			answerReceiver.add(new QueryAnswerRequest(qry, new AnswerExpectation(typeExpected, TsEntityType.TSRETURN)));
		}
	}

	@Override
	public synchronized  void  pushQuery(Query qry) {
		TSxQuery query = (TSxQuery) qry;
		if (query.isDeployable()) {
			bulletin.QUERYENGINE.Verbose.push("accepted query ", new String[] { "QueryEngine", "pushQuery" }, new String[] { query.getContent() });
			deployQueue.add(query);
		} else {
			bulletin.QUERYENGINE.Info.push("denied query ", new String[] { "QueryEngine", "pushQuery" }, new String[] { query.getContent() });
//			deniedCount++;			
		}
	}

	private synchronized void deployQuery(Query qry) {
		TSxQuery query = (TSxQuery) qry;
		if (query.deploy()) {
			registerQueryReceivingAnswer(query);
		}
	}

	public synchronized void deployQueryQueue() {
		int i = 0;
		TSxQuery query = (TSxQuery) deployQueue.poll();
		while (query != null) {
			if (query != null && query.deploy()) {
				registerQueryReceivingAnswer(query);
			}
			query = (TSxQuery) deployQueue.poll();
			++i;
		}
	}
	
	@Override
	public  synchronized TSxQuery pushNewQuery(String query) {
		return pushNewQuery(query, TsEntityType.TSRETURN);
	}

	@Override
	public  synchronized TSxQuery pushNewQuery(String queryContent, TsEntityType resultType) {
		TSxQuery query = (TSxQuery) new TSxQuery(queryContent, resultType);
		
		pushQuery(query);
		
		return query;
	}

	@Override
	public TSxQuery prepareQuery(String queryContent) {
		return prepareQuery(queryContent, TsEntityType.TSRETURN);
	}

	@Override
	public TSxQuery prepareQuery(String queryContent, TsEntityType resultType) {
		TSxQuery query = (TSxQuery) new TSxQuery(queryContent, resultType);
		return query;
	}

	@Override
	public TSxQuery prepareFormattableQuery(String string, TsEntityType expectedReturn) {
		TSxQuery query = prepareQuery(string, expectedReturn);
		query.setPreparable();
		return query;
	}
	
	@Override
	public void deployAll() {
		throw new IllegalStateException();
	}

	@Override
	public void pushResult(TsEntity entity) {
		answerReceiver.pushResultAnswer(entity);
	}
	
	@Override
	public void pushReturn(TsReturn entity) {
		answerReceiver.pushReturnAnswer(entity);
	}

	@Override
	public void removeQuery(String content) {
//		queryCache.remove(content);
	}

	@Override
	public String getStateView() {
		return "NOTHING YET";
	}

	@Override
	public void clearCache() {
		
//		queryCache.clearCache();
	}

	public boolean hasExpectations() {
		return answerReceiver.hasExpectations();
	}
}
