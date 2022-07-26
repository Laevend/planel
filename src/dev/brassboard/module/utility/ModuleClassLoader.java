package dev.brassboard.module.utility;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dev.brassboard.module.ModuleFactory;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class ModuleClassLoader extends URLClassLoader
{
    private Map<String,Class<?>> cachedClasses = new HashMap<>();
    private Set<String> internalCachedClassNames = new HashSet<>();
    
    /**
     * Creates a new ModuleClassLoader
     * 
     * <p>This extended URLClassLoader is designed for loading modules
     * @param url Jar file used to load the module
     * @throws MalformedURLException Thrown to indicate that a malformed URL has occurred
     */
    public ModuleClassLoader(URL url) throws MalformedURLException
    {
        super(new URL[] {url});
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException
    {
    	return findClass(name,true);
    }
    
    public Class<?> findClass(String name,boolean checkGlobalCache) throws ClassNotFoundException
    {
    	if(name.startsWith("org.bukkit.") || name.startsWith("net.minecraft."))
    	{
    		throw new ClassNotFoundException(name);
    	}
    	
    	Class<?> result = this.cachedClasses.get(name);
    	
    	if(result == null)
    	{
    		if(checkGlobalCache) { result = ModuleFactory.findClassInGlobalCache(name); }
    		
    		if(result == null)
    		{
    			result = super.findClass(name);
    			
    			if(result != null)
    			{
    				ModuleFactory.addGlobalCachedClass(name,result);
    				this.internalCachedClassNames.add(name);
    			}
    		}
    		else
    		{
    			this.cachedClasses.put(name,result);
    		}
    	}
    	
    	return result;
    }
    
    /**
     * Gets all classes cached by this loader
     * 
     * <p>This set may also contain classes from other modules and libraries
     * @return Set of cached classes
     */
    public Set<String> getClasses()
    {
    	return this.cachedClasses.keySet();
    }
	
    /**
     * Clears the cache of classes
     */
	public void clearCache()
	{
		this.cachedClasses.clear();
		this.internalCachedClassNames.clear();
	}
	
	/**
	 * Gets all classes loaded by this loader
	 * 
	 * <p>This set contains the class paths of classes loaded by this loader
	 * @return Set of class paths
	 */
	public Set<String> getInternalCachedClassNames()
	{
		return internalCachedClassNames;
	}
	
	/**
	 * Removes a cached class
	 * @param classpath Name of class
	 */
    public void removeCachedClass(String classpath)
    {
    	this.cachedClasses.remove(classpath);
    }
}