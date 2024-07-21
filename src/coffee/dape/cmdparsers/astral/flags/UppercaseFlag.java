package coffee.dape.cmdparsers.astral.flags;

/**
 * @author Laeven
 * 
 * This class defines the uppercase flag.
 * An argument passed must have any alpha characters be all uppercase
 */
public class UppercaseFlag extends CFlag
{
	@Override
	public boolean validate(String argument)
	{
		String uppercaseArg = argument.toUpperCase();
		return uppercaseArg.equals(argument);
	}
	
	@Override
	public String getFailMessage()
	{
		return "Arg is not uppercase";
	}
}
