package coffee.dape.envvars;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import coffee.dape.Dape;
import coffee.dape.utils.Logg;
import coffee.dape.utils.interfaces.Initialiser;
import coffee.dape.utils.structs.Namespace;

/**
 * 
 * @author Laeven
 * Environment variables 
 */
public class EnvVariables implements Initialiser
{
	private static ConcurrentHashMap<Namespace,EnvVariable> serverEnvVars = new ConcurrentHashMap<>(HardVariables.values().length);

	@Override
	public void init()
	{
		// Add hard variables to the map
		for(HardVariables var : HardVariables.values())
		{
			serverEnvVars.put(var.namespace,new Variable(var.namespace,var.description,var.value,var.defaultValue));
		}
	}
	
	/**
	 * Gets the EnvVariable of an environment variable
	 * @param namespace Namespace of this environment variable
	 * @return The environment variable associated with this namespace, null otherwise
	 */
	public static EnvVariable getVar(HardVariables hardVar)
	{
		Objects.requireNonNull(hardVar,"Namespace cannot be null!");
		return serverEnvVars.contains(hardVar.namespace) ? serverEnvVars.get(hardVar.namespace) : null;
	}
	
	/**
	 * Gets the EnvVariableValue of an environment variable
	 * @param namespace Namespace of this environment variable
	 * @return The environment variable value associated with this namespace, null otherwise
	 */
	public static EnvVariableValue getVarValue(HardVariables hardVar)
	{
		Objects.requireNonNull(hardVar,"Namespace cannot be null!");
		return serverEnvVars.contains(hardVar.namespace) ? serverEnvVars.get(hardVar.namespace).getValue() : null;
	}
	
	/**
	 * Gets the EnvVariable of an environment variable
	 * @param namespace Namespace of this environment variable
	 * @return The environment variable associated with this namespace, null otherwise
	 */
	public static EnvVariable getVar(Namespace namespace)
	{
		Objects.requireNonNull(namespace,"Namespace cannot be null!");
		return serverEnvVars.contains(namespace) ? serverEnvVars.get(namespace) : null;
	}
	
	/**
	 * Gets the EnvVariableValue of an environment variable
	 * @param namespace Namespace of this environment variable
	 * @return The environment variable value associated with this namespace, null otherwise
	 */
	public static EnvVariableValue getVarValue(Namespace namespace)
	{
		Objects.requireNonNull(namespace,"Namespace cannot be null!");
		return serverEnvVars.contains(namespace) ? serverEnvVars.get(namespace).getValue() : null;
	}
	
	/**
	 * Gets the EnvVariableValue of an environment variable
	 * @param namespace Namespace of this environment variable
	 * @return The environment variable value associated with this namespace, returns default value provided otherwise
	 */
	public static EnvVariableValue getVarValueOrDefault(Namespace namespace,EnvVariableValue defaultValueReturn)
	{
		Objects.requireNonNull(namespace,"Namespace cannot be null!");
		return serverEnvVars.contains(namespace) ? serverEnvVars.get(namespace).getValue() : defaultValueReturn;
	}
	
	/**
	 * Checks if an environment variable of this namespace exists
	 * @param namespace Namespace of this environment variable
	 * @return True if an environment variable of this namespace exists, false otherwise
	 */
	public static boolean hasVar(Namespace namespace)
	{
		Objects.requireNonNull(namespace,"Namespace cannot be null!");
		return serverEnvVars.contains(namespace);
	}
	
	/**
	 * Adds an environment variable to the server
	 * @param namespace Namespace of this environment variable
	 * @param envVar The environment variable to store
	 * @return True if this variable was stored successfully, false otherwise
	 */
	public static boolean addVar(Namespace namespace,EnvVariable envVar)
	{
		Objects.requireNonNull(namespace,"Namespace cannot be null!");
		Objects.requireNonNull(envVar,"EnvVariable cannot be null!");
		
		if(hasVar(namespace))
		{
			Logg.error("Cannot store environment variable with key '" + envVar.getKey().toString() + "' because its namespace already exists!");
			return false;
		}
		
		serverEnvVars.put(namespace,envVar);
		return true;
	}
	
	/**
	 * Hard coded built-in variables that are setup on initialisation.
	 * They all use the namespace of 'DAPE' and will always exist in the map.
	 */
	public enum HardVariables
	{
		PERMISSION_ERROR_MSG
		(
			"An error to display the player lacks permission for something",
			"You lack permissions for this!",
			"You lack permissions for this!"
		),
		TEST_VAR_REMOVE_LATER_PLS
		(
			"idk they're balloons",
			420,
			66
		),
		;
		
		private final Namespace namespace;
		private final String description;
		private EnvVariableValue value;
		private final EnvVariableValue defaultValue;
		
		/**
		 * Creates an environment variable instance
		 * @param desc Description of this environment variable
		 * @param value Value of this environment variable
		 * @param defaultValue Default value of this environment variable
		 */
		HardVariables(String desc,String value,String defaultValue)
		{
			this.namespace = Namespace.of(Dape.getNamespaceName(),this.toString());
			this.description = desc;
			this.value = EnvVariableValue.of(value);
			this.defaultValue = EnvVariableValue.of(defaultValue);
		}
		
		/**
		 * Creates an environment variable instance
		 * @param desc Description of this environment variable
		 * @param value Value of this environment variable
		 * @param defaultValue Default value of this environment variable
		 */
		HardVariables(String desc,Number value,Number defaultValue)
		{
			this(desc,String.valueOf(value),String.valueOf(defaultValue));
		}
		
		/**
		 * Creates an environment variable instance
		 * @param desc Description of this environment variable
		 * @param value Value of this environment variable
		 * @param defaultValue Default value of this environment variable
		 */
		HardVariables(String desc,boolean value,boolean defaultValue)
		{
			this(desc,String.valueOf(value),String.valueOf(defaultValue));
		}
	}
	
	public static class Common
	{
		
	}
}
