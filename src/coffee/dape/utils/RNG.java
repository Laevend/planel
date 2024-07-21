package coffee.dape.utils;

/**
 * @author Laeven
 * A random number generator (RNG) class for rolling virtual dice
 */
public class RNG
{
	/**
	 * Rolls RNG number
	 * @param chanceOfSuccess Chance that the roll results in success in percentage (0.0 = 0%, 1.0 = 100%, 0.5 = 50%)
	 * @return True if roll succeeded, false otherwise
	 */
	public static boolean roll(double chanceOfSuccess)
	{
		return MathUtils.inclusiveRange(0.0d,chanceOfSuccess,MathUtils.getRandom(0.0d,1.0d));
	}
	
	/**
	 * Rolls RNG number
	 * @param chanceOfSuccess Chance that the roll results in success in percentage (0.0 = 0%, 1.0 = 100%, 0.5 = 50%)
	 * @return True if roll succeeded, false otherwise
	 */
	public static boolean roll(float chanceOfSuccess)
	{
		return MathUtils.inclusiveRange(0.0f,chanceOfSuccess,MathUtils.getRandom(0.0f,1.0f));
	}
}
