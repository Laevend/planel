package coffee.dape.utils.minecraftprofile.data;

public class MinecraftProfileTextures
{
	private String name;
	
	private long timestamp;
	private String profileId;
	private String profileName;
	private boolean signatureRequired = false;
	private String skinURL;
	private String capeURL;
	
	private String signature;
	
	private boolean isSkinSlim = false;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public long getTimestamp()
	{
		return timestamp;
	}
	
	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}
	
	public String getProfileId()
	{
		return profileId;
	}
	
	public void setProfileId(String profileId)
	{
		this.profileId = profileId;
	}
	
	public String getProfileName()
	{
		return profileName;
	}
	
	public void setProfileName(String profileName)
	{
		this.profileName = profileName;
	}
	
	public boolean isSignatureRequired()
	{
		return signatureRequired;
	}

	public void setSignatureRequired(boolean signatureRequired)
	{
		this.signatureRequired = signatureRequired;
	}

	public String getSkinURL()
	{
		return skinURL;
	}
	
	public void setSkinURL(String skinURL)
	{
		this.skinURL = skinURL;
	}
	
	public String getCapeURL()
	{
		return capeURL;
	}
	
	public void setCapeURL(String capeURL)
	{
		this.capeURL = capeURL;
	}
	
	public boolean isSkinSlim()
	{
		return isSkinSlim;
	}
	
	public void setSkinSlim(boolean isSkinSlim)
	{
		this.isSkinSlim = isSkinSlim;
	}
	
	public String getSignature()
	{
		return signature;
	}
	
	public void setSignature(String signature)
	{
		this.signature = signature;
	}
}