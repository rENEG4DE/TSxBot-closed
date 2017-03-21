package api.data;

/**
 * A Data Transfer Interface to use Configurable Variables
 * @author kornholio
 */
public interface CVarDTI {

	/**
	 * Get the Identifier/Name of this CVar
	 * @return Identifier
	 */
	String getIdentifer();

	/**
	 * Returns the managed Configurable as mutable Object
	 * @return Value
	 */
	<T> T getValue();

}