package system.config.configurable;

import java.math.BigInteger;

import system.config.type.ArrayVar;
import system.config.type.BigIntVar;
import system.config.type.BoolVar;
import system.config.type.EnumVar;
import system.config.type.IntVar;
import system.config.type.StringVar;
import api.data.CVarDTI;
import api.data.LibFieldType;

public class CVarFactory {

	public static CVarDTI createBoolVar(String identifier, Boolean defVal) {
		return new BoolVar(identifier, defVal);
	}
	
	public static CVarDTI createIntegerVar(String identifier, int defVal) {
		return new IntVar(identifier, defVal);
	}

	public static CVarDTI createBigIntegerVar(String identifier, BigInteger defVal) {
		return new BigIntVar(identifier, defVal);
	}
	
	public static CVarDTI createStringVar(String identifier, String defVal) {
		return new StringVar(identifier, defVal);
	}
	
	public static CVarDTI createEnumVar(String identifier, String... choices) {
		return new EnumVar(identifier, choices);
	}
	
	public static CVarDTI createArrayVar(String identifier, LibFieldType typeHint) {
		return new ArrayVar(identifier, typeHint);
	}
	
	// *Ich mag diese Methode nicht besonders... instanceof sollte man aus Java rausnehmen... 
	/* * 
	public static <T> CVar createVar(String identifier, T defVal) {
		if (defVal instanceof String) {
			return createStringVar(identifier, (String)defVal);
		} else if (defVal instanceof Integer) {
			return createIntegerVar(identifier, (Integer)defVal);
		} else if (defVal instanceof Boolean) {
			return createBoolVar(identifier, (Boolean)defVal);
		}
		
		return null;
	}*/

}
