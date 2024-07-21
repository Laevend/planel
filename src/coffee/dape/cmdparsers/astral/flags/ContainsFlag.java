package coffee.dape.cmdparsers.astral.flags;

/**
 * @author Laeven
 * 
 * This class defines the contains flag.
 * An argument passed must contain the sub string provided
 */
public class ContainsFlag extends CFlag
{
	private String contains;
	
	public ContainsFlag(String contains)
	{
		this.contains = contains;
	}
	
	@Override
	public boolean validate(String argument)
	{
		return argument.contains(this.contains);
	}
	
	@Override
	public String getFailMessage()
	{
		return "Arg does not contain sub-string '" + contains + "'";
	}
}
