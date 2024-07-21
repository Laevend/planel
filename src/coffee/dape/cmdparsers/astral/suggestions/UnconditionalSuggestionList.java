package coffee.dape.cmdparsers.astral.suggestions;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import coffee.dape.utils.structs.Namespace;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class UnconditionalSuggestionList extends SuggestionList
{
	private boolean buildOnRequest = false;
	private Comparator<String> comp = null;
	
	/**
	 * Creates an unconditional suggestion list
	 * @param suggestionListName Name of this suggestion list
	 * @param buildOnRequest If this list should be re-built when requested by tab complete (should the list be based on elements that can change with time)
	 */
	public UnconditionalSuggestionList(Namespace suggestionListNamespace,boolean buildOnRequest)
	{
		super(suggestionListNamespace);
		this.buildOnRequest = buildOnRequest;
		rebuild();
	}
	
	/**
	 * Creates an unconditional suggestion list
	 * @param suggestionListName Name of this suggestion list
	 * @param buildOnRequest If this list should be re-built when requested by tab complete (should the list be based on elements that can change with time)
	 * @param sorter The comparator to use to sort the suggestion list
	 */
	public <T> UnconditionalSuggestionList(Namespace suggestionListNamespace,boolean buildOnRequest,Comparator<String> sorter)
	{
		super(suggestionListNamespace);
		this.buildOnRequest = buildOnRequest;
		this.comp = sorter;
		rebuild();
	}
	
	/**
	 * Contains the method for building the suggestion list using the code 
	 * injected into this method during initialising
	 * 
	 * <p>You are to override this method when creating of an instance of this class
	 * with your own logic to build the list.
	 * 
	 * <p>SuggestionList operates like an ArrayList
	 * 
	 * <p>Example:
	 * UnconditionalSuggestionList list = new UnconditionalSuggestionList("my_new_list")
	 * {
	 * 		\@Override
	 * 		public void buildList()
	 * 		{
	 * 			// BuildList code here
	 * 		}
	 * }
	 */
	public void build()
	{
		throw new UnsupportedOperationException("SuggestionList " + suggestionListNamespace.toString() + " has not correctly overidden the 'buildList()' method!");
	}
	
	/**
	 * Rebuilds the list using the method in {@link #buildList()}
	 */
	public void rebuild()
	{
		clear();
		build();
		
		if(comp != null)
		{
			Collections.sort(this,comp);
		}
	}
	
	@Override
	public List<String> get()
	{
		if(buildOnRequest) { rebuild(); }
		return this.list;
	}
	
	/**
	 * Returns a boolean on whether or not this suggestion
	 * list will re-build itselg upon request
	 * @return True if this list is re-built each
	 * time it is retrieved
	 */
	public boolean isBuildOnRequest()
	{
		return buildOnRequest;
	}
}