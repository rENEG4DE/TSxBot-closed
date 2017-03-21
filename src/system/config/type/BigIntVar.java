package system.config.type;

import java.math.BigInteger;

import system.config.configurable.CVar;
import api.service.WorkerAction;

public class BigIntVar extends CVar {
	//Integer value;
	private BigInteger value;
	
	public BigIntVar(String identifier, BigInteger defVal) {
		super(identifier, null);
		this.value = defVal;
	}
	
	public BigIntVar(String identifier, BigInteger defVal, WorkerAction onSet) {
		super(identifier, onSet);
		this.value = defVal;
		super.onSet();
	}
	
	@Override
	public void parse(String value) {
		this.value = new BigInteger(value);
		onSet();
	}
  
	@Override
	public BigInteger getValue() {
		return value;
	}

}
