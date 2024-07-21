package coffee.dape.utils.structs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * 
 * @author Laeven
 *
 */
public class LTree<K,V>
{
	private UUID treeId;
	private Node<K,V> root;
	
	public LTree()
	{
		this.root = new Node<K,V>(null,null,true);
		this.treeId = UUID.randomUUID();
	}
	
	public Node<K,V> getRoot()
	{
		return root;
	}
	
	public UUID getTreeId()
	{
		return treeId;
	}
	
	public static final class Node<K,V> implements Entry<K,V>, Iterable<Node<K,V>>
	{
		public LTree<K,V> tree;
		public Node<K,V> parent;
		public Map<K,Node<K,V>> branches;
		public final K key;
		public V value;
		private final boolean root;
		
		/**
		 * Creates a new node
		 * @param parent Parent node that this node can be traversed from, can be null
		 * @param key Key used to traverse to this node from the parent
		 */
		Node(Node<K,V> parent,K key)
		{
			this(parent,key,false);
		}
		
		/**
		 * Creates a new node
		 * @param parent Parent node that this node can be traversed from, can be null
		 * @param key Key used to traverse to this node from the parent
		 * @param isRoot If this node is the root node
		 */
		protected Node(Node<K,V> parent,K key,boolean isRoot)
		{
			this.parent = parent;
			this.key = key;
			this.root = isRoot;
			
			if(this.parent == null) { return; }
			
			// Add this new node branch to parents map of branches
			this.parent.branches.put(key,this);
		}
		
		/**
		 * Adds a new branch to this nodes branch map and returns the new branch
		 * @param key Key used to traverse to this node from the parent
		 * @param value The value this new branch will hold
		 * @return New branch
		 */
		public Node<K,V> addBranch(K key,V value)
		{
			if(this.branches == null) { this.branches = new HashMap<>(); }
			new Node<K,V>(this,key);
			branches.get(key).setValue(value);
			return branches.get(key);
		}
		
		/**
		 * Flag for if this node is the root node
		 * @return True if this node is root, false otherwise
		 */
		public boolean isRoot()
		{
			return root;
		}
		
		/**
		 * Flag for if this node is a leaf
		 * 
		 * <p>A node is a leaf if it has no branches to traverse to
		 * @return True if this node is considered a leaf, false otherwise
		 */
		public boolean isLeaf()
		{
			return branches == null ? true : branches.isEmpty() ? true : false;
		}
		
		/**
		 * Removes this node branch and branches traversable from this node
		 */
		public void removeBranch()
		{
			if(this.isRoot()) { return; }
			if(this.parent == null) { return; }
			
			this.parent.branches.remove(this.key);
		}
		
		/**
		 * Get they key of this branch node
		 */
		@Override
		public K getKey()
		{
			return key;
		}
		
		/**
		 * Get the value of this branch node
		 */
		@Override
		public V getValue()
		{
			return value;
		}
		
		/**
		 * Sets the value of this branch node and returns the old value
		 */
		@Override
		public V setValue(V newValue)
		{
			V oldValue = this.value;
			this.value = newValue;
			return oldValue;
		}
		
		private void print(StringBuilder buffer,String prefix,String childrenPrefix)
		{
			buffer.append(prefix);
			buffer.append(this.key != null ? this.key.toString() : this.isRoot() ? "root" : "null" );
			buffer.append("\n");
			
			for(Iterator<LTree.Node<K,V>> it = this.iterator(); it.hasNext();)
			{
				Node<K,V> next = it.next();
				
				if(it.hasNext())
				{
					next.print(buffer,childrenPrefix + "├── ",childrenPrefix + "│   ");
				}
				else
				{
					next.print(buffer,childrenPrefix + "└── ",childrenPrefix + "    ");
				}
			}
		}
		
		@Override
		public Iterator<Node<K,V>> iterator()
		{
			return new NodeIterator<K,V>(this);
		}
	}
	
	private static final class NodeIterator<K,V> implements Iterator<Node<K,V>>
	{
		private LTree.Node<K,V> current;
		private Iterator<Node<K,V>> it;
		
		NodeIterator(LTree.Node<K,V> node)
		{
			this.current = node;
			this.it = current.branches == null ? Collections.emptyIterator() : current.branches.values().iterator();
		}
		
		@Override
		public boolean hasNext()
		{
			return it.hasNext();
		}
		
		public Node<K,V> next()
		{
			return it.next();
		}
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder(50);
		this.root.print(sb,"","");
		return sb.toString();
	}
}