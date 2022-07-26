package dev.brassboard.module.librarymod;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.brassboard.module.annotations.BrassModule;
import dev.brassboard.module.exceptions.InvalidModuleException;
import dev.brassboard.structs.SemanticVersion;
import dev.brassboard.utils.PrintUtils;

/**
 * @author Laeven (Zack)
 * 
 * <p>A class that shows the cover of a library module.
 * The cover of a module contains all the details in the 
 */
public class LibraryCover
{
	private String name;
	private String description;
	private SemanticVersion version;
	private Material icon = Material.KNOWLEDGE_BOOK;
	private List<String> dependencies;
	
	private Path jarPath;
	
	private ItemStack iconItemStackCache;
	
	private boolean initialised = false;
	private boolean enabled = false;
	private boolean inMemory = false;
	
	protected LibraryCover(File jar) throws InvalidModuleException
	{    	
    	this.jarPath = jar.toPath();
		
		this.name = jar.getName();
    	this.version = new SemanticVersion(0,0,0);
    	
//    	if(moduleData.dependencies().length > 0)
//    	{
//        	this.dependencies = new ArrayList<>();
//        	this.dependencies.addAll(Arrays.asList(moduleData.dependencies()));
//    	}
    	
    	buildModIcon();
	}
	
	private void buildModIcon()
	{
		List<String> lore = new ArrayList<>();
		
		lore.add(PrintUtils.toColour("&8Desc: &e" + this.description));
		lore.add(PrintUtils.toColour("&8Version: &e" + this.version.getVersionAsString()));
		
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

	public Material getIcon()
	{
		return icon;
	}

	public List<String> getDependencies()
	{
		return dependencies;
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