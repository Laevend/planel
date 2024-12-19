package coffee.dape.utils.structs.maps;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Laeven
 * @since 1.0.0
 */
public interface DualKeyMap<KA,KB,V>
{
	/**
	 * Returns a Collection view of the values contained in this map. The collection is backed by the map, so changes to the map are reflected in the collection, and vice-versa. If the map is modified while an iteration over the collection is in progress (except through the iterator's own remove operation), the results of the iteration are undefined. The collection supports element removal, which removes the corresponding mapping from the map, via the Iterator.remove, Collection.remove, removeAll, retainAll and clear operations. It does not support the add or addAll operations.
	 * @return a view of the values contained in this map
	 */
	public Collection<V> values();
	
	/**
	 * Returns a Set view of the keys contained in this map. The set is backed by the map, so changes to the map are reflected in the set, and vice-versa. If the map is modified while an iteration over the set is in progress (except through the iterator's own remove operation), the results of the iteration are undefined. The set supports element removal, which removes the corresponding mapping from the map, via the Iterator.remove, Set.remove, removeAll, retainAll, and clear operations. It does not support the add or addAll operations.
	 * @return a set view of the keys contained in this map
	 */
	public Set<KA> keySetA();
	
	/**
	 * Returns a Set view of the keys contained in this map. The set is backed by the map, so changes to the map are reflected in the set, and vice-versa. If the map is modified while an iteration over the set is in progress (except through the iterator's own remove operation), the results of the iteration are undefined. The set supports element removal, which removes the corresponding mapping from the map, via the Iterator.remove, Set.remove, removeAll, retainAll, and clear operations. It does not support the add or addAll operations.
	 * @return a set view of the keys contained in this map
	 */
	public Set<KB> keySetB();
	
	/**
	 * Associates the specified value with the specified keyA and keyB in this map. If the map previously contained a mapping for keyA or KeyB, the old value is replaced.
	 * @param keyA First key with which the specified value is to be associated
	 * @param keyB Second key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return the previous value(s) associated with keyA and keyB, or null if there was no mapping for key. (A null return can also indicate that the map previously associated null with key.) In the event that the two keys reference different values, both of those values are returned.
	 */
	public List<V> put(KA keyA,KB keyB,V value);
	
	/**
	 * Associates the specified value with the specified key in this map. If the map previously contained a mapping for the key, the old value is replaced.
	 * @param keyA key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with key, or null if there was no mapping for key. (A null return can also indicate that the map previously associated null with key.)
	 */
	public V putA(KA keyA,V value);
	
	/**
	 * Associates the specified value with the specified key in this map. If the map previously contained a mapping for the key, the old value is replaced.
	 * @param keyA key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return the previous value associated with key, or null if there was no mapping for key. (A null return can also indicate that the map previously associated null with key.)
	 */
	public V putB(KB keyB,V value);
	
	/**
	 * Copies all of the mappings from the specified map to this map. These mappings will replace any mappings that this map had for any of the keys currently in the specified map.
	 * @param dkMap mappings to be stored in this map
	 */
	public void putAll(DualKeyMap<KA,KB,V> dkMap);
	
	/**
	 * Links a key to an existing value using the values existing key
	 * @param keyA Key to link to the value
	 * @param keyB Existing key the value is already mapped to
	 */
	public void linkAToExistingB(KA keyA,KB keyB);
	
	/**
	 * Links a key to an existing value using the values existing key
	 * @param keyB Key to link to the value
	 * @param keyA Existing key the value is already mapped to
	 */
	public void linkBToExistingA(KB keyB,KA keyA);
	
	/**
	 * Unlinks a key from an existing value. The value is removed if this key is the only key referencing it.
	 * @param key Existing key the value is already mapped to
	 */
	public void unlinkKeyA(KA key);
	
	/**
	 * Unlinks a key from an existing value. The value is removed if this key is the only key referencing it.
	 * @param key Existing key the value is already mapped to
	 */
	public void unlinkKeyB(KB key);
	
	/**
	 * Removes the mapping for the specified key from this map if present.
	 * @param key key whose mapping is to be removed from the map. If another key is associated with this key, it is also removed.
	 * @return the previous value associated with key, or null if there was no mapping for key. (A null return can also indicate that the map previously associated null with key.)
	 */
	public V removeA(KA key);
	
	/**
	 * Removes the mapping for the specified key from this map if present.
	 * @param key key whose mapping is to be removed from the map. If another key is associated with this key, it is also removed.
	 * @return the previous value associated with key, or null if there was no mapping for key. (A null return can also indicate that the map previously associated null with key.)
	 */
	public V removeB(KB key);
	
	/**
	 * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
	 * More formally, if this map contains a mapping from a key k to a value v such that (key==null ? k==null : key.equals(k)), then this method returns v; otherwise it returns null. (There can be at most one such mapping.)
	 * A return value of null does not necessarily indicate that the map contains no mapping for the key; it's also possible that the map explicitly maps the key to null. The containsKeyA operation may be used to distinguish these two cases.
	 * @param key the key whose associated value is to be returned
	 * @return the value to which the specified key is mapped, or null if this map contains no mapping for the key
	 */
	public V getA(KA key);
	
	/**
	 * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
	 * More formally, if this map contains a mapping from a key k to a value v such that (key==null ? k==null : key.equals(k)), then this method returns v; otherwise it returns null. (There can be at most one such mapping.)
	 * A return value of null does not necessarily indicate that the map contains no mapping for the key; it's also possible that the map explicitly maps the key to null. The containsKeyB operation may be used to distinguish these two cases.
	 * @param key the key whose associated value is to be returned
	 * @return the value to which the specified key is mapped, or null if this map contains no mapping for the key
	 */	
	public V getB(KB key);
	
	/**
	 * Returns the KeyGroupSet.
	 * This is used internally for the putAll operation.
	 * @return the KeyGroupSet
	 */
	public Set<Group<KA,KB>> getKeyGroupSet();
	
	/**
	 * Returns true if this map contains a mapping for the specified key.
	 * @param key The key whose presence in this map is to be tested
	 * @return true if this map contains a mapping for the specified key.
	 */
	public boolean containsKeyA(KA key);
	
	/**
	 * Returns true if this map contains a mapping for the specified key.
	 * @param key The key whose presence in this map is to be tested
	 * @return true if this map contains a mapping for the specified key.
	 */
	public boolean containsKeyB(KB key);
	
	/**
	 * Returns true if this map maps one or more keys to the specified value.
	 * @param value value whose presence in this map is to be tested
	 * @return true if this map maps one or more keys to the specified value
	 */
	public boolean containsValue(V value);
	
	/**
	 * Returns the number of key-key-value mappings in this map.
	 * @return the number of key-key-value mappings in this map
	 */
	public int size();
	
	/**
	 * Returns true if this map contains no key-value mappings.
	 * @return true if this map contains no key-value mappings
	 */
	public boolean isEmpty();
	
	/**
	 * Removes all of the mappings from this map. The map will be empty after this call returns.
	 */
	public void clear();
	
	public String toString();
	
	interface Group<Ka,Kb>
	{
		public void setKeyA(Ka keyA);
		
		public void setKeyB(Kb keyB);
		
		public boolean hasKeyA();
		
		public boolean hasKeyB();
		
		public Ka getKeyA();
		
		public Kb getKeyB();
	}
}