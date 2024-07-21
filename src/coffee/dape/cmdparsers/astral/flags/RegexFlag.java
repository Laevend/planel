package coffee.dape.cmdparsers.astral.flags;

/**
 * @author Laeven
 * 
 * This class defines the regex flag.
 * An argument passed must match the regex rule
 */
public class RegexFlag extends CFlag
{
	private String regex;
	
	public RegexFlag(String regex)
	{
		this.regex = regex;
	}
	
	@Override
	public boolean validate(String argument)
	{
		return argument.matches(this.regex);
	}
	
	@Override
	public String getFailMessage()
	{
		return "Arg does not match regex rule " + regex;
	}
}
