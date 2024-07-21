package coffee.dape.utils.minecraftprofile.data;

import java.util.UUID;

public class MinecraftProfile
{
	private String id;
	private String name;
	private MinecraftProfileTextures texturesProperty;
	
	/**
	 * Minecraft Java Player UUID without hyphens
	 * @return Id
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * Minecraft Java Player UUID
	 * @return UUID
	 */
	public UUID getIdAsUUID()
	{
		return UUID.fromString(id.replaceFirst( "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)","$1-$2-$3-$4-$5"));
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	/**
	 * Name of this Minecraft Java Player
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public MinecraftProfileTextures getTexturesProperty()
	{
		if(texturesProperty == null) { texturesProperty = new MinecraftProfileTextures(); }
		
		return texturesProperty;
	}

	public void setTexturesProperty(MinecraftProfileTextures texturesProperty)
	{
		this.texturesProperty = texturesProperty;
	}
}
