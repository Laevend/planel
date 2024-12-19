package coffee.dape.utils.structs.maps;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Laeven
 * @since 1.0.0
 */
public interface MultiMap<K,L,V>
{
	public Collection<Map<L,V>> values();
	
	public Collection<V> valuesFor(K key);
	
	public Set<K> keySet();

	public void put(K key1,L key2,V value);
	
	public void putAll(MultiMap<K,L,V> mmap);

	public void remove(K key1);
	
	public void remove(K key1,L key2);

	public V get(K key1,L key2);

	public Map<L,V> get(K key1);

	public boolean containsKey(K key);

	public boolean containsKey(K key1,L key2);

	public boolean containsValue(V value);

	public boolean containsValue(K key,V value);

	public int size();

	public boolean isEmpty();

	public void clear();
	
	public String toString();
}
