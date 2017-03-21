package tsxdk.io.query.engine;

import tsxdk.entity.TsEntity;
import tsxdk.entity.TsReturn;
import tsxdk.io.query.TSxQuery;
import api.query.Query;

final public class QueryAnswer {
	final TsEntity[] answerElements;
	private boolean hasEntity;
	
	public QueryAnswer () {
		answerElements = new TsEntity[2];
		hasEntity = false;
	}
	
	public void setResult (TsEntity res) {
		answerElements[0] = res;
		hasEntity = true;
	}
	
	public void setReturn (TsReturn ret) {
		if (!hasEntity) {
			answerElements[0] = ret;
		} else {
			answerElements[1] = ret;
		}
	}
	
	public void assignToQuery (Query query) {
		TSxQuery qry = (TSxQuery) query;
		if (hasEntity) {
			qry.setResult(answerElements[0]);
			qry.setReturn((TsReturn) answerElements[1]);
		} else {
			qry.setReturn((TsReturn) answerElements[0]);
		}
	}
	
	public void clear() {
		answerElements[0] = answerElements[1] = null;
		hasEntity = false;
	}
}