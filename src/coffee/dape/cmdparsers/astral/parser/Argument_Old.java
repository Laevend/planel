package coffee.dape.cmdparsers.astral.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import coffee.dape.cmdparsers.astral.suggestions.ConditionalSuggestionList;
import coffee.dape.cmdparsers.astral.suggestions.UnconditionalSuggestionList;
import coffee.dape.utils.Logg;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class Argument_Old
{
	// Variable to define the argument as a static or dynamic type
	private boolean isDynamic = false;
	
	// Variable to define the argument as dependent
	// Dependent argument populates suggestions based on another already typed argument
	private boolean isDependant = false;
	
	// If this argument has isDependant = true then this holds the index of the
	// argument in the argument array to lookup in the argMap
	private int argumentIndexToLookup = -1;
	
	// If this argument has isDepndable = true then this holds a list of conditional suggestions
	// generated using a previous argument that the user typed in chat.
	// This argument is accessed via its index using argumentIndexToLookup
	private ConditionalSuggestionList conditionalSuggestion;
	
	// Either a static or dynamic argument that will be typed by the player.
	// Static arg examples: hop, set, add, create
	// Dynamic arg examples: STRING, BOOLEAN, INTEGER
	private String argumentText;
	
	// Placeholder which is reserved for tab completion generation
	// Examples: <amount>, <id>, <message>
	private String placeholder;
	private boolean isPlaceholder = false;
	
	// The type of dynamic argument (Should this argument be dynamic)
	// if 'argument' should be a dynamic argument, this will hold that argument class type
	private Class<?> type;
	
	// Individual suggestion lists
	private List<UnconditionalSuggestionList> listOfSuggestionLists;
	
	// all individual suggestions lists compiled into one list
	private List<String> allSuggestions;
	
	public enum Type
	{
		STRING,
		INTEGER,
		LONG,
		BOOLEAN,
		DOUBLE,
		FLOAT,
		ANY
	}
	
	/**
	 * Creates a new static argument
	 * @param staticArgument argument
	 */
	protected Argument_Old(String staticArgument)
	{
		this.argumentText = staticArgument;
		this.listOfSuggestionLists = new ArrayList<>();
//		this.listOfSuggestionLists.add(new UnconditionalSuggestionList(staticArgument)
//		{
//		    @Override
//		    public void buildList()
//		    {
//		    	add(staticArgument);
//		    }
//		});
	}
	
	/**
	 * Creates a new dynamic argument
	 * @param type The data type that the user inputed argument is expected to have
	 * @param if the suggestion is to be a placeholder for this argument. Examples: <\amount>, <\id>, <\message>
	 * @param suggestions list of strings that represent suggestions for input or strings that call lists of more suggestions
	 */
	protected Argument_Old(String placeholder,Type type)
	{
		this.isDynamic = true;
		this.isPlaceholder = true;
		this.argumentText = type.toString().toUpperCase();
		
		switch(type)
		{
			case STRING: { this.type = String.class; break; }
			case INTEGER: { this.type = Integer.class; break; }
			case LONG: { this.type = Long.class; break; }
			case BOOLEAN: { this.type = Boolean.class; break; }
			case DOUBLE: { this.type = Double.class; break; }
			case FLOAT: { this.type = Float.class; break; }
			case ANY: { this.type = String.class; break; }
		}
		
		this.listOfSuggestionLists = new ArrayList<>();
//		this.listOfSuggestionLists.add(new UnconditionalSuggestionList(placeholder)
//		{
//		    @Override
//		    public void buildList()
//		    {
//		    	add(placeholder);
//		    }
//		});
	}
	
	/**
	 * Creates a new dynamic argument
	 * @param type The data type that the user inputed argument is expected to have
	 * @param if the suggestion is to be a placeholder for this argument. Examples: <\amount>, <\id>, <\message>
	 * @param suggestions list of strings that represent suggestions for input or strings that call lists of more suggestions
	 */
	protected Argument_Old(String placeholder,Type type,UnconditionalSuggestionList... suggestions)
	{
		this.isDynamic = true;
		this.isPlaceholder = false;
		this.argumentText = type.toString().toUpperCase();
		
		switch(type)
		{
			case STRING: { this.type = String.class; break; }
			case INTEGER: { this.type = Integer.class; break; }
			case LONG: { this.type = Long.class; break; }
			case BOOLEAN: { this.type = Boolean.class; break; }
			case DOUBLE: { this.type = Double.class; break; }
			case FLOAT: { this.type = Float.class; break; }
			case ANY: { this.type = String.class; break; }
		}
		
		this.listOfSuggestionLists = new ArrayList<>();		
		Arrays.stream(suggestions).forEach(v ->
		{
			this.listOfSuggestionLists.add(v);
		});
	}
	
	/**
	 * Creates an argument with suggestion values dependent on
	 * a previously typed argument.
	 * @param argumentIndex index of argument to lookup
	 * @param list The conditional suggestion list to retrieve using the variable typed at the argument index
	 */
	protected Argument_Old(int argumentIndex,ConditionalSuggestionList list)
	{
		this.isDependant = true;
		this.argumentIndexToLookup = argumentIndex;
		this.conditionalSuggestion = list;
		this.argumentText = "CONDITIONAL_ANY_" + list.getSuggestionListNamespace().toString().toUpperCase();
		// Need better solution to this in Parser v4
		//this.argumentText = UUID.randomUUID().toString();
	}
	
	private void compileSuggestions()
	{		
		if(this.allSuggestions == null) { this.allSuggestions = new ArrayList<>(); }
		
		this.allSuggestions.clear();
		
		this.listOfSuggestionLists.forEach(v ->
    	{
    		//if(v.isBuildOnRequest()) { v.rebuildList(); }
    		this.allSuggestions.addAll(v.get());
    		Logg.verb("Suggestion list name -> " + v.getSuggestionListNamespace());
    	});
	}
	
	public List<UnconditionalSuggestionList> getSuggestionsList()
	{
		return listOfSuggestionLists;
	}
	
	public List<String> getSuggestions()
	{
		compileSuggestions();
		return allSuggestions;
	}
	
	public ConditionalSuggestionList getConditionalSuggestion()
	{
		return conditionalSuggestion;
	}

	public String getPlaceholder()
	{
		return placeholder;
	}

	public String getArgumentText()
	{
		return argumentText;
	}

	public Class<?> getType()
	{
		return type;
	}

	public boolean isDynamic()
	{
		return isDynamic;
	}

	public boolean isPlaceholder()
	{
		return isPlaceholder;
	}

	public boolean isDependant()
	{
		return isDependant;
	}

	public int getArgumentIndexToLookup()
	{
		return argumentIndexToLookup;
	}
}