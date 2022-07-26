package dev.brassboard.module;

/**
 * 
 * @author Laeven
 * Allows for modules to implement reloading support by implementing this method.
 * Only modules that implement this will be assumed to have reload support.
 */
public interface Reloadable
{
	public void reload();
}
