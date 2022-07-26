package dev.brassboard.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class JavaUtils
{
	private static int cachedJavaVersion = -1;
	private static int cachedClassVersion = -1;
	
	/**
	 * Get the Java major class version of a class file.
	 * @param classFile a .class file
	 * @return Java class version. A return value of -1 means it could not be obtained.
	 */
	public static int getClassVersionOfModule(File classFile)
	{
		try
		{
			String data = run("javap","-verbose",classFile.getAbsolutePath());
			int index = data.indexOf("major version: ");
			String version = data.substring(index + "major version: ".length(),index + "major version: ".length() + 2);
			return Integer.parseInt(version);
		}
		catch (Exception e)
		{
			Logs.error("Error occured executing command 'javap' to retrieve class version!",e);
		}
		
		return -1;
	}
	
	/**
	 * Get the Java JVM major class version being used by the server
	 * @return Java class version. A return value of -1 means it could not be obtained.
	 */
	public static int getClassVersion()
	{
		if (cachedClassVersion != -1) { return cachedClassVersion; }

		String classVersion = System.getProperty("java.class.version");
		
		if(classVersion == null) { return -1; }
		
		cachedClassVersion = Integer.parseInt(classVersion);
	    return cachedClassVersion;
	}
	
	/**
	 * Get the Java JVM version being used by the server
	 * @return Java version. A return value of -1 means it could not be obtained.
	 */
	public static int getJavaVersion()
	{
	    if(cachedJavaVersion != -1) { return cachedJavaVersion; }

	    String version = System.getProperty("java.version");
	    
	    if(version == null) { return -1; }
	    
	    if (version.startsWith("1.")) // 8 or older
	    {
	        cachedJavaVersion = Integer.parseInt(version.substring(2, 3));
	        return cachedJavaVersion;
	    }
	        
	    cachedJavaVersion = Integer.parseInt(version.split("\\.")[0]);
	    
	    // 9 or newer
	    return cachedJavaVersion;
	}
	
	private static final String NEWLINE = System.getProperty("line.separator");

    /**
     * Execute an OS command.
     * 
     * <p>Note. This does not work with piping '|'
     * @param command the command to run
     * @return the output of the command
     * @throws IOException if an I/O error occurs
     */
    public static String run(String... command) throws IOException
    {
        ProcessBuilder pb = new ProcessBuilder(command).redirectErrorStream(true);
        Process process = pb.start();
        StringBuilder result = new StringBuilder(80);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream())))
        {
            while (true)
            {
                String line = in.readLine();
                if (line == null)
                    break;
                result.append(line).append(NEWLINE);
            }
        }
        
        return result.toString();
    }
}
