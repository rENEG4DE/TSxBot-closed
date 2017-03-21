package system.config.type;

import java.util.Arrays;

import api.data.LibFieldType;
import bot.BotException;
import system.config.configurable.CVar;

public class ArrayVar extends CVar {

	private ArrayVar(String identifier) {
		super(identifier);
		value = null;
	}

	public ArrayVar(String identifier, LibFieldType typeHint) {
		this(identifier);
		this.typeHint = typeHint;
	}

	Object[] value;
	LibFieldType typeHint;

	@Override
	public String toString() {
		return Arrays.deepToString(value);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object getValue() {
		return Arrays.copyOf((Object[]) value, value.length);
	}

	@Override
	public void parse(String toParse) {
		// test if String is given format
		if (!toParse.matches("\\{.*\\}")) {
			throw new IllegalArgumentException(
					"Array does not have right format: " + toParse);
		}
		try {
			value = (Object[]) typeHint.parse(toParse.substring(1,toParse.length() - 1));
		} catch (Exception e) {
			throw new BotException("ArrayVar.parse", "e1", "Typehint "+ typeHint + " not applicable for String " + toParse, e);
		}
		
	}

}
