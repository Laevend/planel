package coffee.dape.envvars;

import java.util.Objects;

import coffee.dape.utils.structs.Namespace;

/**
 * Defines a server environment variable 
 * @author Laeven
 *
 */
public class Variable implements EnvVariable
{
	private final Namespace namespace;
	private final String description;
	private EnvVariableValue value;
	private final EnvVariableValue defaultValue;
	
	/**
	 * Creates an environment variable instance
	 * @param namespace Namespace of this environment variable
	 * @param desc Description of this environment variable (Nullable)
	 * @param value Value of this environment variable
	 * @param defaultValue Default value of this environment variable
	 */
	public Variable(Namespace namespace,String desc,String value,String defaultValue)
	{
		Objects.requireNonNull(namespace,"Namespace cannot be null when creating an environment variable!");
		Objects.requireNonNull(value,"Value cannot be null when creating an environment variable!");
		Objects.requireNonNull(defaultValue,"Default Value cannot be null when creating an environment variable!");
		
		this.namespace = namespace;
		this.description = (desc == null || desc.isBlank() || desc.isEmpty() ? "none" : desc);
		this.value = EnvVariableValue.of(value);
		this.defaultValue = EnvVariableValue.of(defaultValue);
	}
	
	/**
	 * Creates an environment variable instance
	 * @param namespace Namespace of this environment variable
	 * @param desc Description of this environment variable (Nullable)
	 * @param value Value of this environment variable
	 * @param defaultValue Default value of this environment variable
	 */
	public Variable(Namespace namespace,String desc,Number value,Number defaultValue)
	{
		this(namespace,desc,String.valueOf(value),String.valueOf(defaultValue));
	}
	
	/**
	 * Creates an environment variable instance
	 * @param namespace Namespace of this environment variable
	 * @param desc Description of this environment variable (Nullable)
	 * @param value Value of this environment variable
	 * @param defaultValue Default value of this environment variable
	 */
	public Variable(Namespace namespace,String desc,boolean value,boolean defaultValue)
	{
		this(namespace,desc,String.valueOf(value),String.valueOf(defaultValue));
	}
	
	/**
	 * Creates an environment variable instance
	 * @param namespace Namespace of this environment variable
	 * @param desc Description of this environment variable (Nullable)
	 * @param value Value of this environment variable
	 * @param defaultValue Default value of this environment variable
	 */
	public Variable(Namespace namespace,String desc,EnvVariableValue value,EnvVariableValue defaultValue)
	{
		Objects.requireNonNull(namespace,"Namespace cannot be null when creating an environment variable!");
		Objects.requireNonNull(value,"Value cannot be null when creating an environment variable!");
		Objects.requireNonNull(defaultValue,"Default Value cannot be null when creating an environment variable!");
		
		this.namespace = namespace;
		this.description = (desc == null || desc.isBlank() || desc.isEmpty() ? "none" : desc);
		this.value = value;
		this.defaultValue = defaultValue;
	}
	
	/**
	 * Returns the name of this environment variable
	 * @return
	 */
	public Namespace getKey()
	{
		return namespace;
	}
	
	/**
	 * Returns the description of this environment variable
	 * @return
	 */
	public String getDesc()
	{
		return description;
	}
	
	/**
	 * Returns the current value of this environment variable
	 * @return
	 */
	public EnvVariableValue getValue()
	{
		return value;
	}
	
	/**
	 * Sets the current value of this environment variable
	 * @param newValue New environment variable
	 */
	public void setValue(String newValue)
	{
		Objects.requireNonNull(newValue,"New environment value cannot be null!");
		value = EnvVariableValue.of(newValue);
	}
	
	/**
	 * Sets the current value of this environment variable
	 * @param newValue New environment variable
	 */
	public void setValue(Number newValue)
	{
		Objects.requireNonNull(newValue,"New environment value cannot be null!");
		value = EnvVariableValue.of(String.valueOf(newValue));
	}
	
	/**
	 * Sets the current value of this environment variable
	 * @param newValue New environment variable
	 */
	public void setValue(boolean newValue)
	{
		Objects.requireNonNull(newValue,"New environment value cannot be null!");
		value = EnvVariableValue.of(String.valueOf(newValue));
	}
	
	/**
	 * Returns the default value of this environment variable
	 * @return
	 */
	public EnvVariableValue getDefaultValue()
	{
		return defaultValue;
	}
}
