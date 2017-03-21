package tsxdk.io.query.engine;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import system.core.Context;
import system.core.Default;
import tsxdk.entity.TsEntity;
import tsxdk.entity.TsReturn;
import tsxdk.io.query.TSxQuery;
import utility.bulletin.AdvancedGlobalBulletin;
import utility.misc.StringMan;
import utility.systemInfo.SystemInfo;
import utility.systemInfo.SystemInfoElement;
import utility.systemInfo.SystemObject;

final class QueryAnswerReceiver implements SystemObject {
	private final Queue<QueryAnswerRequest>	requestQueue;
	private QueryAnswer						currentAnswer;
	
	private final AdvancedGlobalBulletin			bulletin;
	
	private int correctQueryCtr = 0;			//How many queries were (supposedly) correctly answered
	private int discardedQueryAnswerCtr = 0;	//How many answers have been discarded ?
	private int resultExpCtr = 0;				//How many Results have been expected
	private int returnExpCtr = 0;				//How many Returns have been expected
	private int discardedResultCtr = 0;			//How many Results have been discarded because there was no query to assign it to
	private int discardedReturnCtr = 0;			//How many Returns have been discarded because there was no query to assign it to
	
	public QueryAnswerReceiver() {
		requestQueue = new LinkedList<>();
		currentAnswer = new QueryAnswer();
		bulletin = AdvancedGlobalBulletin.getSharedInstance();
		SystemInfo.getSharedInstance().registerSystemObject(this);
	}

	public void add(QueryAnswerRequest queryAnswerObject) {
		requestQueue.add(queryAnswerObject);
		returnExpCtr++;
		if (queryAnswerObject.request.takesEntity)
			resultExpCtr++;
	}

	public void pushResultAnswer(TsEntity entity) {
		if (requestQueue.size() == 0) {
			discardedResultCtr++;
		}
		currentAnswer.setResult(entity);
	}

	public void pushReturnAnswer(TsReturn entity) {
		currentAnswer.setReturn(entity);
		if (requestQueue.size() == 0) {
			discardedReturnCtr++;
		} else {
			handleExceptionalId(entity);
			onAnswerReady();
		}
	}

	private void handleExceptionalId(TsReturn entity) {
		Map<String,Object> exceptionInfo = new LinkedHashMap<>();
		try {
			try {
				exceptionInfo.put("About", "Exceptional-ID-handling-error");
				exceptionInfo.put("Entity", entity);
				exceptionInfo.put("Id", entity.getId());
				exceptionInfo.put("Message", entity.getMessage());
				exceptionInfo.put("requestQueue.size", requestQueue.size());
				exceptionInfo.put("requestQueue.peek", requestQueue.peek());
				if (requestQueue.peek() != null) {
					exceptionInfo.put("requestQueue.peek().request", requestQueue.peek().request);
					exceptionInfo.put("requestQueue.peek().query", requestQueue.peek().query);
					exceptionInfo.put("requestQueue.peek().query.getContent()", requestQueue.peek().query.getContent());
				}
			} catch (Exception e) {
			} finally {
				exceptionInfo.put("Data-retrievement-error", "happened");
			}
			switch (entity.getId()) {
				case 1281: {
				}; // <- no break here - fall through on purpose !
				case 771: {					
					AnswerExpectation request = requestQueue.peek().request;
					if (request.takesEntity)
						request.doIgnoreEntity();
				}break;
				case 0: {	//standard case
				}break;
				default: {
					bulletin.QUERYENGINE.Error.push(
							"Unhandled ID caught, id, msg, query",
							new String[] { "QueryAnswerReceiver", "handleExceptionalId" },
							new Object[] { entity.getId(), entity.getMessage(),
									requestQueue.peek().query.getContent() });
				}break;
			}
		} catch (Exception e) {
			SystemInfo.getSharedInstance().pushExceptionInfo(exceptionInfo);
		}
	}
	
	private boolean answerReady() {
		return requestQueue.peek().request.matches(currentAnswer);
	}
	
	private void onAnswerReady() {
		if (answerReady()) {
			bulletin.QUERYENGINE.Verbose.push("Fitting answer caught, expectation, actual, query", 
					new String[]{"QueryAnswerReceiver","onAnswerReady"},
					new Object[]{
						Arrays.deepToString(requestQueue.peek().request.answerElements), 
						Arrays.deepToString(currentAnswer.answerElements),
						requestQueue.peek().query.getContent()});
			onAnswerRequestFulfill();
		} else {
			bulletin.QUERYENGINE.Warn.push("Non fitting answer caught, expectation, actual, query", 
					new String[]{"QueryAnswerReceiver","onAnswerReady"},
					new Object[]{
						Arrays.deepToString(requestQueue.peek().request.answerElements), 
						Arrays.deepToString(currentAnswer.answerElements),
						requestQueue.peek().query.getContent()});
			handleNonFittingAnswer();	
		}
	}

	private void onAnswerRequestFulfill() {
		bulletin.QUERYENGINE.Verbose.push(
				"Answer request was fulfilled, request", 
				new String[]{"QueryAnswerReceiver","onAnswerRequestFulfill"},
				new Object[]{Arrays.deepToString(requestQueue.peek().request.answerElements)});
		
		bindCurrentAnswer();
	}

