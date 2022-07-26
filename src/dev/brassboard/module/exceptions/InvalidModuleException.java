package dev.brassboard.module.exceptions;

/**
 * @author Laeven (Zack)
 * @since 0.6.0
 * 
 * <p>This exception is thrown when any part of an external BrassBoardModule
 * does not conform to the standards of what must be in a module.
 */
public class InvalidModuleException extends Exception
{
	private static final long serialVersionUID = 2473482744940397751L;
	
	public InvalidModuleException(String errorMessage,Throwable err)
	{
        super(errorMessage,err);
    }
	
	public InvalidModuleException(String errorMessage)
	{
        super(errorMessage);
    }
}
