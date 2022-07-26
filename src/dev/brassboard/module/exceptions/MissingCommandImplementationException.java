package dev.brassboard.module.exceptions;

/**
 * @author Laeven
 * Thrown when BaseExecutor is extended and the onCommand method is not overridden and implemented
 */
public class MissingCommandImplementationException extends Exception
{
	private static final long serialVersionUID = 1L;

	public MissingCommandImplementationException(String errorMessage,Throwable err)
	{
        super(errorMessage,err);
    }
	
	public MissingCommandImplementationException(String errorMessage)
	{
        super(errorMessage);
    }
}
