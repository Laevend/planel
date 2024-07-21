package coffee.dape.utils.minecraftprofile;

public class UUIDData
{
	private String uuid;
	private String name;
	private long expire;
	
	public String getUUID()
	{
		return uuid;
	}
	
	public void setUUID(String uuid)
	{
		this.uuid = uuid;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public long getExpire()
	{
		return expire;
	}
	
	public void setExpire(long expire)
	{
		this.expire = expire;
	}
}