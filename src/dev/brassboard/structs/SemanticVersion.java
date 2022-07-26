package dev.brassboard.structs;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class SemanticVersion
{
	private int majorVersion = 0;
	private int minorVersion = 0;
	private int patchVersion = 0;
	private int hotfixVersion = -1;
	
	/**
	 * Creates a new semantic version
	 * @param majorVersion Major version number
	 * @param minorVersion Minor version number
	 * @param patchVersion patch/bug version number
	 */
	public SemanticVersion(int majorVersion,int minorVersion,int patchVersion)
	{
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.patchVersion = patchVersion;
	}
	
	/**
	 * Creates a new semantic version
	 * @param majorVersion Major version number
	 * @param minorVersion Minor version number
	 * @param patchVersion patch/bug version number
	 * @param hotfixVersion hot fix version number
	 */
	public SemanticVersion(int majorVersion,int minorVersion,int patchVersion,int hotfixVersion)
	{
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.patchVersion = patchVersion;
		this.hotfixVersion = hotfixVersion;
	}
	
	/**
	 * Creates a new semantic version
	 * @param version The string format of this version. Formatted as 'M.m.p' Major, Minor, Patch
	 * 
	 * <p>Example string version: 0.3.2
	 */
	public SemanticVersion(String version)
	{
		parse(version);
	}
	
	public void parse(String s)
	{
		String[] versionParts = s.split("[.]");
		
		if(versionParts.length == 3)
		{
			this.majorVersion = Integer.parseInt(versionParts[0]);
			this.minorVersion = Integer.parseInt(versionParts[1]);
			this.patchVersion = Integer.parseInt(versionParts[2]);
		}
		else if(versionParts.length == 4)
		{
			this.majorVersion = Integer.parseInt(versionParts[0]);
			this.minorVersion = Integer.parseInt(versionParts[1]);
			this.patchVersion = Integer.parseInt(versionParts[2]);
			this.hotfixVersion = Integer.parseInt(versionParts[3]);
		}
		else
		{
			this.majorVersion = 0;
			this.minorVersion = 0;
			this.patchVersion = 0;
		}
	}

	public int getMajorVersion()
	{
		return majorVersion;
	}

	public int getMinorVersion()
	{
		return minorVersion;
	}

	public int getPatchVersion()
	{
		return patchVersion;
	}
	
	public int getHotfixVersion()
	{
		return hotfixVersion;
	}
	
	public String getVersionAsString()
	{
		if(this.hotfixVersion <= -1)
		{
			return this.majorVersion + "." + this.minorVersion + "." + this.patchVersion;
		}
		else
		{
			return this.majorVersion + "." + this.minorVersion + "." + this.patchVersion + "." + this.hotfixVersion;
		}
	}
	
	@Override
	public String toString()
	{
		return getVersionAsString();
	}
}
