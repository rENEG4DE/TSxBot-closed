package tsxdk.io.query;

import java.util.ArrayList;
import java.util.List;

import system.core.Period;
import tsxdk.entity.TsEntity;
import tsxdk.entity.TsReturn;
import tsxdk.io.TsPipe;
import utility.bulletin.AdvancedGlobalBulletin;
import utility.reclaimable.Reclaimable;
import api.data.TsEntityObject;
import api.data.TsEntityType;
import api.query.AbstractPreparableQuery;
import api.query.AbstractQuery;
import api.query.GetResultAction;
import api.query.GetReturnAction;
import api.query.OnDeployAction;

public class TSxQuery implements Reclaimable, AbstractQuery, AbstractPreparableQuery {

	private static final AdvancedGlobalBulletin bulletin = AdvancedGlobalBulletin.getSharedInstance();
	private static final TsPipe pipe = TsPipe.getSharedInstance(); 
	private static final Long LIFETIME = Period.QUERY_LIFETIME.getValue();			//The minimum time between sending 2 querys of this kind

	private String content;					//the contents for this Query to Ts
	private String fmtString;
	private TsEntityType expectedResult;	//The kind of result this query expects
	private long timestamp;
	private long timeout;

	private TsEntity tsResult;		//the RESULT for this query - like a channel-list
	private TsReturn tsReturn;		//the RETURN for this query - error with id and message

	private boolean answered;
	private boolean deployed = false;
	
	private List<OnDeployAction> deployActions;
	private List<GetResultAction> resultActions;
	private List<GetReturnAction> returnActions;

	public TSxQuery(String content, TsEntityType resultType) {
		this ();
		bulletin.QUERY.Verbose.push(" creating Query, result-expectation ", new String[]{"TsQuery","ctor"}, new String[]{content, resultType.toString()});
		this.content = content;
		this.expectedResult = resultType;
	}
	
	public TSxQuery () {
		this.content = null;
		this.expectedResult = TsEntityType.TSRETURN;
		this.tsResult = null;
		this.tsReturn = null;
		
		timestamp = System.currentTimeMillis();
		timeout = timestamp;
	}
	
	private TSxQuery (	String content, 
						TsEntityType expectation,
						long timestamp, long timeout,
						List<OnDeployAction> onDeploy, 
						List<GetResultAction> onResult,
						List<GetReturnAction> onReturn) {
		this.content = content;
		this.expectedResult = expectation;
		this.tsResult = null;
		this.tsReturn = null;
		this.timeout = timeout;
		this.timestamp = timestamp;
		
		deployActions = onDeploy;
		resultActions = onResult;
		returnActions = onReturn;
	}
	

	@Override
	public AbstractQuery copy() {
		return new TSxQuery (content,expectedResult,timestamp,timeout,deployActions,resultActions,returnActions);
	}
	
	@Override
	public void clearForReuse() {
//		deployListeners = null;
//		resultListeners = null;
//		returnListeners = null;
//		deployActions =  null;
//		resultActions = null;
//		returnActions = null;
//		
//		tsResult = null;
//		tsReturn = null;				
//
//		timestamp = 0L;
//		timeout = 0L;
	}
	
	public void setup (String content, TsEntityType expectedResultEntityType) {
		this.content = content;
		this.expectedResult = expectedResultEntityType;
	}
	
	public String toString () {
		return AdvancedGlobalBulletin.createBullet(
				"content, expectedResult, timestamp, timeout, answered, deployed:", 
				new String[]{"TsQuery","content"}, 
				new Object[]{content,expectedResult,timestamp,timeout,answered/*,deployed,*/});
	}

	public boolean deploy () {
		//System.out.println ("currentTime: "+ System.currentTimeMillis()+ " timeout"+ timeout);
		if (System.currentTimeMillis() >= timeout) {
			return deploy0();
		} else {
			bulletin.QUERY.Warn.push(" deploying denied for ", new String[]{"TSxQuery","deploy"}, new String[]{content});
			return false;
		}
	}
	
	public boolean deploy (boolean force) {
		//System.out.println ("currentTime: "+ System.currentTimeMillis()+ " timeout"+ timeout);
		if (!force) {return deploy ();}

		return deploy0();
	}

	private boolean deploy0() {
		timestamp = System.currentTimeMillis();
		bulletin.QUERY.Verbose.push("deployed@, query", new String[] { "TSxQuery", "deploy" }, new Object[] { System.currentTimeMillis(), content });
		pipe.out(content);
		answered = false;
		tsResult = null; // Release tsResult, anyone still handling this ResultSet better Release!
		tsReturn = null;
		deployed = true;
		triggerDeployActions();
//		notifyDeployListeners();
		return true;
	}

