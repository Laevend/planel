package coffee.dape.cmdparsers.astral.flags;

/**
 * @author Laeven
 * 
 * This class defines the clamp flag.
 * An argument passed must be between the minimum and maximum length
 */
public class ClampLenFlag extends CFlag
{
	private int minLength;
	private int maxLength;
	
	public ClampLenFlag(int minLength,int maxLength)
	{
		this.minLength = minLength;
		this.maxLength = maxLength;
	}
	
	@Override
	public boolean validate(String argument)
	{
		return argument.length() >= this.minLength && argument.length() <= this.maxLength;
	}

	@Override
	public String getFailMessage()
	{
		return "Arg is not between length " + minLength + " - " + maxLength;
	}
}
