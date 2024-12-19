package coffee.dape.utils.structs.maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class DualKeyHashMap<KA,KB,V> implements DualKeyMap<KA,KB,V>
{
	private Map<KA,Group<KA,KB>> ka = new HashMap<>();
	private Map<KB,Group<KA,KB>> kb = new HashMap<>();
	private Map<Group<KA,KB>,V> values = new HashMap<>();
	
	@Override
	public Collection<V> values()
	{
		return values.values();
	}

	@Override
	public Set<KA> keySetA()
	{
		return ka.keySet();
	}

	@Override
	public Set<KB> keySetB()
	{
		return kb.keySet();
	}

	@Override
	public List<V> put(KA keyA, KB keyB, V value)
	{
		List<V> oldRefValues = new ArrayList<>();
		
		oldRefValues.add(this.removeA(keyA));
		oldRefValues.add(this.removeB(keyB));
		
		// Put new dual key to value
		Group<KA,KB> kGroup = new KeyGroup<>();
		kGroup.setKeyA(keyA);
		kGroup.setKeyB(keyB);
		
		values.put(kGroup,value);
		ka.put(keyA,kGroup);
		kb.put(keyB,kGroup);
		
		return oldRefValues;
	}
	
	@Override
	public V putA(KA keyA, V value)
	{
		V oldValue = this.removeA(keyA);
		
		// Put new dual key to value
		Group<KA,KB> kGroup = new KeyGroup<>();
		kGroup.setKeyA(keyA);
		
		values.put(kGroup,value);
		ka.put(keyA,kGroup);
		
		return oldValue;
	}

	@Override
	public V putB(KB keyB, V value)
	{
		V oldValue = this.removeB(keyB);
		
		Group<KA,KB> kGroup = new KeyGroup<>();
		kGroup.setKeyB(keyB);
		
		values.put(kGroup,value);
		kb.put(keyB,kGroup);
		
		return oldValue;
	}

	@Override
	public void putAll(DualKeyMap<KA,KB,V> dkMap)
	{
		for(Group<KA,KB> group : dkMap.getKeyGroupSet())
		{
			if(group.hasKeyA() && group.hasKeyB())
			{
				this.put(group.getKeyA(),group.getKeyB(),dkMap.getA(group.getKeyA()));
				continue;
			}
			
			if(group.hasKeyA())
			{
				this.putA(group.getKeyA(),dkMap.getA(group.getKeyA()));
				continue;
			}
			
			if(group.hasKeyB())
			{
				this.putB(group.getKeyB(),dkMap.getB(group.getKeyB()));
			}
		}
	}
	
	@Override
	public void linkAToExistingB(KA keyA, KB keyB)
	{		
		if(!kb.containsKey(keyB)) { return; }
		
		kb.get(keyB).setKeyA(keyA);
		this.removeA(keyA);
		ka.put(keyA,kb.get(keyB));
	}

	@Override
	public void linkBToExistingA(KB keyB, KA keyA)
	{
		if(!ka.containsKey(keyA)) { return; }
		
		ka.get(keyA).setKeyB(keyB);
		this.removeB(keyB);
		kb.put(keyB,ka.get(keyA));
	}
	
	@Override
	public void unlinkKeyA(KA key)
	{
		if(!ka.containsKey(key)) { return; }
		
		if(ka.get(key).hasKeyB())
		{
			ka.get(key).setKeyA(null);
			ka.remove(key);
		}
		else
		{
			this.removeA(key);
		}
	}

	@Override
	public void unlinkKeyB(KB key)
	{
		if(!kb.containsKey(key)) { return; }
		
		if(kb.get(key).hasKeyA())
		{
			kb.get(key).setKeyB(null);
			kb.remove(key);
		}
		else
		{
			this.removeB(key);
		}
	}
	
	@Override
	public V removeA(KA key)
	{
		// Remove KeyGroup if attached to an existing KeyA
		if(!ka.containsKey(key)) { return null; }
		
		Group<KA,KB> kGroup = ka.get(key);
		V oldValue = values.get(kGroup);
		values.remove(kGroup);
		if(kGroup.hasKeyB()) { kb.remove(kGroup.getKeyB()); }
		ka.remove(key);
		
		return oldValue;
	}

	@Override
	public V removeB(KB key)
	{
		// Remove KeyGroup if attached to an existing KeyB
		if(!kb.containsKey(key)) { return null; }
		
		Group<KA,KB> kGroup = kb.get(key);
		V oldValue = values.get(kGroup);
		values.remove(kGroup);
		if(kGroup.hasKeyA()) { ka.remove(kGroup.getKeyA()); }
		kb.remove(key);
		
		return oldValue;
	}

	@Override
	public V getA(KA key)
	{
		if(!ka.containsKey(key)) { return null; }
		
		return values.get(ka.get(key));
	}

	@Override
	public V getB(KB key)
	{
		if(!kb.containsKey(key)) { return null; }
		
		return values.get(kb.get(key));
	}
	
	@Override
	public Set<Group<KA,KB>> getKeyGroupSet()
	{
		return values.keySet();
	}

	@Override
	public boolean containsKeyA(KA key)
	{
		return ka.containsKey(key);
	}

	@Override
	public boolean containsKeyB(KB key)
	{
		return kb.containsKey(key);
	}

	@Override
	public boolean containsValue(V value)
	{
		return values.containsKey(value);
	}
	
	@Override
	public int size()
	{
		return values.size();
	}

	@Override
	public boolean isEmpty()
	{
		return values.isEmpty();
	}

	@Override
	public void clear()
	{
		this.ka.clear();
		this.kb.clear();
		this.values.clear();
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		
		for(Group<KA,KB> key : this.ka.values())
		{
			sb.append("[KeyA=" + key.getKeyA() != null ? key.getKeyA().toString() : "null");
			sb.append(",");
			sb.append("KeyB=" + key.getKeyB() != null ? key.getKeyB().toString() : "null");
			sb.append(",");
			sb.append("Value=" + values.get(key).toString());			
			sb.append("}]");
		}
		
		sb.append("}");
		return sb.toString();
	}
	
	private class KeyGroup<Ka,Kb> implements Group<Ka,Kb>
	{		
		private Ka keyA;
		private Kb keyB;
		private boolean hasKeyA;
		private boolean hasKeyB;
		
		@Override
		public void setKeyA(Ka keyA)
		{
			this.keyA = keyA;
			this.hasKeyB = (keyA != null) ? true : false;
		}
		
		@Override
		public void setKeyB(Kb keyB)
		{
			this.keyB = keyB;
			this.hasKeyB = (keyB != null) ? true : false;
		}
		
		@Override
		public boolean hasKeyA()
		{
			return hasKeyA;
		}
		
		@Override
		public boolean hasKeyB()
		{
			return hasKeyB;
		}
		
		@Override
		public Ka getKeyA()
		{
			return keyA;
		}
		
		@Override
		public Kb getKeyB()
		{
			return keyB;
		}
	}
}