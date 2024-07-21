package coffee.dape.cmdparsers.astral.flags;

/**
 * @author Laeven
 * 
 * This class defines the inRange flag.
 * An argument passed must be between the minimum and maximum amounts
 */
public class InRangeFlag extends CFlag
{
	private int minValue;
	private int maxValue;
	
	public InRangeFlag(int minValue,int maxValue)
	{
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	@Override
	public boolean validate(String argument)
	{
		int arg = Integer.valueOf(argument);
		return arg >= this.minValue && arg <= this.maxValue;
	}

	@Override
	public String getFailMessage()
	{
		return "Arg is not between " + minValue + " and " + maxValue;
	}
}
