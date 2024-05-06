package coffee.dape.utils.structs;

import java.util.Objects;

/**
 * 
 * @author Laeven
 * Defines a key inside a namespace without requiring the namespace itself to be a plugin
 */
public final class Namespace
{
	private final String name;
	private final String key;
	private final String toString;
	
	/**
	 * Creates a new namespace
	 * @param namespaceName Name of this namespace
	 * @param key The key of this namespace
	 * @return Namespace object
	 */
	public static final Namespace of(String namespaceName,String namespaceKey)
	{
		return new Namespace(namespaceName,namespaceKey);
	}
	
	private Namespace(String namespaceName,String namespaceKey)
	{
		Objects.requireNonNull(namespaceName,"Namespace name cannot be null!");
		Objects.requireNonNull(namespaceKey,"Namespace key cannot be null!");
		
		name = namespaceName;
		key = namespaceKey;
		toString = "[Namespace:\"" + name + "\",Key:\"" + key + "\"]";
	}
	
	/**
	 * Returns the name of this namespace
	 * @return
	 */
	public final String getName()
	{
		return name;
	}
	
	/**
	 * Returns the key of this namespace
	 * @return
	 */
	public final String getKey()
	{
		return key;
	}
	
	@Override
	public String toString()
	{
		return toString;
	}
}
