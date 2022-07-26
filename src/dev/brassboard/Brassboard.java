package dev.brassboard;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import dev.brassboard.utils.ConfigFile;
import dev.brassboard.utils.Logs;
import dev.brassboard.utils.PrintUtils;

public class Brassboard extends JavaPlugin
{
	private static Brassboard instance;
	private static String nms_version;
	private static ConfigFile config;
	private static File pluginJar;
	private boolean successfulEnable = false;
	
	public final static String prefix = "&8[&9bb&8]";
	
	/* =========== *
	 *   Phase 1
	 * =========== */	
	
	@Override
	public void onEnable()
	{		
    	// Define vars before assigning
		long timeStart = 0;
		long timeEnd = 0;
		
		timeStart = System.currentTimeMillis();
		
		PrintPluginDetails();
		instance = this;
		createConfig();
		
		try
		{
			pluginJar = new File(Brassboard.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		}
		catch(URISyntaxException e)
		{
			Logs.error("Error! Coult not retrieve location of Brassboard plugin!",e);
			return;
		}
		
		configureLogger();
		
		
		//TODO Load Modules
		
		
		// Record loading time
		timeEnd = System.currentTimeMillis();
    	float sec = (timeEnd - timeStart) / 1000F;
		Logs.info("&ePhase 1 initialised in " + sec + " seconds");
	}
	
	/**
	 * Prints plugin details
	 */
	private void PrintPluginDetails()
	{
		PrintUtils.println("&8BrassBoard " + prefix);
		PrintUtils.println("&8Version - &a" + this.getDescription().getVersion());
		PrintUtils.println("&8API - &a" + this.getDescription().getAPIVersion());
		PrintUtils.println("&8JVM Version - &a" + System.getProperty("java.version"));
		PrintUtils.println("&8Contributors:");
		
		for(String author : this.getDescription().getAuthors())
		{
			PrintUtils.println("	&a- " + author);
		}
	}
	
	private void createConfig()
	{
		Map<String,Object> defaults = new HashMap<>();
		
		defaults.put("logger.hide_verbose.all",false);
		defaults.put("logger.hide_warnings",false);
		defaults.put("logger.hide_errors",false);
		defaults.put("logger.hide_fatals",false);
		defaults.put("logger.hide_exceptions",false);
		defaults.put("logger.write_exceptions",false);
		
		defaults.put("crafting-emulator-delete-incompatible-recipes-on-convert",false);
		
		defaults.put("hubcompass.enabled",false);
		defaults.put("hubcompass.world","spawn");
		
		config = new ConfigFile(this.getDataFolder() + File.separator + "config.yml",defaults,"Arc config file");
	}
	
	/**
	 * Registers a bungee plugin channel name
	 * @param pluginChannelName Name of the plugin channel
	 */
	public void registerPluginChannel(String pluginChannelName)
	{
		this.getServer().getMessenger().registerOutgoingPluginChannel(this,pluginChannelName);
	}
	
	private void configureLogger()
	{
		Logs.setHideVerbose(config.get().getBoolean("logger.hide_verbose.all"));
		Logs.setHideWarnings(config.get().getBoolean("logger.hide_warnings"));
		Logs.setHideErrors(config.get().getBoolean("logger.hide_errors"));
		Logs.setHideFatals(config.get().getBoolean("logger.hide_fatals"));
		Logs.setSilenceExceptions(config.get().getBoolean("logger.hide_exceptions"));
		Logs.setWriteExceptions(config.get().getBoolean("logger.write_warnings"));
		
		@SuppressWarnings("unchecked")
		List<String> disabledVerboseClasses = (List<String>) config.get().getList("logger.hide_verbose.class");
		if(disabledVerboseClasses == null) { return; }
		
		Logs.setDisabledVerboseClasses(disabledVerboseClasses);
	}
	
	/* =========== *
	 *   Phase 2
	 * =========== */
	
	public void onEnablePhase2()
	{
    	// Define vars before assigning
		long timeStart = 0;
		long timeEnd = 0;
		
		timeStart = System.currentTimeMillis();
		
		Logs.title("Phase 2/2 Startup Initialising...");
		
		
		//TODO Load POST-WORLD load modules
		
		
		Logs.title("Phase 2/2 Complete.");
		
		// Record loading time
		timeEnd = System.currentTimeMillis();
    	float sec = (timeEnd - timeStart) / 1000F;
		Logs.info("&ePhase 2 initialise time: " + sec + " seconds");
		successfulEnable = true;
	}
	
	/* =========== *
	 *   Disable
	 * =========== */
	
	@Override
	public void onDisable()
	{
		Logs.title("Saving and exiting...");
		
		if(!successfulEnable) { return; }
		
		//TODO Module disable
	}
	
	// Gets instance of this plug
	public static Brassboard getInstance()
	{
		return instance;
	}
	
	public ConfigFile getConfigFile()
	{
		return config;
	}
	
	@Override
	public void saveConfig()
	{
		config.save();
	}
	
	public void reloadConfig()
	{
		config.load();
	}
	
	@Override
	public void saveDefaultConfig()
	{
		throw new UnsupportedOperationException("The default configuration is not supported!");
	}
	
	@Override
	public FileConfiguration getConfig()
	{
		throw new UnsupportedOperationException("The default configuration is not supported! Please use 'Brassboard.getInstance().getConfigFile()");
	}

	public File getPluginJar()
	{
		return pluginJar;
	}

	public boolean isSuccessfulEnable()
	{
		return successfulEnable;
	}
	
	public static String getNmsVersion()
    {
        if(nms_version != null) { return nms_version; }

        String bukkitPackage = Bukkit.getServer().getClass().getPackage().getName();
		nms_version = bukkitPackage.substring(bukkitPackage.lastIndexOf('.') + 1);

        return getNmsVersion();
    }
}