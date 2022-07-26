package dev.brassboard.module.exceptions;

/**
 * @author Laeven
 * 
 * <p>This exception is thrown when a file that is not a jar file is passed
 * as a file argument to a class collector
 */
public class InvalidJarFileException extends Exception
{
	private static final long serialVersionUID = 940542356215937181L;

	public InvalidJarFileException(String errorMessage,Throwable err)
	{
        super(errorMessage,err);
    }
	
	public InvalidJarFileException(String errorMessage)
	{
        super(errorMessage);
    }
}
