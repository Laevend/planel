package coffee.dape.exception;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class InvalidCommandClassException extends Exception
{
	private static final long serialVersionUID = 1L;

	public InvalidCommandClassException(String errorMessage,Throwable err)
	{
        super(errorMessage,err);
    }
	
	public InvalidCommandClassException(String errorMessage)
	{
        super(errorMessage);
    }
}
