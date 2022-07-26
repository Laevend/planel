package dev.brassboard.module.exceptions;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class MissingAnnotationException extends Exception
{
	private static final long serialVersionUID = 2473482744940397751L;
	
	public MissingAnnotationException(String errorMessage,Throwable err)
	{
        super(errorMessage,err);
    }
	
	public MissingAnnotationException(String errorMessage)
	{
        super(errorMessage);
    }
}
