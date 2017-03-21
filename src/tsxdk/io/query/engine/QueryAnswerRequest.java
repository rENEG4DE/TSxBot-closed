package tsxdk.io.query.engine;

import tsxdk.io.query.TSxQuery;
import api.query.Query;

/**
 * Every query has an expected answer.
 */
final public class QueryAnswerRequest {
	final Query query;
	final AnswerExpectation request;
	
	public QueryAnswerRequest (TSxQuery query, AnswerExpectation expectation) {
		this.query = query;
		this.request = expectation;
	}
}