package coffee.dape.cmdparsers.astral.flags;

/**
 * @author Laeven
 * 
 * This class defines the length flag.
 * An argument passed must be exactly of this length
 */
public class LenFlag extends CFlag
{
	private int length;
	
	public LenFlag(int length)
	{
		this.length = length;
	}
	
	@Override
	public boolean validate(String argument)
	{
		return argument.length() == this.length;
	}
	
	@Override
	public String getFailMessage()
	{
		return "Arg is not of length " + length;
	}
}
