package dev.brassboard.module.brassmod;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginLoadOrder;

import dev.brassboard.Brassboard;
import dev.brassboard.module.annotations.BrassModule;
import dev.brassboard.module.enums.ApiVersion;
import dev.brassboard.module.enums.JavaVersion;
import dev.brassboard.module.exceptions.InvalidModuleException;
import dev.brassboard.structs.SemanticVersion;
import dev.brassboard.utils.PrintUtils;

/**
 * @author Laeven (Zack)
 * @since 0.6.0
 * 
 * <p>A class that shows the cover of a brass module.
 * The cover of a module contains all the details in the 
 */
public class BrassCover
{
	private String name;
	private String description;
	private SemanticVersion version;
	private List<String> authors;
	private Material icon;
	private List<String> dependencies;
	private List<String> externalLibs;
	private List<ApiVersion> apiVersions;
	private JavaVersion jdkVersion;
	private PluginLoadOrder loadOrder;
	
	private Path jarPath;
	
	private ItemStack iconItemStackCache;
	
	private boolean initialised = false;
	private boolean enabled = false;
	private boolean inMemory = false;
	
	protected BrassCover(File jar,BrassModule moduleData) throws InvalidModuleException
	{    	
    	this.jarPath = jar.toPath();
		
		this.name = moduleData.name();
		this.description = moduleData.description();
    	this.version = new SemanticVersion(moduleData.version());
    	this.authors = new ArrayList<>();
    	this.authors.addAll(Arrays.asList(moduleData.author()));
    	this.icon = moduleData.icon();
    	
    	if(moduleData.dependencies().length > 0)
    	{
        	this.dependencies = new ArrayList<>();
        	this.dependencies.addAll(Arrays.asList(moduleData.dependencies()));
    	}
    	
    	if(moduleData.externalLibs().length > 0)
    	{
        	this.externalLibs = new ArrayList<>();
        	this.externalLibs.addAll(Arrays.asList(moduleData.externalLibs()));
    	}
    	
    	this.apiVersions = new ArrayList<>();
    	this.apiVersions.addAll(Arrays.asList(moduleData.api()));
    	
    	if(moduleData.jdk().equals(JavaVersion.MINIMUM_SUPPORTED_FOR_API_VERSION))
    	{
    		if(!this.apiVersions.contains(ApiVersion.valueOf(Brassboard.getNmsVersion()))) {}
    		
    		this.jdkVersion = ApiVersion.valueOf(Brassboard.getNmsVersion()).getMinimumJavaVersion();
    	}
    	else
    	{
    		this.jdkVersion = moduleData.jdk();
    	}
    	
    	this.loadOrder = moduleData.load();
    	
    	buildModIcon();
	}
	
	private void buildModIcon()
	{
		List<String> lore = new ArrayList<>();
		
		lore.add(PrintUtils.toColour("&8Desc: &e" + this.description));
		lore.add(PrintUtils.toColour("&8Version: &e" + this.version.getVersionAsString()));
		lore.add(PrintUtils.toColour("&8:Author(s):"));
		
		for(int i = 0; i < 5 && i < this.authors.size(); i++)
		{
			lore.add(PrintUtils.toColour("  &e- " + this.authors.get(i)));
		}
		
		if(this.authors.size() > 5)
		{
			lore.add(PrintUtils.toColour("  &6- " + (this.authors.size() - 5) + " more.."));
		}
		
		if(this.dependencies != null)
		{
			lore.add(PrintUtils.toColour("&8:Dependencies:"));
			
			for(int i = 0; i < 5 && i < this.dependencies.size(); i++)
			{
				lore.add(PrintUtils.toColour("  &e- " + this.dependencies.get(i)));
			}
			
			if(this.dependencies.size() > 5)
			{
				lore.add(PrintUtils.toColour("  &6- " + (this.dependencies.size() - 5) + " more.."));
			}
		}
		
		if(this.externalLibs != null)
		{
			lore.add(PrintUtils.toColour("&8:External Libs:"));
			
			for(int i = 0; i < 5 && i < this.externalLibs.size(); i++)
			{
				lore.add(PrintUtils.toColour("  &e- " + this.externalLibs.get(i)));
			}
			
			if(this.externalLibs.size() > 5)
			{
				lore.add(PrintUtils.toColour("  &6- " + (this.externalLibs.size() - 5) + " more.."));
			}
		}
		
		lore.add(PrintUtils.toColour("&8:API:"));
		
		for(int i = 0; i < 5 && i < this.apiVersions.size(); i++)
		{
			lore.add(PrintUtils.toColour("  &e- " + this.apiVersions.get(i)));
		}
		
		if(this.apiVersions.size() > 5)
		{
			lore.add(PrintUtils.toColour("  &6- " + (this.apiVersions.size() - 5) + " more.."));
		}
		
		lore.add(PrintUtils.toColour("&8JDK: &e" + this.jdkVersion.toString().toLowerCase()));
		lore.add(PrintUtils.toColour("&8Load: &e" + this.loadOrder.toString().toLowerCase()));
		
		this.iconItemStackCache = new ItemStack(this.icon);
		this.iconItemStackCache.getItemMeta().setDisplayName(PrintUtils.toColour("&9" + this.name));
		this.iconItemStackCache.getItemMeta().setLore(lore);
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public SemanticVersion getVersion()
	{
		return version;
	}

	public List<String> getAuthors()
	{
		return authors;
	}

	public Material getIcon()
	{
		return icon;
	}

	public List<String> getDependencies()
	{
		return dependencies;
	}

	public List<String> getExternalLibs()
	{
		return externalLibs;
	}

	public List<ApiVersion> getApiVersions()
	{
		return apiVersions;
	}

	public JavaVersion getJdkVersion()
	{
		return jdkVersion;
	}

	public PluginLoadOrder getLoadOrder()
	{
		return loadOrder;
	}

	public ItemStack getIconItemStackCache()
	{
		return iconItemStackCache;
	}

	public Path getJarPath()
	{
		return jarPath;
	}

	public boolean isInitialised()
	{
		return initialised;
	}

	protected void setInitialised(boolean initialised)
	{
		this.initialised = initialised;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	protected void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public boolean isInMemory()
	{
		return inMemory;
	}

	protected void setInMemory(boolean inMemory)
	{
		this.inMemory = inMemory;
	}
}