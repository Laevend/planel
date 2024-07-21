package coffee.dape.cmdparsers.astral.suggestions;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import coffee.dape.cmdparsers.astral.types.ArgumentType;
import coffee.dape.cmdparsers.astral.types.StringType;
import coffee.dape.utils.structs.Namespace;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class ConditionalSuggestionList extends SuggestionList
{
	// Placeholder reserved for when no other suggestions are available
	// Examples: <amount>, <id>, <message>
	private String suggestionPlaceholder = "<?>";
	
	// The expected data type of the user inputed argument
	protected ArgumentType argumentType = new StringType();
	private Comparator<String> comp = null;
	
	/**
	 * Creates an conditional suggestion list
	 * @param suggestionListName Name of this suggestion list
	 */
	public ConditionalSuggestionList(Namespace suggestionListNamespace)
	{
		super(suggestionListNamespace);
	}
	
	/**
	 * Creates an conditional suggestion list
	 * @param suggestionListName Name of this suggestion list
	 * @param sorter The comparator to use to sort the suggestion list
	 */
	public ConditionalSuggestionList(Namespace suggestionListNamespace,Comparator<String> sorter)
	{
		this(suggestionListNamespace);
		this.comp = sorter;
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
	 * ConditionalSuggestionList mySuggestionList = new ConditionalSuggestionList("my_suggestion_list",false)
	 * {
	 * 		\@Override
	 * 		public void buildList(String argument)
	 * 		{
	 * 			// BuildList code here
	 * 
	 * 			// Also depending on the condition, you may want to change the 'argumentType'
	 *			// and/or the suggestion placeholder should no suggestions be returned.
	 * 		}
	 * };
	 * 
	 * <p>Example 2:
	 * ConditionalSuggestionList mySuggestionList = new ConditionalSuggestionList("my_suggestion_list",true)
	 * {
	 * 		\@Override
	 * 		public void buildList(String argument)
	 * 		{
	 * 			// BuildList code here
	 * 			if(condition.equals("amount"))
	 * 			{
	 * 				argumentType = new IntegerType();
	 * 				suggestionPlaceholder = "\<amount\>";
	 * 				addAll(List.of(1,5,10,15));
	 * 			}
	 * 			else if(condition.equals("name"))
	 * 			{
	 * 				argumentType = new StringType();
	 * 				suggestionPlaceholder = "\<name\>";
	 * 			}
	 * 		}
	 * };
	 */
	protected void build(String argument)
	{
		throw new UnsupportedOperationException("ConditionalSuggestionList " + suggestionListNamespace.toString() + " has not correctly overidden the 'buildList(String condition)' method!");
	}
	
	/**
	 * Rebuilds the list using the method in {@link #buildList()}
	 */
	private void rebuild(String argument)
	{
		clear();
		build(argument);
		
		if(comp != null)
		{
			Collections.sort(this,comp);
		}
	}
	
	public List<String> get(String argument)
	{
		rebuild(argument);
		return this.list;
	}

	public String getSuggestionPlaceholder()
	{
		return suggestionPlaceholder;
	}

	public ArgumentType getArgumentType()
	{
		return argumentType;
	}
}