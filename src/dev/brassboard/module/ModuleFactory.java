package dev.brassboard.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.bukkit.Material;

import dev.brassboard.Brassboard;
import dev.brassboard.module.brassmod.BrassCover;
import dev.brassboard.module.brassmod.BrassModule;
import dev.brassboard.module.exceptions.InvalidJarFileException;
import dev.brassboard.module.exceptions.InvalidModuleException;
import dev.brassboard.module.librarymod.LibraryModule;
import dev.brassboard.module.utility.ClasspathCollector;
import dev.brassboard.module.utility.ModYMLCollector;
import dev.brassboard.module.utility.ModuleClassCollector;
import dev.brassboard.module.utility.ModuleClassLoader;
import dev.brassboard.module.utility.ModuleFile;
import dev.brassboard.utils.Logs;
import dev.brassboard.utils.PrintUtils;

/**
 * @author Laeven
 */
public class ModuleFactory
{
	private static Map<String,BrassCover> availableMods = new HashMap<>();
	private static Map<String,BaseModule> loadedMods = new HashMap<>();

	private static Map<String,Class<?>> globalCachedClasses = new HashMap<>();
	private static String modulesDirectory = Brassboard.getInstance().getDataFolder() + File.separator + "modules";
	
	/**
	 * Searches all ModuleClassLaoders for this class
	 * @param name Classpath
	 * @return Class of this classpath
	 */
	public static Class<?> findClassInGlobalCache(String name)
	{
		Logs.verb("GetClassByName -> " + name);
		
		Class<?> cachedClass = globalCachedClasses.get(name);
		
		if(cachedClass != null) { return cachedClass; }
		
		for(BaseModule module : loadedMods.values())
		{
			try
			{
				cachedClass = module.getClassLoader().findClass(name,false);
				return cachedClass;
			} catch (ClassNotFoundException e) {}
		}
		
		return null;
	}
	
	/**
	 * Disables this module
	 * @return True if the enable operation completed successfully, otherwise false
	 */
	public boolean disable()
	{		
		try
		{
			ModuleFactory.unloadEntitled(this);
			onDisable();
			return true;
		}
		catch(Exception e)
		{
			Logs.error("An error occured attempting to disable Module: " + this.cover.getName(),e);
			return false;
		}
	}
	
	
	
	/**
	 * Unloads a VertexModule
	 * @param moduleName Name of the module
	 */
	public static void unloadVertexModule(String moduleName)
	{
		if(!containsLoadedModule(moduleName)) { return; }
		
		BrassModule mod = loadedMods.get(moduleName);
		
		mod.disable();
		mod.getCollector().clear();
		unloadModule(mod);
	}
	
	public static void unloadLibraryModule(String libraryName)
	{
		if(!containsLibraryModule(libraryName)) { return; }
		
		LibraryModule mod = libraryMods.get(libraryName);
		
		unloadModule(mod);
	}
	
	/**
	 * Unloads a BaseModule
	 * @param module BaseModule
	 */
	public static void unloadModule(BaseModule module)
	{
		try
		{			
			unloadEntitled(module);
			
			// Remove all references of any loaded classes from this module
			for(String className : module.getLoader().getInternalCachedClassNames())
			{
				purgeClassFromCache(className);
			}
			
			loadedMods.remove(module.getName());
			module.getLoader().clearCache();
			module.close();
			
			Logg.Common.moduleUnregisterSuccessful(module.getName());
		}
		catch (Exception e)
		{
			Logg.fatal("Module " + module.getName() +" failed to close its class loader! Restart the server asap!",e);
			Logg.Common.moduleUnregisterUnsuccessful(module.getName());
		}
	}
	
	/**
	 * Reloads a VertexModule
	 * 
	 * <p>Attempts to reload a module by unloading it via {@link #unloadModule(BaseModule)}
	 * and then loading it via {@link #loadVertexModule(File)}
	 * @param moduleJar .jar file of the module
	 * @return True if loading was successful, otherwise false.
	 */
	public static boolean reloadVertexModule(BrassModule mod)
	{    	
		Logg.info("Reloading module " + mod.getName());
		
		BrassCover sMod = mod.getShell();
    	unloadModule(mod);
    	if(!loadVertexModule(sMod)) { return false; }
    	return true;
	}
	
