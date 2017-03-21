package bot.action.instance;

import system.core.Context;
import tsxdk.io.query.engine.QueryAgent;
import api.data.TsEntityType;
import api.query.Query;

public class QueryAction extends AbstractAction {
//	private static final QueryAgent agent = AsynchronousQueryAgent.getSharedInstance();
	private static final QueryAgent agent = Context.getQueryAgent();

	private final Query query;

	private final TsEntityType expectedResult;

	protected QueryAction(Long delay, Long expire, String whatToDo) {
		super(delay, expire, "TsQuery.sendCommand(" + whatToDo + ")");
		query = Context.getQueryAgent().prepareQuery(whatToDo);
		expectedResult = null;
	}

	public QueryAction(Long delay, Long expire, Query query) {
		super(delay, expire, "TsQuery.sendCommand(" + query.getContent() + ")");
		this.query = query;
		expectedResult = query.getExpectedType();
	}
	
	public QueryAction(Long delay, Long expire, String whatToDo, TsEntityType result) {
		super(delay, expire, "TsQuery.sendCommand(" + whatToDo + ")");
		query = Context.getQueryAgent().prepareQuery(whatToDo,result);
		this.expectedResult = result;
	}
	
	protected QueryAction(Long delay, String whatToDo) {
		this(delay, 0L, whatToDo);
	}
	
	protected QueryAction(Long delay, String whatToDo, TsEntityType result) {
		this(delay, 0L, whatToDo, result);
	}

	@Override
	public void execute() {
		agent.pushQuery(query);
	}
}
