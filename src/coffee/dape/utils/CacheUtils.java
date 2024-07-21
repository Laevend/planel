package coffee.dape.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import coffee.dape.Dape;
import coffee.dape.cmdparsers.astral.annos.CommandEx;
import coffee.dape.utils.tools.ClasspathCollector;

/**
 * 
 * @author Laeven
 *
 */
public class CacheUtils
{
	private static Map<String,CacheList> cacheLists = new HashMap<>();
	
	private static String COMMAND_NAMES = "command_names";
	private static String COMMAND_CLASSPATHS = "command_classpaths";
	
	public static CacheList commandNames()
	{
		String listName = COMMAND_NAMES;
		if(cacheLists.containsKey(listName)) { return cacheLists.get(listName); }
		
		cacheLists.put(listName,new CacheList(listName,false)
		{
		    @Override
		    protected void cache()
		    {
		    	try
				{
					for(String cmdClasspath : new ClasspathCollector().getClasspathsWithAnnotation(CommandEx.class))
					{
						Class<?> cmdClass = Class.forName(cmdClasspath,false,Dape.class.getClassLoader());
						CommandEx cmdAnno = cmdClass.getAnnotation(CommandEx.class);
						add(cmdAnno.name());
					}
				}
		    	catch (Exception e) { Logg.error("Could not cache " + listName,e); }
		    }
		});
		
		return cacheLists.get(listName);
	}
	
	public static CacheList commandClasspaths()
	{
		String listName = COMMAND_CLASSPATHS;
		if(cacheLists.containsKey(listName)) { return cacheLists.get(listName); }
		
		cacheLists.put(listName,new CacheList(listName,false)
		{
		    @Override
		    protected void cache()
		    {
		    	try
				{
		    		addAll(new ClasspathCollector().getClasspathsWithAnnotation(CommandEx.class));
				}
		    	catch (Exception e) { Logg.error("Could not cache " + listName,e); }
		    }
		});
		
		return cacheLists.get(listName);
	}
	
	public static class CacheList
	{
		protected String name;
		private List<String> list = new ArrayList<>();
		private boolean invalidateOnRequest;
		
		/**
		 * Creates a new cache list
		 * @param cacheName
		 * @param invalidateOnRequest
		 */
		public CacheList(String cacheName,boolean invalidateOnRequest)
		{
			this.name = cacheName;
			this.invalidateOnRequest = invalidateOnRequest;
		}
		
		/**
		 * Contains the custom logic that generates data to be cached.
		 * 
		 * <p>This logic is declared at initialisation
		 * 
		 * <p>You are to override this method when creating of an instance of this class
		 * with your own logic to cache the list.
		 * 
		 * <p>CacheList operates like an ArrayList
		 * 
		 * <p>Example:
		 * CacheList list = new CacheList("my_new_list",true)
		 * {
		 * 		\@Override
		 * 		public void cache()
		 * 		{
		 * 			// cache logic code here
		 * 		}
		 * }
		 */
		protected void cache()
		{
			throw new UnsupportedOperationException("Cache list " + name + " has not correctly overidden the 'cache()' method!");
		}
		
		/**
		 * Invalidates the cache
		 */
		public void invalidate()
		{
			list.clear();
		}
		
		public List<String> getCache()
		{
			if(list.isEmpty() || invalidateOnRequest)
			{
				invalidate();
				cache();
			}
			
			return this.list;
		}
		
		public Set<String> getCacheAsSet()
		{
			if(list.isEmpty() || invalidateOnRequest)
			{
				invalidate();
				cache();
			}
			
			return new HashSet<>(this.list);
		}

		public int size()
		{
			return this.list.size();
		}

		public boolean isEmpty()
		{
			return this.list.isEmpty();
		}

		public boolean add(String e)
		{
			return this.list.add(e);
		}
		
		public boolean addAll(Collection<? extends String> c)
		{
			return this.list.addAll(c);
		}

		public boolean addAll(int index, Collection<? extends String> c)
		{
			return this.list.addAll(c);
		}

		public void add(int index, String element)
		{
			this.list.add(index,element);
		}
	}
}
