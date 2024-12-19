package coffee.dape.utils.security;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import coffee.dape.utils.Logg;

/**
 * 
 * @author Laeven
 * A really fun and dumb way to obfuscate numbers
 * <p>Designed with the motivation to obscure sensitive numbers in a JVM dump<br>
 * as well as causing inconvenience to those trying to read and modify variables<br>
 * 
 * Make this more readable? Who needs to read this? :D
 */
public final class ObfuscatedRandBaseEncoder
{
	// RNG for the base
	private final SecureRandom randBase = new SecureRandom();
	
	// RNG for the cSet padding
	private final SecureRandom randPad = new SecureRandom();
	
	// RNG for length of cSet values
	private final SecureRandom randLen = new SecureRandom();
	
	// RNG for cSet imposter values
	private final SecureRandom randByte = new SecureRandom();
	private SecureString[] cSet;
	private final int[] map;
	
	private static final Map<String,ObfuscatedRandBaseEncoder> instances = new HashMap<>();
	
	private ObfuscatedRandBaseEncoder()
	{
		map = new int[randBase.nextInt(17 << 4 >> 3 << 7 >> 3 >> 5 << 1,128)];
		generateBase();
	}
	
	/**
	 * Generates an obfuscated random base encoder
	 */
	private final void generateBase()
	{
		int total = 0;
		
		for(int i = 0; i < map.length; i++)
		{
			int next = randPad.nextInt(1,6);
			total+= next;
			map[i] = total;
		}
		
		// Collections.shuffle() no work on primitive peasants!
		shuffle(map);
		
		cSet = new SecureString[total + 1];
		
		// Fill array with garbage byte data :D
		for(int i = 0; i < cSet.length; i++)
		{
			cSet[i] = new SecureString(generateBytes());
		}
		
		// Map size will always be smaller than cSet
		// Fill in spots of the array
		for(int i = 0; i < map.length; i++)
		{
			String finalEncode = generateBytes();
			String encodedChar = Base64.getEncoder().encodeToString(String.valueOf((char) i).getBytes());
			int len = encodedChar.length();
			
			finalEncode = finalEncode.substring(0,finalEncode.length() - len) + encodedChar;
			cSet[map[i]] = new SecureString(finalEncode);
		}
	}
	
	private final String generateBytes()
	{
		byte[] randB = new byte[randLen.nextInt(6,32)];
		randByte.nextBytes(randB);
		return Base64.getEncoder().encodeToString(randB);
	}
	
	/**
	 * Encodes and obfuscates a number while replacing a previous instance of this
	 * @param num Number to encode and obfuscate
	 * @param encodedNum SecureByteArray representation of number
	 * @return SecureByteArray representation
	 */
	public final static SecureByteArray encodeAndReplace(final long num,final SecureByteArray encodedNum)
	{
		if(num < 0)
		{
			Logg.error("Error! Cannot encode a number less than 0 -> " + num);
			throw new IllegalArgumentException("Invalid argument for encoder " + num);
		}
		
		// Remove existing instance
		if(instances.containsKey(byteToHex(encodedNum.asByteArray())))
		{
			instances.remove(byteToHex(encodedNum.asByteArray()));
		}
		
		ObfuscatedRandBaseEncoder ORBE = new ObfuscatedRandBaseEncoder();
		SecureByteArray sba = ORBE.encode_(num);
		instances.put(byteToHex(sba.asByteArray()),ORBE);
		return sba;
	}
	
	/**
	 * Encodes and obfuscates a number
	 * @param num Number to encode and obfuscate
	 * @return SecureByteArray representation
	 */
	public final static SecureByteArray encode(final long num)
	{
		if(num < 0)
		{
			Logg.error("Error! Cannot encode a number less than 0 -> " + num);
			throw new IllegalArgumentException("Invalid argument for encoder " + num);
		}
		
		ObfuscatedRandBaseEncoder ORBE = new ObfuscatedRandBaseEncoder();
		SecureByteArray sba = ORBE.encode_(num);
		instances.put(byteToHex(sba.asByteArray()),ORBE);
		return sba;
	}
	
	private final SecureByteArray encode_(final long num)
	{
		LinkedList<SecureByteArray> encodedNumParts = new LinkedList<>();
		long base = map.length;
		long remainingValue = 0;
		
		/**
		 * Do a division and modules operator on the number to see how many times it will divide into the base
		 * Take the modulus number, encode it, and add it to the array
		 */
		encodedNumParts.addFirst(new SecureByteArray(extract((int) (num % base))));
		remainingValue = num / base;
		
		/**
		 *  If the divided number is bigger than the base we must divide it further, and keep doing it until it's less than the base
		 *  For every loop we add the remainder (modules) to the array after encoding it
		 */
		if(remainingValue != 0)	// If the number got from the first operation is less than the base, we don't need to add another letter to it
		{
			while(remainingValue >= base)
			{
				encodedNumParts.addFirst(new SecureByteArray(extract((int) (remainingValue % base))));
				remainingValue = remainingValue / base;
			}
			
			// Add remaining character
			encodedNumParts.addFirst(new SecureByteArray(extract((int) remainingValue)));
		}
		
		int length = 0;
		
		// Add UUID to decrease collision
		encodedNumParts.addFirst(new SecureByteArray(UUID.randomUUID().toString().getBytes()));
		
		for(SecureByteArray arr : encodedNumParts)
		{
			// +1 to add spacing for a -1 byte value as a delimiter for splitting to decode
			length += arr.asByteArray().length + 1;
		}
		
		byte[] encodedNum = new byte[length];
		int index = 0;
		
		for(SecureByteArray arr : encodedNumParts)
		{
			for(byte b : arr.asByteArray())
			{
				encodedNum[index] = b;
				index++;
			}
			
			encodedNum[index] = ((byte) -1);
			index++;
		}
		
		return new SecureByteArray(encodedNum);
	}
	
