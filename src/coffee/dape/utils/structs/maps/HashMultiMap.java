package coffee.dape.utils.structs.maps;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class HashMultiMap<K,L,V> implements MultiMap<K,L,V>
{
	private Map<K,Map<L,V>> map = new HashMap<>(); 
	
	@Override
	public Collection<Map<L,V>> values()
	{
		return this.map.values();
	}
	
	@Override
	public Collection<V> valuesFor(K key)
	{
		return this.map.get(key).values();
	}

	@Override
	public Set<K> keySet()
	{
		return map.keySet();
	}

	@Override
	public void put(K key1,L key2,V value)
	{		
		if(this.map.containsKey(key1))
		{
			this.map.get(key1).put(key2,value);
		}
		else
		{			
			Map<L,V> nestedMap = new HashMap<>();
			nestedMap.put(key2,value);
			
			this.map.put(key1,nestedMap);
		}
	}
	
	@Override
	public void putAll(MultiMap<K,L,V> mmap)
	{
		for(K key : mmap.keySet())
		{
			this.map.put(key,mmap.get(key));
		}
	}

	@Override
	public V get(K key1,L key2)
	{
		if(this.map.get(key1) == null) { return null; }
		
		return this.map.get(key1).get(key2);
	}
	
	@Override
	public Map<L,V> get(K key1)
	{
		return this.map.get(key1);
	}
	
	@Override
	public void remove(K key1)
	{
		this.map.remove(key1);
	}
	
	@Override
	public void remove(K key1,L key2)
	{
		if(this.map.get(key1) == null) { return; }
		
		this.map.get(key1).remove(key2);
	}
	
	@Override
	public boolean containsKey(K key)
	{
		return this.map.containsKey(key);
	}
	
	@Override
	public boolean containsKey(K key1,L key2)
	{
		if(this.map.get(key1) == null) { return false; }
		
		return this.map.get(key1).containsKey(key2);
	}
	
	@Override
	public boolean containsValue(V value)
	{
		return this.map.containsValue(value);
	}
	
	@Override
	public boolean containsValue(K key,V value)
	{
		if(this.map.get(key) == null) { return false; }
		
		return this.map.get(key).containsValue(value);
	}
	
	@Override
	public int size()
	{
		return this.map.size();
	}
	
	@Override
	public boolean isEmpty()
	{
		return this.map.isEmpty();
	}
	
	@Override
	public void clear()
	{
		this.map.clear();
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		
		for(K key : this.map.keySet())
		{
			sb.append("[Key=" + key.toString());
			sb.append(",");
			sb.append("Value={");
			
			for(L key2 : this.map.get(key).keySet())
			{
				sb.append("[Key=" + key2.toString());
				sb.append(",");
				sb.append("Value=" + this.map.get(key).get(key2).toString());
				sb.append("]");
			}
			
			sb.append("}]");
		}
		
		sb.append("}");
		return sb.toString();
	}
}
