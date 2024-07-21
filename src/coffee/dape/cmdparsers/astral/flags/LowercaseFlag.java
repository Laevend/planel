package coffee.dape.cmdparsers.astral.flags;

/**
 * @author Laeven
 * 
 * This class defines the lowercase flag.
 * An argument passed must have any alpha characters be all lowercase
 */
public class LowercaseFlag extends CFlag
{
	@Override
	public boolean validate(String argument)
	{
		String lowercaseArg = argument.toLowerCase();
		return lowercaseArg.equals(argument);
	}
	
	@Override
	public String getFailMessage()
	{
		return "Arg is not lowercase";
	}
}
