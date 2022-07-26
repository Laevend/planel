package dev.brassboard.module.exceptions;

/**
 * @author Laeven (Zack)
 * @since 0.6.0
 * 
 * <p>This exception is thrown if a problem occurs during a module
 * initialisation and it cannot be evaded.
 */
public class ModuleInitialisationException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public ModuleInitialisationException(String errorMessage,Throwable err)
	{
        super(errorMessage,err);
    }
	
	public ModuleInitialisationException(String errorMessage)
	{
        super(errorMessage);
    }
}
