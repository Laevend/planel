package coffee.dape.cmdparsers.astral.flags;

/**
 * @author Laeven
 * 
 * This is used to add flags to an argument during definition
 * Flags are additional constraints on the argument before its type is validated
 */
public abstract class CFlag
{
	public abstract boolean validate(String argument);
	
	public abstract String getFailMessage();
}
