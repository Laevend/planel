package coffee.dape.config;

import java.util.Map;

import coffee.khyonieheart.lilac.value.TomlObject;

/**
 * 
 * @author Laeven
 * Gets default key>value pairs for the main configuration file
 */
public interface Configurable
{
	public Map<String,TomlObject<?>> getDefaults();
}
