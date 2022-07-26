package dev.brassboard.module.librarymod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import dev.brassboard.module.BaseModule;
import dev.brassboard.module.ModuleFactory;
import dev.brassboard.module.enums.Initialiser;
import dev.brassboard.module.exceptions.InvalidJarFileException;
import dev.brassboard.module.exceptions.InvalidModuleException;
import dev.brassboard.module.utility.ClasspathCollector;
import dev.brassboard.module.utility.ModuleClassLoader;
import dev.brassboard.utils.Logs;
import dev.brassboard.utils.PrintUtils;

public class LibraryModuleInitialiser
{	
	/**
	 * Loads a library module
	 * @param library Library jar file
	 * @return True if loading was successful, otherwise false.
	 */
	public static boolean loadLibrary(File library)
	{
		if(library.isDirectory() || !library.getName().endsWith(".jar")) { return false; }
		
		Manifest mf = readManifest(library);
		
		if(mf == null) { Logs.error(library.getName() + " has a null/missing Manifest!"); return false; }
		
		String libraryName = "Unknown";
		String libraryVersion = "0.0.0";
		Attributes att = mf.getMainAttributes();
		
		libraryName = att.getValue("Bundle-Name");
		
		if(libraryName == null)
		{
			libraryName = att.getValue("Implementation-Title");
			
			if(libraryName == null)
			{
				libraryName = att.getValue("Automatic-Module-Name");
				
				if(libraryName == null)
				{
					Logs.warn(library.getName() + " does not have a title/name field in Manifest.MF to identify itself!");
					libraryName = library.getName().substring(0,library.getName().length() - 4);
				}
			}
		}
		
		libraryVersion = att.getValue("Bundle-Version");
		
		if(libraryVersion == null)
		{
			libraryVersion = att.getValue("Implementation-Version");
			
			if(libraryVersion == null)
			{
				libraryVersion = att.getValue("Specification-Version");
				
				if(libraryVersion == null)
				{
					Logs.warn(library.getName() + " does not have a version!");
				}
			}
		}
		
		ModuleClassLoader loader = null;
		
		try
    	{
    		loader = new ModuleClassLoader(library.toURI().toURL());
    		
    		LibraryModule mod = new LibraryModule(libraryName,libraryVersion,library);
			
			if(ModuleFactory.containsLoadedModule(mod.getName()))
			{
				Logs.fatal("Conflicting library module names! Another library module called " + mod.getName() + " already exists!");
				loader.close();
			}
			
			mod.setJar(library);
			mod.setClassLoader(loader);
			libraryMods.put(mod.getName(),mod);
    		return true;
    	}
    	catch(Exception e)
    	{
    		Logs.error("Library Module failed to dock correctly!",e);
    		
        	if(loader != null)
        	{
        		try { loader.close(); }
        		catch (IOException e2)
    			{
    				Logs.fatal("Vertex attempted to clearup empty ModuleClassLoader but was unable to close! This will cause a memory leak!",e2);
    			}
        	}
    	}
		
		return false;
	}
	
	/**
	 * Reads a libraries Manifest.MF to get information
	 * about the name of the library and version
	 * @param library Library jar
	 * @return Manifest
	 */
	public static Manifest readManifest(File library)
	{
		if(!library.getName().endsWith(".jar")) { return null; }
		
		try(FileInputStream fis = new FileInputStream(library); JarInputStream jarStream = new JarInputStream(fis);)
		{
			return jarStream.getManifest();
		}
		catch(Exception e)
		{
			Logs.error("Unable to read/find manifest of " + library.getName());
		}
		
		return null;
	}
}