	/**
	 * Scans the ../modules/ directory for available modules that
	 * can
	 */
	public static void collectAvailableModules()
	{
		availableMods.clear();
		
		if(!DataPaths.ARC_MODULES.getFile().exists()) { DataPaths.ARC_MODULES.getFile().mkdirs(); return; }
		
		for(File moduleJar : DataPaths.ARC_MODULES.getListOfFilesInDirectory())
		{
			if(moduleJar.isDirectory()) { continue; }
			if(!moduleJar.getName().endsWith(".jar")) { continue; }
			
			Logg.verbose("Collecting " + moduleJar.getName());
			
			try
			{
				File modYML = new ModYMLCollector(moduleJar).collect();
				
				if(modYML == null) { continue; }
				
				ModuleFile file = new ModuleFile(modYML,moduleJar);				
				String name = new String(file.getName());
				String desc = new String(file.getDescription());
				List<String> author = new ArrayList<>(file.getAuthors());
				List<String> dependencies = new ArrayList<>(file.getDependencies());
				String version = null;
				
		    	if(file.getVersion().matches("^[0-9]{1,2}[.]{1}[0-9]{1,3}[.]{1}[0-9]{1,4}$") || file.getVersion().matches("^[0-9]{1,2}[.]{1}[0-9]{1,3}[.]{1}[0-9]{1,4}[.]{1}[0-9]{1,5}$"))
				{
					version = new String(file.getVersion());
				}
				else
				{
					throw new InvalidModuleException("Invalid formatting! Semantic version accepted formats are: 0.0.0 or 0.0.0.0");
				}
		    	
		    	Material iconMaterial = MaterialUtils.getMaterial(file.getIcon());
		    	
		    	if(iconMaterial == null)
		    	{
		    		throw new InvalidModuleException("Invalid icon! The icon attribute does not have a spigot or minecraft material id!");
		    	}
		    	
				availableMods.put(name,new BrassCover(name,desc,author,version,iconMaterial,dependencies,moduleJar));
			}
			catch(Exception e)
			{
				Logg.error("A module failed to be validated for being loadable! " + moduleJar.getName(),e);
			}
		}
	}
	
