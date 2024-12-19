package coffee.dape.utils.structs;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import coffee.dape.utils.Logg;

/**
 * @author Laeven
 * @since 1.0.0
 * 
 * A collection of elements are added to this pool. The order is nondeterministic.
 * every time {@link #next()} is called, an element will be selected from the pool.
 * Elements with larger weights are more likely to be selected.
 * This pool is static meaning the weights do not change once chosen
 * 
 * All weights are stacked into a bar and a random number is generated from the size of this bar.
 * +==========+========================+===========+
 * |  0 - 20  |        21 - 200        | 201 - 211 | ... etc.
 * +==========+========================+===========+ 
 * 
 */
public class StaticWeightedPool<T>
{
	// List<Pair<Weight,Element>>
	// Weight - The weight of this element
	// Element - The element
	private List<Pair<Integer,T>> elements = new ArrayList<>();
	
	// NavMap<StackedRange,ElementListIndex>
	// StackedRange is the lower bound number of the range, example as above 0, 21, and 201 are the lower bounds of the range.
	// ElementListIndex is the index of the element in the element array
	private NavigableMap<Integer,Integer> rangeMap = new TreeMap<>();
	
	// The total weight of all elements combined.
	private int totalWeight;
	private boolean autoRecompile = false;
	
	/**
	 * Creates a weighted pool
	 * 
	 * <p>When the pool is modified in any way, the range map must
	 * be recompiled so it takes into account the new modified list of elements.
	 * Recompiling is o(n) in time complexity.
	 */
	public StaticWeightedPool() {}
	
	/**
	 * Creates a weighted pool
	 * 
	 * <p>When the pool is modified in any way, the range map must
	 * be recompiled so it takes into account the new modified list of elements.
	 * Recompiling is o(n) in time complexity.
	 */
	public StaticWeightedPool(List<T> elements,List<Integer> weights)
	{
		if(elements.size() != weights.size())
		{
			Logg.error("Element list and Weight list are not the same size!");
			return;
		}
		
		for(int i = 0; i < elements.size(); i++)
		{
			this.elements.add(new Pair<>(weights.get(i),elements.get(i)));
		}
		
		compileRangeMap();
	}
	
	/**
	 * Add an element to the pool with a predetermined weight value
	 * @param ele Element to add
	 * @param weight Predetermined weight
	 */
	public void add(T ele,int weight)
	{
		this.elements.add(new Pair<>(weight,ele));
		if(this.autoRecompile) { compileRangeMap(); }
	}
	
	/**
	 * Remove an element from the pool
	 * @param index Index where this element exists in the pool
	 */
	public void remove(int index)
	{
		if(index < 0 || index >= elements.size()) { return; }
		
		this.elements.remove(index);
		if(this.autoRecompile) { compileRangeMap(); }
	}
	
	/**
	 * Returns a copy of the contents of the pool
	 * @return Pool elements
	 */
	public List<Pair<Integer,T>> getPoolElementsAndWeights()
	{
		return new ArrayList<>(this.elements);
	}
	
	/**
	 * Returns a copy of the contents of the pool
	 * @return Pool elements
	 */
	public List<T> getPoolElements()
	{
		List<T> ele = new ArrayList<>();
		
		for(int i = 0; i < this.elements.size(); i++)
		{
			ele.add(this.elements.get(i).getValueB());
		}
		
		return ele;
	}
	
	/**
	 * Size of the pool
	 * @return size of pool
	 */
	public int size()
	{
		return this.elements.size();
	}
	
	/**
	 * Clears the pool
	 */
	public void clear()
	{
		this.elements.clear();
		this.rangeMap.clear();
	}
	
	/**
	 * Grabs the next chosen element from the pool
	 * @return Next random element from pool
	 */
	public T next()
	{
		int rangeIndex = rangeMap.floorKey((int) ThreadLocalRandom.current().nextInt(0,this.totalWeight + 1));
		int nextElementIndex = rangeMap.get(rangeIndex);
		T nextElement = this.elements.get(nextElementIndex).getValueB();
		
		return nextElement;
	}
	
	public void compileRangeMap()
	{
		int lowerRangeBound = 0;
		rangeMap.clear();
		
		for(int i = 0; i < this.elements.size(); i++)
		{
			rangeMap.put(lowerRangeBound,i);
			lowerRangeBound += this.elements.get(i).getValueA();
		}
		
		this.totalWeight = lowerRangeBound;
	}
}
