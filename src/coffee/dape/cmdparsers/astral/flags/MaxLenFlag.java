package coffee.dape.cmdparsers.astral.flags;

/**
 * @author Laeven
 * 
 * This class defines the maximum length flag.
 * An argument passed must be equal to or smaller than this length
 */
public class MaxLenFlag extends CFlag
{
	private int maxLength;
	
	public MaxLenFlag(int maxLength)
	{
		this.maxLength = maxLength;
	}
	
	@Override
	public boolean validate(String argument)
	{
		return argument.length() <= this.maxLength;
	}
	
	@Override
	public String getFailMessage()
	{
		return "Arg length is too big! Must be smaller than " + maxLength;
	}
}
