package coffee.dape.utils.nms;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class NMSUtils
{
	public enum NMSVersion
	{
		v1_16_R3,
		v1_19_R1,
		v1_19_R3,
	}
	
	private static final int SEGMENT_BITS = 0x7F;
	private static final int CONTINUE_BIT = 0x80;
	
	public static int readVarInt(byte[] varInt)
	{
		int value = 0;
		int position = 0;
		byte currentByte;
		
		for(byte nextByte : varInt)
		{
			currentByte = nextByte;
			value |= (currentByte & SEGMENT_BITS) << position;
			
			if((currentByte & CONTINUE_BIT) == 0) { break; }
			
			position += 7;
			
			if(position >= 32) { throw new RuntimeException("VarInt is too big"); }
		}
		
		return value;
	}
	
	public static byte[] writeVarInt(int value)
	{
		List<Byte> byteValues = new ArrayList<>();
		
		while(true)
		{
			if((value & ~SEGMENT_BITS) == 0)
			{
				byteValues.add((byte) value);
				return listToByteArray(byteValues);
			}
			
			byteValues.add((byte) ((value & SEGMENT_BITS) | CONTINUE_BIT));
			
	        // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
	        value >>>= 7;
		}
	}
	
	private static byte[] listToByteArray(List<Byte> bytes)
	{
		byte[] byteArr = new byte[bytes.size()];
		
		for(int i = 0; i < byteArr.length; i++)
		{
			byteArr[i] = bytes.get(i);
		}
		
		return byteArr;
	}
}
