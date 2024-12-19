package coffee.dape.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import coffee.dape.Dape;
import coffee.dape.utils.FileOpUtils;
import coffee.dape.utils.Logg;
import coffee.dape.utils.TimeUtils;

/**
 * 
 * @author Laeven
 * Implements YAML for managing server configurations as a backup for TOML
 */
public class YamlConfig implements DapeConfig
{
	private FileConfiguration config;
	private Map<String,Object> defaults;
	private Path configFile;
	private String header;
	private boolean loaded;
	
	private int maxCorruptReplaceAttempts = 5;
	private int corruptReplaceAttempts = 0;
	
	private static final String CORRUPT_CONFIG_DIR = "corrupt_configs";
	
	/**
	 * Create a new configuration file object
	 * @param configFile Configuration file
	 * @param defaults Default values for this configuration file
	 * @param header The header comment of the configuration file
	 */
	public YamlConfig(Path configFile,Map<String,Object> defaults,String header)
	{
		this.configFile = configFile;
		this.defaults = defaults;
		this.header = header;
		
		if(Files.exists(configFile))
		{ 
			load();
			setDefaults();
			save();
			return;
		}
		
		firstTimeSetup();
	}
	
	/**
	 * Loads configuration file that already exists
	 */
	public void load()
	{
		if(this.configFile == null)
		{
			Logg.fatal("Cannot load configuration file as path is null!");
			Dape.forceShutdown();
		}
		
		this.config = YamlConfiguration.loadConfiguration(configFile.toFile());
		
		/**
		 * MD_5 back at it again with soft silencing an exception internally instead of
		 * letting plugin devs handle it smh.
		 * 
		 * This checks if a blank YAML was returned which is what happens if loading fails.
		 */
		if(this.config.saveToString().replace('\n',' ').replace(" ","").length() == 0)
		{
			Logg.error("Error! Configuration file could not be parsed/read correctly!");
			replaceCorruptedConfig();
		}
		else
		{
			this.loaded = true;
		}
	}
	
	/**
	 * In the event the configuration file cannot be read, it is moved to a corrupt files directory (if the user later wishes to consult it).
	 * A new configuration file is generated in its place to allow the server to continue functioning
	 */
	private void replaceCorruptedConfig()
	{
		// Prevents the server from continually attempting to replace the corrupt file in the event it keeps failing
		if((corruptReplaceAttempts++) >= maxCorruptReplaceAttempts)
		{
			Logg.fatal("Maximum corrupt configuration file replacement attempts reached!");
			Dape.forceShutdown();
			return;
		}
		
		corruptReplaceAttempts++;
		
		String fileName = "corrupt_dape_config_" + TimeUtils.getDateFormat(TimeUtils.PATTERN_DASH_dd_MM_yy);
		Path corruptConfigPath = Dape.internalFilePath(CORRUPT_CONFIG_DIR);
		
		FileOpUtils.createDirectories(corruptConfigPath);
		
		int extraNumber = 1;
		
		// In the event (somehow) more than configuration file corrupts in the same second.
		while(Files.exists(Paths.get(corruptConfigPath.toAbsolutePath().toString() + File.separator + fileName)))
		{
			fileName = "corrupt_dape_config_" + TimeUtils.getDateFormat(TimeUtils.PATTERN_DASH_dd_MM_yy) + " (" + extraNumber + ")";
			extraNumber++;
		}
		
		Path finalPath = Dape.internalFilePath(CORRUPT_CONFIG_DIR + File.separator + fileName + ".yml");
		FileOpUtils.copyFile(configFile,finalPath);
		
		if(!Files.exists(finalPath))
		{
			Logg.fatal("Corrupted configuration file '" + configFile.toString() + "' could not be moved to directory '" + corruptConfigPath.toAbsolutePath().toString() + "'!");
			Dape.forceShutdown();
			return;
		}
		
		FileOpUtils.delete(configFile);
		
		if(Files.exists(configFile))
		{
			Logg.fatal("Corrupted configuration file '" + configFile.toString() + "' could not be deleted from original location!");
			Dape.forceShutdown();
			return;
		}
		
		Logg.warn("New configuration file has been created. Old configuration file moved to " + finalPath.toString());
		
		firstTimeSetup();
	}
	
	/**
	 * Saves configuration file
	 */
	public void save()
	{
		if(this.configFile == null) { Logg.error("The configuration file location is null"); return; }
		if(this.config == null) { Logg.error("The FileConfiguration Object is null"); return; }
		
		try
		{
			this.config.save(this.configFile.toFile());
		}
		catch (IOException e)
		{
			Logg.error("Failed to save configuration file!",e);
		}
	}
	
	/**
	 * Sets the defaults for the configuration file
	 * 
	 * <p>New configuration files have this done automatically
	 */
	public void setDefaults()
	{
		for(String key : this.defaults.keySet())
		{
			if(this.config.contains(key)) { continue; }
			this.config.set(key,this.defaults.get(key));
		}
	}
	
	public void reset()
	{
		FileOpUtils.delete(configFile);
		firstTimeSetup();
	}
	
	/**
	 * Gets the configuration
	 * @return FileConfiguration
	 */
	public FileConfiguration get()
	{
		return this.config;
	}
	
	/**
	 * Setup for new configuration files
	 */
	private void firstTimeSetup()
	{
		if(this.configFile == null)
		{
			Logg.fatal("Cannot load configuration file as path is null!");
			Dape.forceShutdown();
			return;
		}
		
		FileOpUtils.createFile(configFile);
		this.config = YamlConfiguration.loadConfiguration(configFile.toFile());
		setDefaults();
		this.config.options().setHeader(List.of(this.header));
		save();
	}

	public boolean isLoaded()
	{
		return loaded;
	}

	@Override
	public void saveConfig()
	{
		this.save();
	}

	@Override
	public void reloadConfig()
	{
		this.load();
	}

	@Override
	public boolean hasKey(String key)
	{
		return get().contains(key);
	}

	@Override
	public String getString(String key)
	{
		return get().getString(key);
	}

	@Override
	public boolean getBoolean(String key)
	{
		return get().getBoolean(key);
	}

	@Override
	public int getInt(String key)
	{
		return get().getInt(key);
	}

	@Override
	public long getLong(String key)
	{
		return get().getLong(key);
	}

	@Override
	public float getFloat(String key)
	{
		// No option to directly get a value as float... bruh
		return (float) get().getDouble(key);
	}

	@Override
	public double getDouble(String key)
	{
		return get().getDouble(key);
	}

	@Override
	public List<String> getStringList(String key)
	{
		return get().getStringList(key);
	}

	@Override
	public List<Boolean> getBooleanList(String key)
	{
		return get().getBooleanList(key);
	}

	@Override
	public List<Integer> getIntList(String key)
	{
		return get().getIntegerList(key);
	}

	@Override
	public List<Long> getLongList(String key)
	{
		return get().getLongList(key);
	}

	@Override
	public List<Float> getFloatList(String key)
	{
		return get().getFloatList(key);
	}

	@Override
	public List<Double> getDoubleList(String key)
	{
		return get().getDoubleList(key);
	}

	@Override
	public void set(String key,Object value)
	{
		get().set(key,value);
	}
}
