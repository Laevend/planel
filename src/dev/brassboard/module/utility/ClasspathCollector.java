package dev.brassboard.module.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import dev.brassboard.module.exceptions.InvalidJarFileException;
import dev.brassboard.utils.Logs;

/**
 * @author Laeven
 */
public class ClasspathCollector
{
	private final Pattern jarPattern = Pattern.compile(".jar$");
	private final ClassLoader loaderRef;
	private final File jarRef;
	
	private  Map<String,Set<String>> classpaths = new HashMap<>();
	
	/**
	 * Creates a new classpath collector
	 * @param jar Jar file to collect classes from
	 * @param loader ClassLoader used 
	 */
	public ClasspathCollector(File jar,ClassLoader loader)
	{
		this.loaderRef = loader;
		this.jarRef = jar;
	}
	
	/**
	 * Collects classpaths from a jar file if they are assignable from classes provided as arguments.
	 * @param assignableFromClasses Class(s) to check that a class is assignable from to retrieve.
	 * @return Map containing classpaths. Can be retrieved using Class.getSimpleName() for each assignable from class
	 * @throws InvalidJarFileException thrown if anything but a jar file is passed
	 */
	public ClasspathCollector collectIsAssignableFrom(Class<?>... assignableFromClasses) throws InvalidJarFileException
	{
		if(!jarPattern.matcher(jarRef.getName()).find()) { throw new InvalidJarFileException("File passed is not of type '.jar'"); }
		
		classpaths.clear();
		
		try
		{
			ZipInputStream zis = new ZipInputStream(new FileInputStream(this.jarRef.getAbsolutePath()));
	        ZipEntry zipEntry = zis.getNextEntry();
	        
	        while(zipEntry != null)
	        {
	        	if(!zipEntry.getName().endsWith(".class")) { zipEntry = zis.getNextEntry(); continue; }
				
				String classpath = zipEntry.getName().replace("/",".");
				
				// This substring method is to remove the '.class' extension on the end
				classpath = classpath.substring(0,classpath.length() - 6);
				               
				try
				{
					Class<?> clazz = Class.forName(classpath,false,loaderRef);
					
					CheckIfAssignable:
					for(Class<?> assignFrom : assignableFromClasses)
					{
						if(!assignFrom.isAssignableFrom(clazz) || assignFrom.getSimpleName().equals(clazz.getSimpleName())) { continue CheckIfAssignable; }
						
						if(!classpaths.containsKey(assignFrom.getSimpleName()))
						{
							classpaths.put(assignFrom.getSimpleName(),new HashSet<>());
						}
						
						classpaths.get(assignFrom.getSimpleName()).add(classpath);
					}
				}
				catch(ClassNotFoundException e)
				{
					Logs.error("Class not found! " + classpath,e);
				}
				
				zipEntry = zis.getNextEntry();
	        }
	        
	        zis.closeEntry();
	        zis.close();
		}
		catch(IOException e)
		{
			Logs.error("An error occured traversing " + this.jarRef.getName());
			throw new InvalidJarFileException("Could not traverse .jar. Perhaps this is not a .jar file or it's corrupt?");
		}
		
		return this;
	}
	
	/**
	 * Returns the collection result as a Map of class path sets
	 * @return Map of a set of class paths from assignable classes.
	 * <p>Class paths of an assignable class can be accessed using the assignable class simple name.
	 * <p>Map may not contain all assignable class simple names if a class was not found that was assignable by it.
	 */
	public Map<String,Set<String>> asClasspaths()
	{
		return classpaths;
	}
	
	/**
	 * Clears the maps of all classes collected
	 */
	public void clear()
	{
		this.classpaths.clear();
	}
}