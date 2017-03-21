package tsxdk.entity;

import api.data.TsEntityType;
import api.data.TsReturnDTI;
import tsxdk.parser.LibTsSym;
import tsxdk.parser.TsFieldStack;

public class TsReturn implements TsReturnDTI, TsEntity {
	
	int propHash;
	
	int id;
	String message;
	
	public TsReturn () {
		this(-1,"NO_MESSAGE_SET");
	}
	
	public TsReturn (int id, String message) {
		this.id = id;
		this.message = message;
		
		propHash = message.hashCode() ^ id;
	}
	
	public int getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public void update (TsFieldStack stack) {
		id = (int) stack.popFirst(LibTsSym.ID);
		message = (String) stack.popFirst(LibTsSym.MSG);
	}

	@Override
	public TsEntityType getType() {
		return TsEntityType.TSRETURN;
	}

	@Override
	public int getTsPropHash() {
		return propHash;
	}

	@Override
	public void setTsPropHash(String str) {
		propHash = str.hashCode();
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
	public int getGlueID() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setGlueID(int id) {
		throw new UnsupportedOperationException();
	}
}
