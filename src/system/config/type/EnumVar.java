package system.config.type;

import java.util.Arrays;

import bot.BotException;

import system.config.configurable.CVar;
import api.service.WorkerAction;

public class EnumVar extends CVar {
	
	private final String[] choices;
	private String value;

	public EnumVar(String identifier, String... defVal) {
		super(identifier);
		choices = defVal;
	}
	
	public EnumVar(String identifier, WorkerAction onSet, String... defVal) {
		super(identifier,onSet);
		this.choices = defVal;
		super.onSet();
	}

	@Override
	public void parse(String value) {
		if (isValidChoice(value)) {
			this.value=value.replace("\"","");			//Remove the " because they are read from the file as part of the string
			super.onSet();
		} else {
			throw new BotException(
					"CVar::EnumVar::parse", "e1", 
					"Value {" + value + "} is not part of "
					+ "possible choices {" + Arrays.deepToString(choices) + "}");
		}
	}
	
	private boolean isValidChoice (String value) {
		return Arrays.asList(choices).contains(value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getValue() {
		return value;
	}
}
