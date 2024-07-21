package coffee.dape.utils.structs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Laeven
 *
 */
public class VTree<T,L>
{
	private T head;
	private L leaf;
	private VTree<T,L> parent = null;
	private Map<T,VTree<T,L>> branchMap = new HashMap<>();
	
	/**
	 * Creates a new Tree
	 * @param head Head of the tree
	 */
	public VTree(T head)
	{
		this.head = head;
	}
	
	/**
	 * Creates a new Tree
	 * @param head Head of the tree
	 * @param parent the parent tree this tree resides in
	 */
	public VTree(T head,VTree<T,L> parent)
	{
		this.head = head;
		this.parent = parent;
	}
	
	/**
	 * Adds a branch to this tree
	 * Branches can lead to other branches
	 * @param branchName Name of the branch
	 * @return Tree created by this new branch
	 */
	public VTree<T,L> addBranch(T branchName)
	{
		VTree<T,L> newBranch = new VTree<T,L>(branchName,this);
		this.branchMap.put(branchName,newBranch);
		return getBranch(branchName);
	}
	
	/**
	 * Gets a branch from this tree
	 * @param branchName Name of the branch
	 * @return Tree that this branch links to
	 */
	public VTree<T,L> getBranch(T branchName)
	{
		return this.branchMap.get(branchName);
	}
	
	/**
	 * Sets the head value of this tree
	 * @param head Head of this tree
	 */
	public void setHead(T head)
	{
		this.head = head;
	}
	
	/**
	 * Gets the head value of this tree
	 * @return Head of this tree
	 */
	public T getHead()
	{
		return head;
	}
	
	/**
	 * Sets the lead of this tree
	 * @param leaf The leaf of the tree
	 */
	public void setLeaf(L leaf)
	{
		this.leaf = leaf;
	}
	
	/**
	 * Gets the leaf of this tree
	 * @return Leaf of this tree
	 */
	public L getLeaf()
	{
		return leaf;
	}
	
	/**
	 * Checks if this tree has a leaf attached to it
	 * @return true/false
	 */
	public boolean hasLeaf()
	{
		return leaf != null ? true : false;
	}
	
	/**
	 * Sets the tree parent of this tree
	 * @param parent Tree
	 */
	public void setParent(VTree<T,L> parent)
	{
		this.parent = parent;
	}
	
	/**
	 * Gets the tree parent of this tree
	 * @return Tree parent
	 */
	public VTree<T,L> getParent()
	{
		return parent;
	}
	
	/**
	 * Gets the available tree branches that are
	 * attached to this tree
	 * @return Map of branches
	 */
	public Map<T,VTree<T,L>> getBranches()
	{
		return this.branchMap;
	}
	
	/**
	 * Gets the head value of all branches attached
	 * to this tree
	 * @return Collection of branch heads
	 */
	public Collection<T> getBranchesHeads()
	{
		Collection<T> branchHeads = new ArrayList<>();
		
		for(VTree<T,L> branch : this.branchMap.values())
		{
			branchHeads.add(branch.getHead());
		}
		
		return branchHeads;
	}
	
	/**
	 * Gets the lead values of all branches attached
	 * to this tree
	 * @return Collection of branch leafs
	 */
	public Collection<L> getBranchesLeafs()
	{
		Collection<L> branchLeafs = new ArrayList<>();
		
		for(VTree<T,L> branch : this.branchMap.values())
		{
			if(!branch.hasLeaf()) { continue; }
			
			branchLeafs.add(branch.getLeaf());
		}
		
		return branchLeafs;
	}
	
	/**
	 * Checks of a branch exists
	 * @param branchHead the value that represents the head of the branch
	 * @return true/false
	 */
	public boolean containsBranch(T branchHead)
	{
		return this.branchMap.containsKey(branchHead);
	}
	
	/**
	 * Get the number of branches attached to this tree
	 * @return Number of branches
	 */
	public int getNumberOfBranches()
	{
		return this.branchMap.size();
	}

	@Override
	public String toString()
	{
		return printTree(0);
	}
	
	private static int indent = 2;
	
	/**
	 * Prints this tree and all nested branches and leafs inside
	 * @param increment number of space indentations per tree scope
	 * @return String of entire tree
	 */
	public String printTree(int increment)
	{
		String s = "";
		String inc = "";
		
		for (int i = 0; i < increment; ++i) 
		{
			inc = inc + " ";
		}
		
		s = inc + head + ", Leaf{ " + leaf + " }";
		
		for (VTree<T,L> branch : this.branchMap.values())
		{
			s += "\n" + branch.printTree(increment + indent);
		}
		
		return s;
	}
}