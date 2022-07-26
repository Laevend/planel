package dev.brassboard.module.librarymod;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import dev.brassboard.module.BaseModule;
import laeven.arc.utilities.ItemBuilder;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class LibraryModule extends BaseModule
{
	private ItemStack modIcon;
	private String name;
	private String version;
	
	/**
	 * Create a new library module
	 * @param library 
	 * @param libraryJarFileName Name of the library jar
	 */
	public LibraryModule(String name,String version,File library)
	{
		super(name);
		this.name = name;
		this.version = version;
    	this.modIcon = new ItemBuilder(Material.KNOWLEDGE_BOOK)
    			.setName("&e" + this.name)
    			.addLore("&9Version &8- &e" + this.version)
    			.addLore("&9Jar &8- &e" + library.getName())
    			.getItemStack();
	}

	/**
	 * Gets the ItemStack that represents this module in
	 * the module panel
	 * @return Module icon
	 */
	public ItemStack getModIcon()
	{
		return modIcon;
	}

	public String getName()
	{
		return name;
	}

	public String getVersion()
	{
		return version;
	}
}