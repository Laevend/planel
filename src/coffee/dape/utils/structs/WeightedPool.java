package coffee.dape.utils.structs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Laeven
 * @since 1.0.0
 * 
 * A collection of elements are added to this pool. The order is nondeterministic.
 * Initially, every element has a weight of 1.
 * every time {@link #next()} is called, an element will be selected from the pool.
 * Elements with larger weights are more likely to be selected.
 * If an element succeeds in being selected, its weight will be set back to 1.
 * 
 * All weights are stacked into a bar and a random number is generated from the size of this bar.
 * +==========+========================+===========+
 * |  0 - 20  |        21 - 200        | 201 - 211 | ... etc.
 * +==========+========================+===========+ 
 * 
 */
public class WeightedPool<T>
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
	
	Random rand = new Random();
	
	/**
	 * Creates a weighted pool with auto compile disabled
	 * 
	 * <p>When the pool is modified in any way, the range map must
	 * be recompiled so it takes into account the new modified list of elements.
	 * Recompiling is o(n) in time complexity.
	 */
	public WeightedPool()
	{
		this(false);
	}
	
	/**
	 * Creates a weighted pool
	 * 
	 * <p>When the pool is modified in any way, the range map must
	 * be recompiled so it takes into account the new modified list of elements.
	 * Recompiling is o(n) in time complexity.
	 * @param autoRecompile Whether this pool should auto recompile the range map when the list of elements in the pool changes
	 */
	public WeightedPool(boolean autoRecompile)
	{
		this.autoRecompile = autoRecompile;
	}
	
	/**
	 * Creates a weighted pool with auto compile disabled
	 * 
	 * <p>When the pool is modified in any way, the range map must
	 * be recompiled so it takes into account the new modified list of elements.
	 * Recompiling is o(n) in time complexity.
	 * @param el Collection of elements
	 */
	public WeightedPool(Collection<T> el)
	{
		this(el,false);
	}
	
	/**
	 * Creates a weighted pool
	 * 
	 * <p>When the pool is modified in any way, the range map must
	 * be recompiled so it takes into account the new modified list of elements.
	 * Recompiling is o(n) in time complexity.
	 * @param el Collection of elements
	 * @param autoRecompile Whether this pool should auto recompile the range map when the list of elements in the pool changes
	 */
	public WeightedPool(Collection<T> el,boolean autoRecompile)
	{		
		el.forEach(v ->
		{
			this.elements.add(new Pair<>(1,v));
		});
		
		this.autoRecompile = autoRecompile;
		
		compileRangeMap();
	}
	
	/**
	 * Add an element to the pool
	 * @param ele Element to add
	 */
	public void add(T ele)
	{
		this.elements.add(new Pair<>(1,ele));
		if(this.autoRecompile) { compileRangeMap(); }
	}
	
	/**
	 * Add an element to the pool with a predetermined weight value
	 * @param ele Element to add
	 * @param weight Predetermined weight
	 */
	public void add(T ele,int weight)
	{
		this.elements.add(new Pair<>(1,ele));
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
	public List<Pair<Integer,T>> getPoolElements()
	{
		return new ArrayList<>(this.elements);
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
		
		incrementAllWeights();
		
		this.elements.get(nextElementIndex).setValueA(1);
		
		compileRangeMap();
		
		return nextElement;
	}
	
	private void incrementAllWeights()
	{
		this.elements.forEach(v ->
		{
			v.setValueA(v.getValueA() + 1);
		});
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
