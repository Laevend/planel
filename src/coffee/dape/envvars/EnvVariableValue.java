package coffee.dape.envvars;

import java.util.Objects;

import coffee.dape.utils.Logg;

/**
 * 
 * @author Laeven
 *
 */
public class EnvVariableValue
{
	protected String value;
	
	public static EnvVariableValue of(String value)
	{
		EnvVariableValue envVar = new EnvVariableValue();
		envVar.value = value;
		return envVar;
	}
	
	/**
	 * Retrieves this environment variable
	 * @return environment variable
	 */
	public String getString()
	{
		return value;
	}
	
	/**
	 * Attempts to retrieve this environment variable as an integer
	 * @return environment variable as an integer, -1 if parsing fails
	 */
	public int getInt()
	{
		try
		{
			Objects.requireNonNull(value,"Environment value cannot be null!");
			return Integer.parseInt(value);
		}
		catch(Exception e)
		{
			Logg.error("Cannot convert Environment value '" + value + " to integer!");
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Attempts to retrieve this environment variable as a long
	 * @return environment variable as a long, -1 if parsing fails
	 */
	public long getLong()
	{
		try
		{
			Objects.requireNonNull(value,"Environment value cannot be null!");
			return Long.parseLong(value);
		}
		catch(Exception e)
		{
			Logg.error("Cannot convert Environment value '" + value + " to long!");
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Attempts to retrieve this environment variable as a boolean
	 * @return environment variable as an boolean, false if parsing fails
	 */
	public boolean getBoolean()
	{
		try
		{
			Objects.requireNonNull(value,"Environment value cannot be null!");
			return Boolean.parseBoolean(value);
		}
		catch(Exception e)
		{
			Logg.error("Cannot convert Environment value '" + value + " to boolean!");
			e.printStackTrace();
			return false;
		}
	}
}