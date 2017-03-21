package tsxdk.io.query.engine;

import tsxdk.entity.TsEntity;
import utility.bulletin.AdvancedGlobalBulletin;
import api.data.TsEntityType;

public final class AnswerExpectation {
	final TsEntityType[]	answerElements;
	final boolean			takesEntity;	// That is if we got a 2-part answer, 1 entity, 1 return
	boolean					ignoreEntity;	// May be we have a 2-part answer, but there is no entity to be received (e.g.: empty complain-list)

	public AnswerExpectation(TsEntityType... expectations) {
		this.answerElements = expectations;
		this.takesEntity = expectations.length == 2;
	}

	public void doIgnoreEntity() {
		if (!takesEntity) {
			AdvancedGlobalBulletin.getSharedInstance().QUERYENGINE.Error.push("Illegal state: trying to ignore entity in expectation that does not expect an entity", 
					new String[]{"AnswerExpectation", "doIgnoreEntity"});
		}
		answerElements[0] = TsEntityType.IGNORE;
		ignoreEntity = true;
	}

	public boolean matches(QueryAnswer exp) {
		return (matches(exp.answerElements));
	}

	private boolean matches(TsEntity[] match) {
		if (ignoreEntity) {
			return answerElements[1] == match[0].getType();
		}

		if (!takesEntity) {
			return answerElements[0] == match[0].getType();
		} else {
			return answerElements[0] == match[0].getType() && answerElements[1] == match[1].getType();
		}
	}

}