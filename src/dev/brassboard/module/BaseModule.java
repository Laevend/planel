package dev.brassboard.module;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import dev.brassboard.module.enums.Initialiser;
import dev.brassboard.module.utility.ModuleClassLoader;
import dev.brassboard.utils.Logs;


/**
 * @author Laeven (Zack)
 * @since 0.6.0
 * 
 * <p>A class designed to act as base for dynamically
 * loading external jars at runtime.
 */
public abstract class BaseModule
{
	private Set<String> dependantModules;
	private Set<String> entitledModules;
	private ModuleClassLoader loader;
	private File jar;
	private Initialiser init;
	
	/**
	 * Creates a new base module
	 * @param moduleName Name of this module
	 * @param jar Physical Jar file being used to load this module from
	 * @param loader The ModuleClassLoader used to load this modules classes
	 */
	public BaseModule(File jar,ModuleClassLoader loader,Initialiser init)
	{
		this.jar = jar;
		this.loader = loader;
		this.init = init;
	}
	
	public BaseModule() {}
	
	/**
	 * Get a set of module names that this module
	 * depends on to operate correctly
	 * @return Dependant module names
	 */
	public Set<String> getDependantModules()
	{
		return dependantModules == null ? Collections.emptySet() : this.dependantModules;
	}
	
	/**
	 * Adds a module name that this module depends on
	 * @param dependantModule Name of a module this module depends on
	 */
	public void addDependantModule(String dependantModule)
	{
		if(this.dependantModules == null) { this.dependantModules = new HashSet<>(); }
		this.dependantModules.add(dependantModule);
	}
	
	/**
	 * Get a set of module names that are dependant
	 * on this module to operate correctly
	 * @return Entitled module names
	 */
	public Set<String> getEntitledModules()
	{
		return entitledModules == null ? Collections.emptySet() : this.entitledModules;
	}
	
	/**
	 * Adds a module name that is dependant on this module
	 * @param entitledModule Name of a module that depends on this module
	 */
	public void addEntitledModule(String entitledModule)
	{
		if(this.entitledModules == null) { this.entitledModules = new HashSet<>(); }
		this.entitledModules.add(entitledModule);
	}
	
	/**
	 * Get the ModuleClassLoader used to load this module
	 * @return ModuleClassLoader
	 */
	public ModuleClassLoader getClassLoader()
	{
		return loader;
	}
	
	protected void setClassLoader(ModuleClassLoader loader)
	{
		this.loader = loader;
	}
	
	/**
	 * Get this modules initialiser type
	 * @return
	 */
	public Initialiser getInit()
	{
		return init;
	}
	
	protected void setInitialiser(Initialiser init)
	{
		this.init = init;
	}

	/**
	 * Get the Jar file this module was loaded from
	 * @return Jar file of this module
	 */
	public File getJar()
	{
		return jar;
	}
	
	protected void setJar(File jar)
	{
		this.jar = jar;
	}
	
	/**
	 * Gets the filename of the Jar used to load this module
	 * @return Filename of Jar
	 */
	public String getJarFilename()
	{
		return jar.getName();
	}
	
	/**
	 * Attempts to close the ModuleClassLoader used to
	 * load this module. This will stop any new classes
	 * being loaded.
	 * 
	 * <p>If the loader is null, it will not be closed.
	 * @throws IOException
	 */
	public void close() throws IOException
	{
		if(this.loader == null) { Logs.warn("Loader is null " + getJarFilename()); return; }
		this.loader.close();
	}
}