	private void bindCurrentAnswer() {
		QueryAnswerRequest answered = requestQueue.poll();
		currentAnswer.assignToQuery(answered.query);
		currentAnswer.clear();

		correctQueryCtr++;
		bulletin.QUERYENGINE.Verbose.push(
				"Bound answer to query", 
				new String[]{"QueryAnswerReceiver","bindCurrentAnswer"},
				new Object[]{Arrays.deepToString(answered.request.answerElements), StringMan.makeFixedLen(answered.query.getContent(), 30)});
	}
	

	private void handleNonFittingAnswer() {
		QueryAnswerRequest peek = requestQueue.peek();
		Context.getSharedInstance().getLog().fine(AdvancedGlobalBulletin.createBullet("Non fitting answer caught, discarding! query, expectation, actual", 
				new String[]{"QueryAnswerReceiver","catchNonFittingAnswer"},
				new Object[]{peek.query.getContent(),
					Arrays.deepToString(peek.request.answerElements),
					Arrays.deepToString(currentAnswer.answerElements)}));
		discardCurrentAnswer0();
	}
	
	private void discardCurrentAnswer0() {
		discardedQueryAnswerCtr++;
		final QueryAgent agent = Context.getQueryAgent();
		final QueryAnswerRequest answered = requestQueue.poll();
		final QueryAnswerRequest newRequest = requestQueue.peek();

		bulletin.QUERYENGINE.Info.push(
				"Discarded Query-Expectation. query, expectation, actual, new Expectation", 
				new String[]{"QueryAnswerReceiver","discardCurrentAnswer"},
				new Object[]{answered.query.getContent(),
						Arrays.deepToString(answered.request.answerElements),
						Arrays.deepToString(currentAnswer.answerElements),
						newRequest != null ? Arrays.deepToString(newRequest.request.answerElements) : null});

		//Does the answer maybe fit the next query ?
		if (newRequest != null && newRequest.request.matches(currentAnswer)) {
			bulletin.QUERYENGINE.Info.push(
					"Assigning answer to next query, query, new expectation, answer elements", 
					new String[]{"QueryAnswerReceiver","discardCurrentAnswer"},
					new Object[]{newRequest.query.getContent(),
							Arrays.deepToString(newRequest.request.answerElements),
							Arrays.deepToString(currentAnswer.answerElements)});
					
			currentAnswer.assignToQuery(newRequest.query);
			agent.removeQuery(((TSxQuery)newRequest.query).getSlotID().toString());
			QueryAnswerRequest poll = requestQueue.poll();
		}

		if ((boolean)Default.resend_faulty_query.getValue()) {
			bulletin.QUERYENGINE.Info.push(
					"Resending faulty query", 
					new String[]{"QueryAnswerReceiver","discardCurrentAnswer"},
					new Object[]{answered.query.getContent()});

			agent.pushQuery(answered.query);
		}
		
		//Discard the query expecting the answer.
		
		
		//Discard the given, non-fitting answer
		currentAnswer.clear();
	}

	private void discardCurrentAnswer1() {
		discardedQueryAnswerCtr++;
		final QueryAgent agent = Context.getQueryAgent();
		final QueryAnswerRequest answered = requestQueue.poll();
		final QueryAnswerRequest newRequest = requestQueue.peek();

		bulletin.QUERYENGINE.Info.push(
				"Discarded Query-Expectation. query, expectation, actual, new Expectation", 
				new String[]{"QueryAnswerReceiver","discardCurrentAnswer"},
				new Object[]{answered.query.getContent(),
						Arrays.deepToString(answered.request.answerElements),
						Arrays.deepToString(currentAnswer.answerElements),
						newRequest != null ? Arrays.deepToString(newRequest.request.answerElements) : null});

		//Does the answer maybe fit the next query ?
		if (newRequest != null && newRequest.request.matches(currentAnswer)) {
			bulletin.QUERYENGINE.Info.push(
					"Assigning answer to next query, query, new expectation, answer elements", 
					new String[]{"QueryAnswerReceiver","discardCurrentAnswer"},
					new Object[]{newRequest.query.getContent(),
							Arrays.deepToString(newRequest.request.answerElements),
							Arrays.deepToString(currentAnswer.answerElements)});
					
			currentAnswer.assignToQuery(newRequest.query);
			agent.removeQuery(((TSxQuery)newRequest.query).getSlotID().toString());
			final QueryAnswerRequest eliminateStupidAssFindBugsError = requestQueue.poll();
		}

		if ((boolean)Default.resend_faulty_query.getValue()) {
			bulletin.QUERYENGINE.Info.push(
					"Resending faulty query", 
					new String[]{"QueryAnswerReceiver","discardCurrentAnswer"},
					new Object[]{answered.query.getContent()});

			agent.pushQuery(answered.query);
		}
		
		//Discard the query expecting the answer.
		
		
		//Discard the given, non-fitting answer
		currentAnswer.clear();
	}
	
	@Override
	public SystemInfoElement getSystemInfo() {
		return new SystemInfoElement("QueryAnswerReceiver", "state", 
				"Correctly answered queries: " + correctQueryCtr,
				"Faulty answered queries: " + discardedQueryAnswerCtr,
				"Current request-queue size: " + requestQueue.size(),
				"Current answer-state: " + Arrays.deepToString(currentAnswer.answerElements),
				"Expected results: " + resultExpCtr,
				"Expected returns: " + returnExpCtr,
				"Discarded results: " + discardedResultCtr,
				"Discarded returns: " + discardedReturnCtr);
		
	}

	public boolean hasExpectations() {
		return requestQueue.size() > 0;
	}
}
