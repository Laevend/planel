package coffee.dape.utils.structs;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import coffee.dape.cmdparsers.astral.parser.Argument;
import coffee.dape.cmdparsers.astral.parser.AstralExecutor;
import coffee.dape.cmdparsers.astral.parser.ConVarArgument;
import coffee.dape.cmdparsers.astral.parser.PlayerVarArgument;
import coffee.dape.cmdparsers.astral.parser.StaticArgument;
import coffee.dape.cmdparsers.astral.parser.VarArgument;
import coffee.dape.utils.Logg;

/**
 * 
 * @author Laeven
 *
 */
public class CmdTree
{
	private UUID treeId;
	private Node root;
	private AstralExecutor parent;
	
	public CmdTree(AstralExecutor executor)
	{
		this.root = new Node(null,null,true);
		this.treeId = UUID.randomUUID();
		this.parent = executor;
	}
	
	public Node getRoot()
	{
		return root;
	}
	
	public UUID getTreeId()
	{
		return treeId;
	}
	
	public AstralExecutor getParent()
	{
		return parent;
	}

	public static final class Node implements Entry<String,Argument>, Iterable<Node>
	{
		public CmdTree tree;
		public Node parent;
		public Map<String,Node> branches = new HashMap<>();
		public final String key;
		public Argument value;
		private final boolean root;
		
		// Parser specific stuff
		// Set of static argument branches
		private Set<String> staticArgs = new HashSet<>();
		
		// Set of variable argument branches
		private Set<String> varArgs = new HashSet<>();
		
		// The conditional variable argument branch. Doesn't make sense to have more than 1 conditional variable argument per node
		private String conVarArg = null;
		
		// Set of variable argument branches
		private Set<String> playerVarArgs = new HashSet<>();
		
		private String pathName = null;
		private boolean endNode = false;
		
		/**
		 * Creates a new node
		 * @param parent Parent node that this node can be traversed from, can be null
		 * @param key Key used to traverse to this node from the parent
		 */
		Node(Node parent,String key)
		{
			this(parent,key,false);
		}
		
		/**
		 * Creates a new node
		 * @param parent Parent node that this node can be traversed from, can be null
		 * @param key Key used to traverse to this node from the parent
		 * @param isRoot If this node is the root node
		 */
		protected Node(Node parent,String key,boolean isRoot)
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
		public Node addBranch(String key,Argument value)
		{
			if(this.branches == null) { this.branches = new HashMap<>(); }
			new Node(this,key);
			branches.get(key).setValue(value);
			sortArgument(value);
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
		public String getKey()
		{
			return key;
		}
		
		/**
		 * Get the value of this branch node
		 */
		@Override
		public Argument getValue()
		{
			return value;
		}
		
		/**
		 * Sets the value of this branch node and returns the old value
		 */
		@Override
		public Argument setValue(Argument newValue)
		{
			Argument oldValue = this.value;
			this.value = newValue;
			return oldValue;
		}
		
		/**
		 * If this node is an end node.
		 * <p>End node is a node that is at the end of a path of arguments and has a path method to execute
		 * @return True if this node is an end node, false otherwise
		 */
		public boolean isEndNode()
		{
			return endNode;
		}
		
		public void setEndNode(boolean endNode)
		{
			this.endNode = endNode;
		}
		
		/**
		 * Gets the name of the path used to execute the path logic method.
		 * <p>This is the 'name' attribute in the \@Path annotation that is declared above
		 * each path logic method in a command.
		 * @return Path name
		 */
		public String getPathName()
		{
			return pathName;
		}
		
		public void setPathName(String pathName)
		{
			this.pathName = pathName;
		}
		
		/**
		 * Sorts argument branch into whether its a:
		 * Static Argument
		 * Variable Argument
		 * Secondary Argument
		 * @param arg Argument
		 */
		private void sortArgument(Argument arg)
		{		
			if(arg == null)
			{
				Logg.warn("Argument is null and cannot be sorted!");
				return;
			}
			
			if(arg instanceof StaticArgument staticArg)
			{
				staticArgs.add(arg.getArgumentKey());
				Logg.verb("Sorted static arg " + staticArg.getArgument(),Logg.VerbGroup.ASTRAL_PARSER);
				return;
			}
			
			if(arg instanceof VarArgument varArg)
			{
				varArgs.add(arg.getArgumentKey());
				Logg.verb("Sorted var arg " + varArg.getArgumentType().getTypeName(),Logg.VerbGroup.ASTRAL_PARSER);
				return;
			}
			
			if(arg instanceof ConVarArgument)
			{
				this.conVarArg = arg.getArgumentKey();
				Logg.verb("Sorted con var arg",Logg.VerbGroup.ASTRAL_PARSER);
				return;
			}
			
			if(arg instanceof PlayerVarArgument playerVarArg)
			{
				playerVarArgs.add(arg.getArgumentKey());
				Logg.verb("Sorted player var arg " + playerVarArg.getPlayerArgumentType().getTypeName(),Logg.VerbGroup.ASTRAL_PARSER);
				return;
			}
			
			Logg.fatal("Argument is not of static, variable, or secondary type!");
			return;
		}
		
		public Set<String> getStaticArgs()
		{
			return staticArgs;
		}

		public Set<String> getVarArgs()
		{
			return varArgs;
		}

		public String getConVarArg()
		{
			return conVarArg;
		}
		
		public Set<String> getPlayerVarArgs()
		{
			return playerVarArgs;
		}

		private void print(StringBuilder buffer,String prefix,String childrenPrefix)
		{
			buffer.append(prefix);
			buffer.append(this.key != null ? this.key.toString() : this.isRoot() ? "root" : "null" );
			buffer.append("\n");
			
			for(Iterator<CmdTree.Node> it = this.iterator(); it.hasNext();)
			{
				Node next = it.next();
				
				if(it.hasNext())
				{
					next.print(buffer,childrenPrefix + "├── ",childrenPrefix + "│   ");
				}
				else
				{
					next.print(buffer,childrenPrefix + "└── ",childrenPrefix + "    ");
				}
			}
		}
		
		@Override
		public Iterator<Node> iterator()
		{
			return new NodeIterator(this);
		}
	}
	
	private static final class NodeIterator implements Iterator<Node>
	{
		private CmdTree.Node current;
		private Iterator<Node> it;
		
		NodeIterator(CmdTree.Node node)
		{
			this.current = node;
			this.it = current.branches == null ? Collections.emptyIterator() : current.branches.values().iterator();
		}
		
		@Override
		public boolean hasNext()
		{
			return it.hasNext();
		}
		
		public Node next()
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