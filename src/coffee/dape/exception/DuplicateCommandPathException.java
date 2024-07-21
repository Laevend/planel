package coffee.dape.exception;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class DuplicateCommandPathException extends Exception
{
	private static final long serialVersionUID = 1L;

	public DuplicateCommandPathException(String errorMessage,Throwable err)
	{
        super(errorMessage,err);
    }
	
	public DuplicateCommandPathException(String errorMessage)
	{
        super(errorMessage);
    }
}
