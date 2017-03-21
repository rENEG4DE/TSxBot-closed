package system.config.type;

import system.config.configurable.CVar;
import api.service.WorkerAction;

public class IntVar extends CVar {
	//Integer value;
	private Integer value;
	
	public IntVar(String identifier, int defVal) {
		super(identifier, null);
		this.value = defVal;
	}
	
	public IntVar(String identifier, int defVal, WorkerAction onSet) {
		super(identifier, onSet);
		this.value = defVal;
		super.onSet();
	}
	
	@Override
	public void parse(String value) {
		this.value = Integer.parseInt(value);
		onSet();
	}
  
	@Override
	@SuppressWarnings("unchecked")
	public Integer getValue() {
		return value;
	}

}
