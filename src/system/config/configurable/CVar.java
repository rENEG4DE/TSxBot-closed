package system.config.configurable;

import api.data.CVarDTI;
import api.service.WorkerAction;

/**
 * Abstract definition of Configurable Values
 * 
 * All CVars have a Name, which is also their identifier,
 * later on, some kind of comment could be added if needed!
 * @author kornholio
 *
 */

public abstract class CVar implements CVarDTI {
	private String identifier;
//	protected T value;
	/**
	 * Defines what is to be done when setting this particular
	 * config Variable
	 * 
	 * For doing special stuff or being able to change cfg-Vars on the fly
	 * Only one Action per CVar!
	 */
	
	private WorkerAction onSet;

	@SuppressWarnings("unused")
	private CVar () {};		//Hide the default-constructor

	protected CVar (String identifier) {
		this.identifier = identifier;
		this.onSet = null;
	}
	
	protected CVar (String identifier, WorkerAction onSet) {
		this.identifier = identifier;
		this.onSet = onSet;
	}
	
	@Override
	public String getIdentifer () {
		return identifier;
	}
	
	public void onSet () {
		if (onSet != null){
			onSet.performAction(getValue());
		}
	}

	/**
	 * Parses a given string representing a value
	 * @param value The string-representation of the value
	 */
	public abstract void parse (String value);
	
	/**
	 * Returns the managed Configurable as mutable Object
	 * @return T
	 */
	@Override
	public abstract <T> T getValue ();
	
	@Override
	public String toString() {
		return getValue().toString();
	}
}
