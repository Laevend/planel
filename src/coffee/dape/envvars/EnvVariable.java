package coffee.dape.envvars;

import coffee.dape.utils.structs.Namespace;

/**
 * 
 * @author Laeven
 * An interface for global server environmental variables
 * <p>{@link #getPrefix()} and {@link #getName()} are combined to 
 */
public interface EnvVariable
{
	/**
	 * Returns the name of this environment variable
	 * @return
	 */
	public Namespace getKey();
	
	/**
	 * Returns the description of this environment variable
	 * @return
	 */
	public String getDesc();
	
	/**
	 * Returns the current value of this environment variable
	 * @return
	 */
	public EnvVariableValue getValue();
	
	/**
	 * Sets the current value of this environment variable
	 * @param newValue New environment variable
	 */
	public void setValue(String newValue);
	
	/**
	 * Sets the current value of this environment variable
	 * @param newValue New environment variable
	 */
	public void setValue(Number newValue);
	
	/**
	 * Sets the current value of this environment variable
	 * @param newValue New environment variable
	 */
	public void setValue(boolean newValue);
	
	/**
	 * Returns the default value of this environment variable
	 * @return
	 */
	public EnvVariableValue getDefaultValue();
}
