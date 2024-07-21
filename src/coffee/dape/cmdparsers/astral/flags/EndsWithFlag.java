package coffee.dape.cmdparsers.astral.flags;

/**
 * @author Laeven
 * 
 * This class defines the ends with flag.
 * An argument passed must end with the sub string provided
 */
public class EndsWithFlag extends CFlag
{
	private String endsWith;
	
	public EndsWithFlag(String startWith)
	{
		this.endsWith = startWith;
	}
	
	@Override
	public boolean validate(String argument)
	{
		return argument.endsWith(this.endsWith);
	}
	
	@Override
	public String getFailMessage()
	{
		return "Arg does not end with sub-string '" + endsWith + "'";
	}
}
