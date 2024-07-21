package coffee.dape.utils.data;

import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;

import coffee.dape.Dape;
import coffee.dape.utils.data.DataUtils.DType;

public class DataContainerValue
{
	private NamespacedKey key;
	private PersistentDataContainer container;
	
	public DataContainerValue(String key,PersistentDataContainer container)
	{
		this.key = new NamespacedKey(Dape.instance(),key);
		this.container = container;
	}
	
	public boolean asBoolean()
	{
		return (boolean) container.get(key,DType.BOOLEAN.type);
	}
	
	public byte asByte()
	{
		return (byte) container.get(key,DType.BYTE.type);
	}
	
	public byte[] asByteArray()
	{
		return (byte[]) container.get(key,DType.BYTE_ARRAY.type);
	}
	
	public double asDouble()
	{
		return (double) container.get(key,DType.DOUBLE.type);
	}
	
	public float asFloat()
	{
		return (float) container.get(key,DType.FLOAT.type);
	}
	
	public int asInt()
	{
		return (int) container.get(key,DType.INTEGER.type);
	}
	
	public int[] asIntArray()
	{
		return (int[]) container.get(key,DType.INTEGER_ARRAY.type);
	}
	
	public long asLong()
	{
		return (long) container.get(key,DType.LONG.type);
	}
	
	public long[] asLongArray()
	{
		return (long[]) container.get(key,DType.LONG_ARRAY.type);
	}
	
	public short asShort()
	{
		return (short) container.get(key,DType.SHORT.type);
	}
	
	public String asString()
	{
		return (String) container.get(key,DType.STRING.type);
	}
	
	public UUID asUUID()
	{
		return UUID.fromString(asString());
	}
	
	public PersistentDataContainer asTagContainer()
	{
		return (PersistentDataContainer) container.get(key,DType.TAG_CONTAINER.type);
	}
}
