package dev.brassboard.module.brassmod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;

import dev.brassboard.module.BaseModule;
import dev.brassboard.module.ModuleFactory;
import dev.brassboard.module.enums.Initialiser;
import dev.brassboard.module.exceptions.InvalidJarFileException;
import dev.brassboard.module.exceptions.InvalidModuleException;
import dev.brassboard.module.utility.ClasspathCollector;
import dev.brassboard.module.utility.ModuleClassLoader;
import dev.brassboard.utils.Logs;
import dev.brassboard.utils.PrintUtils;

public class BrassModuleInitialiser
{	
	/**
	 * Loads an available module as a BrassModule and initialises it
	 * @param cover ModuleCover for this module
	 * @return True if loading was successful, otherwise false.
	 * @throws InvalidModuleException 
	 */
	public static boolean initModule(BrassCover cover) throws InvalidModuleException
	{
		ClasspathCollector collector = null;
		ModuleClassLoader loader = null;
		
		if(cover == null) { throw new InvalidModuleException("ModuleCover passed is null!"); }
		
		if(ModuleFactory.containsLoadedModule(cover.getName()))
		{
			Logs.fatal("Conflicting module names! Another module called " + cover.getName() + " already exists!");
			return false;
		}
		
		try
    	{
    		if(cover.getJarPath() == null) { Logs.Common.moduleRegisterUnsuccessful(cover.getName()); return false; }
    		
    		File moduleJar = cover.getJarPath().toFile();
    		
    		if(moduleJar.isDirectory() || !moduleJar.getName().endsWith(".jar")) { throw new InvalidJarFileException("Module file referenced by cover is not a .jar file!"); }		
    		if(!ModuleFactory.loadDependancies(cover)) { Logs.Common.moduleRegisterUnsuccessful(cover.getName()); return false; }
    		
    		PrintUtils.println("");
    		PrintUtils.println("		&3Loading module " + cover.getName());
    		PrintUtils.println("");
    		
    		try { loader = new ModuleClassLoader(moduleJar.toURI().toURL()); } catch(Exception e) { Logs.error("Failed to create loader for module " + cover.getName(),e); return false; }
    		collector = new ClasspathCollector(moduleJar,loader);
    		
    		Map<String,Set<String>> classpaths = collector.collectIsAssignableFrom(BaseModule.class).asClasspaths();
    		
    		if(!classpaths.containsKey(BaseModule.class.getSimpleName())) { throw new InvalidModuleException(cover.getName() + " is not a module, or doesn't extend BaseModule!"); }
    		
    		String mainClasspath = classpaths.get(BaseModule.class.getSimpleName()).iterator().next();
			Class<? extends BrassModule> moduleClass = Class.forName(mainClasspath,true,loader).asSubclass(BrassModule.class);
			Constructor<? extends BrassModule> constructor;
			
			try { constructor = moduleClass.getConstructor(); } catch(Exception e) { Logs.error("Failed to find empty constructor for module " + cover.getName(),e); return false; }
			
			BrassModule mod = constructor.newInstance();
			
			mod.preInit(cover,loader,Initialiser.BRASS);
			mod.onInit();
			mod.postInit();
			
			ModuleFactory.getLoadedModulesMap().put(mod.getCover().getName(),mod);
			Logs.Common.moduleRegisterSuccessful(cover.getName());
    		return true;
    	}
    	catch(Exception e)
    	{
    		Logs.error("Module failed to initialise correctly!",e);
    		
        	if(loader != null)
        	{
        		try { loader.close(); }
        		catch (IOException e2)
    			{
    				Logs.fatal("BrassBoard attempted to clearup empty ModuleClassLoader but was unable to close! This will cause a memory leak!",e2);
    			}
        	}
    	}
    	
    	Logs.Common.moduleRegisterUnsuccessful(cover.getName());
    	return false;
	}
}
