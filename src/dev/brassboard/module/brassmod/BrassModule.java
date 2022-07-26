package dev.brassboard.module.brassmod;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import dev.brassboard.Brassboard;
import dev.brassboard.module.BaseModule;
import dev.brassboard.module.enums.Initialiser;
import dev.brassboard.module.exceptions.InvalidJarFileException;
import dev.brassboard.module.exceptions.InvalidModuleException;
import dev.brassboard.module.utility.ModuleClassLoader;
import dev.brassboard.utils.ConfigFile;

/**
 * @author Laeven (Zack)
 * @since 0.6.0
 * 
 * <p>A class designed to act as the main back end module
 * class for all Vertex Modules
 */
public abstract class BrassModule extends BaseModule
{
	private BrassCover cover;
	private String mainClasspath;
	
	private Map<String,Object> configDefaults;
	private File dataFolder;
	private ConfigFile config;

	public BrassModule() {}
	
	protected void preInit(BrassCover cover,ModuleClassLoader loader,Initialiser init) throws InvalidModuleException, InvalidJarFileException
	{
		this.setJar(cover.getJarPath().toFile());
		this.setClassLoader(loader);
		this.setInitialiser(init);
    	this.cover = cover;
		this.dataFolder = new File(Brassboard.getInstance().getDataFolder() + File.separator + "mods" + File.separator + this.cover.getName());
		this.configDefaults = new HashMap<>();
	}
	
	/**
	 * Called by BrassBoard when this module is initialising.
	 * 
	 * <p>Only called if a module has not already been initialised.
	 */
	protected abstract void onInit();
	
	/**
	 * Called by BrassBoard when this module is being enabled.
	 * 
	 * <p>Only called if a module is initialised and not already enabled.
	 */
	protected abstract void onEnable();
	
	/**
	 * Called by BrassBoard when this module is being disabled.
	 * 
	 * <p>Only called if a module is already enabled
	 */
	protected abstract void onDisable();
	
	/**
	 * Called by BrassBoard when this module is being unloaded.
	 * 
	 * <p>Only called if a module is already enabled
	 */
	protected abstract void onUnload();
	

	/**
	 * Post-initialiser for configuration files
	 * Called after {@link #onInit()}
	 */
	protected void postInit()
	{		
		addDefaultConfigValue("version",this.cover.getVersion().getVersionAsString());
		this.config = new ConfigFile(this.dataFolder.getAbsoluteFile() + File.separator + "config.yml",this.configDefaults,this.cover.getName() + " configuration file");    	
	}
	
	/**
	 * Gets the data folder 
	 * @return
	 */
	public File getDataFolder()
	{
		return dataFolder;
	}
	
	/**
	 * Get configuration file
	 * @return Configuration file
	 */
	public ConfigFile getConfig()
	{
		return config;
	}
	
	/**
	 * Reloads the configuration file without saving it
	 * @return
	 */
	public void reloadConfig()
	{
		if(this.config == null) { return; }
		this.config.load();
	}
	
	/**
	 * Reloads the configuration file without saving it
	 * @return
	 */
	public void saveAndReloadConfig()
	{
		if(this.config == null) { return; }
		this.config.save();
		this.config.load();
	}
	
	public abstract <T extends BrassModule> Class<T> getInstance();
	
	/**
	 * Adds a default configuration setting
	 * @param s Key
	 * @param o Value
	 */
	public void addDefaultConfigValue(String s,Object o)
	{
		this.configDefaults.put(s,o);
	}
	
	public String getMainClasspath()
	{
		return mainClasspath;
	}
	
	public BrassCover getCover()
	{
		return cover;
	}
}