	public final static long peek(final SecureByteArray encodedNum)
	{
		if(!instances.containsKey(byteToHex(encodedNum.asByteArray())))
		{
			throw new NullPointerException("Missing instance of ObfuscatedRandBaseEncoder! Cannot peek!");
		}
		
		ObfuscatedRandBaseEncoder ORBE = instances.get(byteToHex(encodedNum.asByteArray()));
		long num = ORBE.decode_(encodedNum);
		return num;
	}
	
	/**
	 * Removes an encoded and obfuscated number
	 * @param encodedNum SecureByteArray representation of number
	 */
	public final static void remove(final SecureByteArray encodedNum)
	{
		if(!instances.containsKey(byteToHex(encodedNum.asByteArray()))) { return; }
		instances.remove(byteToHex(encodedNum.asByteArray()));
	}
	
	/**
	 * Decodes an encoded and obfuscated number
	 * @param encodedNum SecureByteArray representation of number
	 * @return decoded number
	 */
	public final static long decode(final SecureByteArray encodedNum)
	{
		if(!instances.containsKey(byteToHex(encodedNum.asByteArray())))
		{
			throw new NullPointerException("Missing instance of ObfuscatedRandBaseEncoder! Cannot decode!");
		}
		
		ObfuscatedRandBaseEncoder ORBE = instances.remove(byteToHex(encodedNum.asByteArray()));
		long num = ORBE.decode_(encodedNum);
		return num;
	}
	
	NumberFormat nf = NumberFormat.getInstance(Locale.UK);
	
	private final long decode_(final SecureByteArray encodedNum)
	{
		LinkedList<SecureByteArray> encodedNumParts = new LinkedList<>();
		int base = map.length;
		List<Byte> readBuffer = new ArrayList<>();
		
		// Convert single encoded byte value back into multiple encoded byte values
		for(byte b : encodedNum.asByteArray())
		{
			if(((int) b) == -1)
			{
				byte[] primitiveArr = new byte[readBuffer.size()];
				for(int i = 0; i < primitiveArr.length; i++)
				{
					primitiveArr[i] = readBuffer.get(i);
				}
				encodedNumParts.add(new SecureByteArray(primitiveArr));
				readBuffer.clear();
				continue;
			}
			
			readBuffer.add(b);
		}
		
		Map<String,Integer> byteMap = new HashMap<>();
		
		for(int i = 0; i < map.length; i++)
		{
			byteMap.put(byteToHex(extract(i)),i);
		}
		
		// Remove UUID
		encodedNumParts.pollFirst();
		
		int power = encodedNumParts.size() - 1;
		long decodedNum = 0;
		
		for(SecureByteArray encodedNumPart : encodedNumParts)
		{	
			decodedNum+= (byteMap.get(byteToHex(encodedNumPart.asByteArray())) * (long) (power(base,power)));
			power--;
		}
		
		return decodedNum;
	}
	
	private final byte[] extract(final int key)
	{
		int index = map[key];
		return Base64.getDecoder().decode(cSet[index].asString().substring(cSet[index].length() - 4,cSet[index].length()));
	}
	
	private static String byteToHex(byte[] bArr)
	{
		StringBuilder sb = new StringBuilder();
		for(byte b : bArr)
		{
			sb.append(String.format("%02X ", b));
		}
		
		sb.setLength(sb.length() - 1);
		
		return sb.toString();
	}
	
	private void shuffle(int[] intArray)
	{
		SecureRandom sr = new SecureRandom();
		int shuffles = sr.nextInt(intArray.length,intArray.length * 2);
		
		while(shuffles > 0)
		{
			int indexA = -1;
			int indexB = -1;
			
			while(indexA == indexB)
			{
				indexA = sr.nextInt(0,intArray.length - 1);
				indexB = sr.nextInt(0,intArray.length - 1);
			}
			
			int temp = intArray[indexA];
			intArray[indexA] = intArray[indexB];
			intArray[indexB] = temp;
			
			shuffles--;
		}
	}
	
	/**
	 * Math.pow() is flawed and sometimes does not give back the correct number because longs and ints are cast to doubles
	 * When the base is a multiple of 2 or even, then it's fine but it starts trying to round when base is odd number
	 * @param base
	 * @param power
	 * @return
	 */
	private long power(long base,int power)
	{
		long res = 1;
		long sq = base;
		
		while(power > 0)
		{
			if(power % 2 == 1)
			{
				res*= sq;
			}
			
			sq = sq * sq;
			power/= 2;
		}
		
		return res;
	}
}
