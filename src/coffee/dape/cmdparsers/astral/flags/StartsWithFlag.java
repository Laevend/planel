package coffee.dape.cmdparsers.astral.flags;

/**
 * @author Laeven
 * 
 * This class defines the starts with flag.
 * An argument passed must start with the sub string provided
 */
public class StartsWithFlag extends CFlag
{
	private String startWith;
	
	public StartsWithFlag(String startWith)
	{
		this.startWith = startWith;
	}
	
	@Override
	public boolean validate(String argument)
	{
		return argument.startsWith(this.startWith);
	}
	
	@Override
	public String getFailMessage()
	{
		return "Arg does not start with sub-string '" + startWith + "'";
	}
}
