package dev.brassboard.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Laeven
 */
public class ConfigFile
{
	private FileConfiguration config;
	private Map<String,Object> defaults;
	private File configFile;
	private String header;
	
	/**
	 * Create a new configuration file object
	 * @param configFile Configuration file
	 * @param defaults Default values for this configuration file
	 * @param header The header comment of the configuration file
	 */
	public ConfigFile(File configFile,Map<String,Object> defaults,String header)
	{
		this.configFile = configFile;
		this.defaults = defaults;
		this.header = header;
		mkConfigFile();
	}
	
	/**
	 * Create a new configuration file object
	 * @param path Path of configuration file
	 * @param defaults Default values for this configuration file
	 * @param header The header comment of the configuration file
	 */
	public ConfigFile(String path,Map<String,Object> defaults,String header)
	{
		this.configFile = new File(path);
		this.defaults = defaults;
		this.header = header;
		mkConfigFile();
	}
	
	/**
	 * Loads configuration file
	 */
	public void load()
	{
		if(this.configFile == null) { Logs.error("The configuration file location is null"); return; }
		this.config = YamlConfiguration.loadConfiguration(configFile);
	}
	
	/**
	 * Saves configuration file
	 */
	public void save()
	{
		if(this.configFile == null) { Logs.error("The configuration file location is null"); return; }
		if(this.config == null) { Logs.error("The FileConfiguration Object is null"); return; }
		
		try
		{
			this.config.save(this.configFile);
		}
		catch (IOException e)
		{
			Logs.error("Failed to save configuration file!",e);
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
			this.config.set(key,this.defaults.get(key));
		}
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
	private void mkConfigFile()
	{
		if(this.configFile.exists()) { load(); return; }
		
		this.configFile.getParentFile().mkdirs();
		
		try
		{
			this.configFile.createNewFile();
			load();
			setDefaults();
			this.config.options().setHeader(List.of(this.header));
			save();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
