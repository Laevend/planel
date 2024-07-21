package coffee.dape.utils;

public class ByteUtils
{
	public static String byteToHex(byte[] bArr)
	{
		StringBuilder sb = new StringBuilder();
		for(byte b : bArr)
		{
			sb.append(String.format("%02X ", b));
		}
		
		sb.setLength(sb.length() - 1);
		
		return sb.toString();
	}
}
