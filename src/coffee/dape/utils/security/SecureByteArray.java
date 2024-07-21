package coffee.dape.utils.security;


import java.security.SecureRandom;
import java.util.Arrays;

/**
 * 
 * @author Laeven
 * A byte array that has its contents scrambled in memory to obscure it in JVM dumps
 */
public class SecureByteArray
{
	private final int[] byteChars;
	private final int[] pad;
	
	public SecureByteArray(final byte[] original)
	{
		this(0,original.length,original);
	}

	public SecureByteArray(final int start,final int end,final byte[] original)
	{
		final int length = end - start;
		pad = new int[length];
		byteChars = new int[length];
		scramble(start, length, original);
	}

	public byte byteAt(final int i)
	{
		return (byte) (pad[i] ^ byteChars[i]);
	}

	public int length()
	{
		return byteChars.length;
	}
	
	/**
	 * Convert array back to String but not using toString(). See toString() docs
	 * below.
	 */
	public byte[] asByteArray()
	{
		final byte[] value = new byte[byteChars.length];
		
		for(int i = 0; i < value.length; i++)
		{
			value[i] = byteAt(i);
		}
		
		return value;
	}
	
	/**
	 * Manually clear the underlying array holding the characters
	 */
	public void clear()
	{
		Arrays.fill(byteChars,'0');
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
		return "Secure:FF";
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
	private void scramble(final int start,final int length,final byte[] bytes)
	{
		final SecureRandom random = new SecureRandom();
		
		for(int i = start; i < length; i++)
		{
			final byte byteAt = bytes[i];
			pad[i] = random.nextInt();
			byteChars[i] = pad[i] ^ byteAt;
		}
	}
}