package system.config.type;

//import java.util.Arrays;

import system.config.configurable.CVar;
import api.service.WorkerAction;

/**
 * @author kornholio
 *
 */
public class BoolVar extends CVar {
	
	private Boolean value;

	/**
	 * @param identifier
	 * @param defVal
	 */
	public BoolVar(String identifier, Boolean defVal) {
		super(identifier);
		this.value = defVal;
	}
	
	public BoolVar(String identifier, Boolean defVal, WorkerAction onSet) {
		super(identifier, onSet);
		this.value = defVal; 
		super.onSet();
	}
	
	@Override
	public void parse(String value) {
		this.value = Boolean.parseBoolean(value);
		onSet();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Boolean getValue() {
		return value;
	}
}
