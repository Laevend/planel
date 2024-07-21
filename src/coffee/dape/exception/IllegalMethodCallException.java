package coffee.dape.exception;

/**
 * @author Laeven
 * Exception thrown when Bouncer stops thread calling a method from an illegal class/method
 */
public class IllegalMethodCallException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public IllegalMethodCallException(String className,String methodName,int line,Throwable err)
	{
        super("Bouncer halted illegal method call of '" + Thread.currentThread().getStackTrace()[3].getMethodName() + "'. Called from " + className + " > " + methodName + " (" + line + ")",err);
    }
	
	public IllegalMethodCallException(String className,String methodName,int line)
	{
		super("Bouncer halted illegal method call of '" + Thread.currentThread().getStackTrace()[3].getMethodName() + "'. Called from " + className + " > " + methodName + " (" + line + ")");
    }
}
