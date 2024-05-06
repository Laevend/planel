package coffee.dape.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.google.common.base.Charsets;

import coffee.dape.Dape;
import coffee.dape.utils.FileOpUtils;
import coffee.dape.utils.Logg;
import coffee.dape.utils.TimeUtils;
import coffee.khyonieheart.lilac.Lilac;
import coffee.khyonieheart.lilac.TomlConfiguration;
import coffee.khyonieheart.lilac.value.TomlObject;
import coffee.khyonieheart.lilac.value.TomlTable;
import coffee.khyonieheart.lilac.value.formatting.TomlComment;

/**
 * 
 * @author Laeven
 * Implements TOML for managing server configurations
 */
public class TomlConfig implements DapeConfig
{
	private TomlConfiguration config;
	private Map<String,TomlObject<?>> defaults;
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
	public TomlConfig(Path configFile,Map<String,TomlObject<?>> defaults,String header)
	{
		this.configFile = configFile;
		this.defaults = defaults;
		this.header = header;
		
		if(Files.exists(configFile))
		{ 
			load();
			setDefaults();
			save();
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
		
		/**
		 * Wow! error handling when parsing a config file! What a concept MD_5!
		 * Frickin' idiot...
		 */
		
		try
		{
			this.config = Lilac.tomlParser().getDecoder().decode(configFile.toFile());
			this.loaded = true;
		}
		catch(Exception e)
		{
			Logg.error("Error! Configuration file could not be parsed/read correctly!",e);
			replaceCorruptedConfig();
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
		
		String fileName = "corrupt_arc_config_" + TimeUtils.getDateFormat(TimeUtils.PATTERN_DASH_dd_MM_yy);
		Path corruptConfigPath = Dape.internalFilePath(CORRUPT_CONFIG_DIR);
		
		int extraNumber = 1;
		
		// In the event (somehow) more than configuration file corrupts in the same second.
		while(Files.exists(Paths.get(corruptConfigPath.toAbsolutePath().toString() + File.separator + fileName)))
		{
			fileName = "corrupt_arc_config_" + TimeUtils.getDateFormat(TimeUtils.PATTERN_DASH_dd_MM_yy) + " (" + extraNumber + ")";
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
		if(this.config == null) { Logg.error("The TomlConfiguration Object is null"); return; }
		
		try(Writer writer = new OutputStreamWriter(new FileOutputStream(this.configFile.toFile()), Charsets.UTF_8))
		{
			String data = Lilac.tomlParser().getEncoder().encode(this.config,Lilac.tomlParser().setPreserveComments(true));
			writer.write(data);
		}
		catch(Exception e)
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
			int split = key.indexOf('.');			
			String tableName = split == -1 ? "global" : key.substring(0,split);
			String tableMemberName = split == -1 ? key : key.substring(split,key.length());
			
			if(!this.config.hasKey(tableName))
			{
				this.config.set(tableName,new TomlTable(tableName,List.of()));
			}
			
			TomlTable newTable = new TomlTable(tableName,List.of());
			newTable.rebase(this.config.getTable(tableName));
			newTable.get().put(tableMemberName,this.defaults.get(key));
			this.config.set(tableName,newTable);
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
	public TomlConfiguration get()
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
		
		try
		{
			this.config = Lilac.tomlParser().getDecoder().decode(configFile.toFile());
		}
		catch(Exception e)
		{
			Logg.fatal("Cannot load configuration file as TOML threw exception!",e);
			Dape.forceShutdown();
			return;
		}
		
		setDefaults();
		this.config.set("header",new TomlComment(header));
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
}