	/**
	 * Load any dependancies this module has first
	 * @param module BaseModule
	 * @return True if all dependancies for this module loaded successfully, otherwise false.
	 */
	public static boolean loadDependancies(BrassCover module)
	{
		if(module.getDependencies().isEmpty()) { Logg.verbose("No dependencies found! " + module.getName()); return true; }
		
		Logg.verbose("Loading dependencies for " + module.getName());
		
		for(String name : module.getDependencies())
		{
			Logg.verbose("Dependency: " + name);
			
			// Dependancy is already loaded
			if(ModuleFactory.containsLoadedModule(name))
			{ 
				ModuleFactory.getLoadedModule(name).addEntitledModule(module.getName());
				continue;
			}
			
			// Dependancy is listed as an available module to be loaded
			if(ModuleFactory.containsAvailableModule(name))
			{
				// Load dependant module
				if(!ModuleFactory.loadVertexModule(ModuleFactory.getAvailableModule(name))) { return false; }
				ModuleFactory.getLoadedModule(name).addEntitledModule(module.getName());
			}
			else
			{
				// Re-collect list of available modules in the event new modules were placed in the directory
				collectAvailableModules();
				
				if(ModuleFactory.containsAvailableModule(name))
				{
					if(!ModuleFactory.loadVertexModule(ModuleFactory.getAvailableModule(name))) { return false; }
					ModuleFactory.getLoadedModule(name).addEntitledModule(module.getName());
				}
				else
				{
					// Dependancy doesn't exist
					Logg.error("Module " + module.getName() + " is missing dependancy: " + name);
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Unloads any entitled modules for this module
	 * @param module BaseModule
	 * @return
	 */
	public static void unloadEntitled(BaseModule module)
	{
		if(module.getEntitledModules().isEmpty()) { return; }
		
		for(String name : module.getEntitledModules())
		{
			if(!ModuleFactory.containsLoadedModule(name)) { continue; }
			
			ModuleFactory.unloadVertexModule(name);
		}
	}
	
	/**
	 * Loads all BrassModules in the ../modules/ folder
	 */
	public static void loadAllBrassModules()
	{		
		Logg.title("Docking Vertex Modules...");
		
		collectAvailableModules();
		
		for(BrassCover availableModule : getAvailableModules())
		{
			if(containsLoadedModule(availableModule.getName())) { continue; }
			
			loadVertexModule(availableModule);
		}
	}

	/**
	 * Loads all VertexModules in the ../modules/ folder
	 */
	public static void unloadAllVertexModules()
	{
		Set<String> loadedModuleNames = new HashSet<>(loadedMods.keySet());
		
		for(String name : loadedModuleNames)
		{
			BrassModule mod = loadedMods.get(name);
			
			if(mod == null) { continue; }
			
			ModuleFactory.unloadVertexModule(name);
		}
	}
	
	/**
	 * Loads all libraries in the ../libs/ folder
	 */
	public static void loadAllLibraries()
	{
		Logg.title("Loading Libraries...");
		
		if(!DataPaths.ARC_LIBRARIES.getFile().exists()) { DataPaths.ARC_LIBRARIES.getFile().mkdirs(); return; }
		
		for(File jar : DataPaths.ARC_LIBRARIES.getListOfFilesInDirectory())
	    {
			if(jar.isDirectory()) { continue; }
	    	if(!jar.getName().endsWith(".jar")) { continue; }
	    	if(loadLibrary(jar))
	    	{
	    		Logg.Common.libraryRegisterSuccessful(jar.getName());
	    	}
	    	else
	    	{
	    		Logg.Common.libraryRegisterUnsuccessful(jar.getName());
	    	}
	    }
	}

	/**
	 * unloads all libraries
	 */
	public static void unloadAllLibraries()
	{
		for(LibraryModule lib : ModuleFactory.getLibraryModules())
		{
			unloadModule(lib);
		}
	}
	
	/**
	 * Purges a class from all module caches
	 * @param name Class name
	 */
	public static void purgeClassFromCache(String classpath)
	{
		globalCachedClasses.remove(classpath);
		
		for(BaseModule module : loadedMods.values())
		{
			module.getClassLoader().removeCachedClass(classpath);
		}
	}
	

	
	/**
	 * Adds a class to the global cache
	 * @param name Classpath of the class
	 * @param resultClass Resulting class of this classpath
	 */
	public static void addGlobalCachedClass(String name,Class<?> resultClass)
	{
		globalCachedClasses.put(name,resultClass);
	}
	
	/**
	 * Removes a class from the global cache
	 * @param name Classpath of the class
	 */
	public static void removeGlobalCachedClass(String name)
	{
		globalCachedClasses.remove(name);
	}
	
	/**
	 * Checks if an available module exists
	 * @param moduleName The name of the module
	 * @return true/false
	 */
	public static boolean containsAvailableModule(String moduleName)
	{
		return availableMods.keySet().contains(moduleName);
	}
	
	/**
	 * Returns a collection of available modules
	 * @return Collection of modules
	 */
	public static Collection<BrassCover> getAvailableModules()
	{
		return availableMods.values();
	}
	
	/**
	 * Returns a set of available module names
	 * @return Set of module names
	 */
	public static Set<String> getAvailableModuleNames()
	{
		return availableMods.keySet();
	}
	
	/**
	 * Gets an available module
	 * @param moduleName Name of the module
	 * @return ShellModule
	 */
	public static BrassCover getAvailableModule(String moduleName)
	{
		return availableMods.get(moduleName);
	}
	
	/**
	 * Checks if a loaded module exists
	 * @param moduleName The name of the module
	 * @return true/false
	 */
	public static boolean containsLoadedModule(String moduleName)
	{
		return loadedMods.keySet().contains(moduleName);
	}
	
	/**
	 * Returns a collection of loaded modules
	 * @return Collection of modules
	 */
	public static Collection<BrassModule> getLoadedModules()
	{
		return loadedMods.values();
	}
	
	/**
	 * Returns a set of loaded module names
	 * @return Set of module names
	 */
	public static Set<String> getLoadedModuleNames()
	{
		return loadedMods.keySet();
	}
	
	public static Map<String,BaseModule> getLoadedModulesMap()
	{
		return loadedMods;
	}
	
	/**
	 * Gets a VertexModule
	 * @param moduleName Name of the module
	 * @return VertexModule
	 */
	public static BaseModule getLoadedModule(String moduleName)
	{
		return loadedMods.get(moduleName);
	}
	
	/**
	 * Checks if a library module exists
	 * @param moduleName The name of the module
	 * @return true/false
	 */
	public static boolean containsLibraryModule(String moduleName)
	{
		return libraryMods.keySet().contains(moduleName);
	}
	
	/**
	 * Returns a collection of library modules
	 * @return Collection of modules
	 */
	public static Collection<LibraryModule> getLibraryModules()
	{
		return libraryMods.values();
	}
	
	/**
	 * Returns a set of library module names
	 * @return Set of module names
	 */
	public static Set<String> getLibraryModuleNames()
	{
		return libraryMods.keySet();
	}
	
	/**
	 * Gets a LibraryModule
	 * @param moduleName Name of the module
	 * @return LibraryModule
	 */
	public static LibraryModule getLibraryModule(String moduleName)
	{
		return libraryMods.get(moduleName);
	}
	
	/**
	 * Gets the configuration file path for a module
	 * @return Configuration file path
	 */
	public static String getModuleconfigfilepath()
	{
		return moduleConfigFilePath;
	}
}