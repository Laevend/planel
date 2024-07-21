package coffee.dape.cmdparsers.astral.types;

/**
 * @author Laeven
 * 
 * This class defines the Integer type
 */
public class IntegerType extends ArgumentType
{	
	public IntegerType()
	{
		super("INTEGER");
	}
	
	public boolean isType(String argument)
	{
		try { Integer.parseInt(argument); return true; }
		catch(Exception e) { return false; }
	}
	
	@Override
	public Integer parse(String argument)
	{
		if(!isType(argument)) { throw new IllegalArgumentException("Argument '" + argument + "' can not be parsed to type " + getTypeName()); }
		return Integer.parseInt(argument);
	}
}
