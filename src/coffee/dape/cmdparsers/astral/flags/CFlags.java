package coffee.dape.cmdparsers.astral.flags;

/**
 * @author Laeven
 * 
 * This is used to construct a CFlag (Command Flag)
 * Command Flags are used to apply some level of validation before the type is checked
 */
public class CFlags
{
	public static LenFlag len(int argumentLength)
	{
		return new LenFlag(argumentLength);
	}
	
	public static MinLenFlag minLen(int argumentLengthMin)
	{
		return new MinLenFlag(argumentLengthMin);
	}
	
	public static MaxLenFlag maxLen(int argumentLengthMax)
	{
		return new MaxLenFlag(argumentLengthMax);
	}
	
	public static ClampLenFlag clampLen(int argumentLengthMin,int argumentLengthMax)
	{
		return new ClampLenFlag(argumentLengthMin,argumentLengthMax);
	}
	
	public static RegexFlag regex(String regex)
	{
		return new RegexFlag(regex);
	}
	
	public static ContainsFlag contains(String string)
	{
		return new ContainsFlag(string);
	}
	
	public static StartsWithFlag startsWith(String string)
	{
		return new StartsWithFlag(string);
	}
	
	public static EndsWithFlag endsWith(String string)
	{
		return new EndsWithFlag(string);
	}
	
	public static UppercaseFlag uppercase()
	{
		return new UppercaseFlag();
	}
	
	public static LowercaseFlag lowercase()
	{
		return new LowercaseFlag();
	}
	
	public static InRangeFlag inRange(int lower,int upper)
	{
		return new InRangeFlag(lower,upper);
	}
}
