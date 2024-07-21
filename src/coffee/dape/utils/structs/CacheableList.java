package coffee.dape.utils.structs;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Laeven
 *
 */
public class CacheableList<V>
{
	// Build a data struct that caches a list and only updates the cache when the source changes
	// Have a CacheSourceUpdate event to trigger when a new object is added to the source to tell the cache
	// to re-build itself the next time it's requested, or just rebuild cache on the cache source update
	
	private List<V> list = new ArrayList<>();
	
	/**
	 * Creates a new cached list.
	 */
	public CacheableList()
	{
		buildCache();
	}
	
	/**
	 * Builds/Rebuilds the list.
	 * 
	 * <p>You are to override this method on creation of an instance of this class
	 * with your own methods to build the list.
	 * The last line should always be super.setCache()
	 */
	public void buildCache() {}
	
	/**
	 * Sets the cached list.
	 * @see #buildCache()
	 * @param newList List
	 */
	public void setCache(List<V> newList)
	{
		this.list = newList;
	}
	
	/**
	 * Gets the cache
	 * @return
	 */
	public List<V> getCache()
	{
		return this.list;
	}
}
