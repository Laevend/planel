package coffee.dape.cmdparsers.astral.flags;

/**
 * @author Laeven
 * 
 * This class defines the minimum length flag.
 * An argument passed must be equal to or longer than this length
 */
public class MinLenFlag extends CFlag
{
	private int minLength;
	
	public MinLenFlag(int minLength)
	{
		this.minLength = minLength;
	}
	
	@Override
	public boolean validate(String argument)
	{
		return argument.length() >= this.minLength;
	}
	
	@Override
	public String getFailMessage()
	{
		return "Arg length is too small! Must be bigger than " + minLength;
	}
}
