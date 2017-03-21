package system.config.type;

import system.config.configurable.CVar;
import api.service.WorkerAction;

public class StringVar extends CVar {
	
	private String value;

	public StringVar(String identifier, String defVal) {
		super(identifier);
		this.value = defVal;
	}
	
	public StringVar(String identifier, String defVal, WorkerAction onSet) {
		super(identifier,onSet);
		this.value = defVal;
		super.onSet();
	}
	
	
	@Override
	public void parse(String value) {
		this.value=value.replace("\"","");			//Remove the " because they are read from the file as part of the string
		super.onSet();
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getValue() {
		return value;
	}
}