	private void updateTimeOut() {
		timeout = System.currentTimeMillis() + LIFETIME;
	}
	
	public void setReturn (TsReturn tsRet) {
		bulletin.QUERY.Verbose.push(" for query,message,id ", new String[]{"TsQuery","Return"}, new Object[]{content, tsRet.getMessage(), tsRet.getId()});
		if (expectedResult == TsEntityType.TSRETURN || tsResult != null) 
			answered = true;

		this.tsReturn = tsRet;

		triggerReturnActions();
//		notifyReturnListeners();
	}
	

	public void setResult (TsEntity entity) {
		bulletin.QUERY.Verbose.push(" result, content ", new String[]{"TsQuery","Result"}, new Object[]{entity.getType(),getShortContent()});
//		bulletin.QUERY.push(getShortContent(), new String[]{"TsQuery","Result"}, new Object[]{entity.getType()});
		if (entity.getType() != expectedResult) {
			bulletin.QUERY.Error.push("Can't handle result " + entity.getType()
					+ " for query " + content, new String[] { "TsQuery",
					"setResult" });
			// If TSRETURN, may be empty result set!
		}

		this.tsResult = entity;
		triggerResultActions();
//		notifyResultListeners();
	}

	public boolean isAnswered() {
		return answered;
	}
	
	public boolean isTimedOut() {
		return System.currentTimeMillis() >= getTimeout();
	}
	
	public boolean isDeployable() {
		if (!deployed || isAnswered() || isTimedOut()) {
			bulletin.QUERYSTATE.Verbose.push(" is deployable ", new String[]{"TsQuery",getShortContent()});
			return true;
		} else {
			bulletin.QUERYSTATE.Verbose.push(" is not deployable ", new String[]{"TsQuery",getShortContent()});
			return false;
		}
	}
	
	final private void triggerResultActions() {
		if (resultActions != null) {
			bulletin.QUERYLISTENERS.Verbose.push(" notifying Result-listeners", new String[] { "TsQuery", getShortContent() });
			for (GetResultAction lstr : resultActions) {
				lstr.onUpdate((TsEntityObject) tsResult);
			}
			resultActions.clear();
		}
	}
	
	final private void triggerReturnActions() {
		if (returnActions != null) {
			bulletin.QUERYLISTENERS.Verbose.push(" notifying Return-listeners", new String[] { "TsQuery", getShortContent() });
			for (GetReturnAction lstr : returnActions) {
				lstr.onUpdate(tsReturn);
			}
			returnActions.clear();
		}
	}

	final private void triggerDeployActions() {
		if (deployActions != null) {
			bulletin.QUERYLISTENERS.Verbose.push(" notifying Deploy-listeners", new String[] { "TsQuery", getShortContent() });
			for (OnDeployAction lstr : deployActions) {
				lstr.onDeploy(this);
			}
			deployActions.clear();
		}
	}
	
	@Override
	public void registerResultAction(GetResultAction action) {
		bulletin.QUERYLISTENERS.Verbose.push(" registering Result-Action", new String[]{"TsQuery",getShortContent()});
		if (resultActions == null) {
			resultActions = new ArrayList<>(1);
		}
		resultActions.add(action);
	}

	@Override
	public void registerReturnAction(GetReturnAction action) {
		bulletin.QUERYLISTENERS.Verbose.push(" registering Return-Action", new String[]{"TsQuery",getShortContent()});
		if (returnActions == null) {
			returnActions = new ArrayList<>(1);
		}
		returnActions.add(action);
	}

	@Override
	public void registerDeployAction(OnDeployAction action) {
		bulletin.QUERYLISTENERS.Verbose.push(" registering Deploy-Action", new String[]{"TsQuery",getShortContent()});
		if (deployActions == null) {
			deployActions = new ArrayList<>(1);
		}
		deployActions.add(action);
	}
	
	@Override
	public String getContent() {
		return content;
	}
	
	@Override
	public TsEntityType getExpectedType() {
		return expectedResult;
	}
	
	@Override
	public Long getTimestamp() {
		return timestamp;
	}
	
	@Override
	public Long getTimeout() {
		return timeout;
	}

	//a little convenience...
	
	private String getShortContent () {
		return content.length() >= 8 ? content.substring(0, 8) : content;
	}
	
	@Override
	public Object getSlotID() {
		return content;
	}

	@Override
	public void updateSlotID(Object k) {
		content = (String) k;
	}

	@Override
	public void prepare(Object... values) {
		content = String.format(fmtString, values);
	}

	public void setPreparable() {
		fmtString = content;	
	}
}
