package coffee.dape.utils.security;

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * A string that has its contents scrambled in memory to obscure it in JVM dumps 
 * @author Melloware 
 * https://stackoverflow.com/questions/51242150/java-equivalent-of-securestring
 */
public class SecureString implements CharSequence
{
	private final int[] chars;
	private final int[] pad;
	
	public SecureString(final CharSequence original)
	{
		this(0, original.length(), original);
	}

	public SecureString(final int start,final int end,final CharSequence original)
	{
		final int length = end - start;
		pad = new int[length];
		chars = new int[length];
		scramble(start, length, original);
	}

	@Override
	public char charAt(final int i)
	{
		return (char) (pad[i] ^ chars[i]);
	}

	@Override
	public int length()
	{
		return chars.length;
	}

	@Override
	public CharSequence subSequence(final int start,final int end)
	{
		return new SecureString(start,end,this);
	}	
	
	/**
	 * Convert array back to String but not using toString(). See toString() docs
	 * below.
	 */
	public String asString()
	{
		final char[] value = new char[chars.length];
		
		for(int i = 0; i < value.length; i++)
		{
			value[i] = charAt(i);
		}
		
		return new String(value);
	}
	
	/**
	 * Manually clear the underlying array holding the characters
	 */
	public void clear()
	{
		Arrays.fill(chars,'0');
		Arrays.fill(pad,0);
	}	
	
	/**
	 * Protect against using this class in log statements.
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return "Secure:XXXXX";
	}
	
	/**
	 * Called by garbage collector.
	 * <p>
	 * {@inheritDoc}
	 */
	@SuppressWarnings("removal")
	@Override
	public void finalize() throws Throwable
	{
		try { clear(); }
		finally { super.finalize(); }
	}
	
	/**
	 * Randomly pad the characters to not store the real character in memory.
	 * @param start start of the {@code CharSequence}
	 * @param length length of the {@code CharSequence}
	 * @param characters the {@code CharSequence} to scramble
	 */
	private void scramble(final int start,final int length,final CharSequence characters)
	{
		final SecureRandom random = new SecureRandom();
		
		for(int i = start; i < length; i++)
		{
			final char charAt = characters.charAt(i);
			pad[i] = random.nextInt();
			chars[i] = pad[i] ^ charAt;
		}
	}
}