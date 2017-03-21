package tsxdk.entity;

import java.util.Map;

import tsxdk.parser.TsFieldStack;
import api.data.TsEntityType;
import api.data.TsSimpleResultDTI;

public class TsSimpleResult implements TsEntity, TsSimpleResultDTI {
	private Map<String, Object> content;
	
	public TsSimpleResult() {
		
	}

	@Override
	public void update(TsFieldStack stack) {
		content = stack.getRaw();
		stack.ensureEmpty();
	}

	@Override
	public Map<String, Object> getContent() {
		return content;
	}

	@Override
	public TsEntityType getType() {
		return TsEntityType.TSSIMPLERESULT;
	}

	@Override
	public int getTsPropHash() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTsPropHash(String str) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTSXDBID(int id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getTSXDBID() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setGlueID(int id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getGlueID() {
		throw new UnsupportedOperationException();
	}

}
