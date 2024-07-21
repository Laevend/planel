package coffee.dape.cmdparsers.astral.parser;

import java.util.List;

import coffee.dape.cmdparsers.astral.suggestions.ConditionalSuggestionList;
import coffee.dape.cmdparsers.astral.types.ArgumentType;

/**
 * @author Laeven
 *
 * This class provides implementation for conditional variable arguments.
 * 
 * <p>Conditional variable arguments are like variable arguments except their suggestions, expected type, and 
 * suggestion placeholder can change based on arguments that the player has previously typed before executing the command.
 * 
 * <p>A player may type an argument for 'attribute' and then the argument after would show different suggestions
 * based on the attribute argument before it.
 */
public class ConVarArgument extends Argument
{
	// This holds a conditional suggestion that accepts an argument as an input to
	// vary the suggestions compiled and shown to the player
	// The argument input used is accessed via its index using argumentIndexToLookup
	private ConditionalSuggestionList conditionalSuggestion;
	
	// Placeholder reserved for when no other conditional suggestions are available
	// Examples: <amount>, <id>, <message>
	private String conditionalPlaceholder;
	
	// The expected data type of the user inputed argument
	private ArgumentType conditionalArgumentType;
	
	// If this argument has isDependant = true then this holds the index of the
	// argument in the argument array to lookup in the argMap
	private int argumentIndexToLookup = -1;
	
	/**
	 * Creates a SecondaryArgument with suggestion values dependent on
	 * a previously typed argument.
	 * @param argumentIndex index of argument to lookup
	 * @param list The conditional suggestion list to retrieve using the variable typed at the argument index
	 */
	protected ConVarArgument(int argumentIndex,ConditionalSuggestionList list)
	{
		this.argumentIndexToLookup = argumentIndex;
		this.conditionalSuggestion = list;
	}
	
	public ConditionalSuggestionList getConditionalSuggestion()
	{
		return conditionalSuggestion;
	}
	
	/**
	 * Gets the argument index used to acquire an argument from the argument array
	 * @return Gets the argument index
	 */
	public int getArgumentIndexToLookup()
	{
		return argumentIndexToLookup;
	}
	
	public String getConditionalPlaceholder()
	{
		return conditionalPlaceholder;
	}

	public ArgumentType getConditionalArgumentType()
	{
		return conditionalArgumentType;
	}
	
	/**
	 * Returns suggestions based on a condition.
	 * A previously typed argument is used as input
	 * @param args Array of player typed arguments
	 * @return Conditional suggestions
	 */
	public List<String> getSuggestions(String[] args)
	{
		suggestions.clear();
		suggestions.addAll(conditionalSuggestion.get(args[argumentIndexToLookup]));
		conditionalArgumentType = conditionalSuggestion.getArgumentType();
		conditionalPlaceholder = conditionalSuggestion.getSuggestionPlaceholder();
		
		if(suggestions.isEmpty()) { return List.of(conditionalPlaceholder); }
		
		return suggestions;
	}
	
	/**
	 * Not used in conditional variable argument
	 */
	@Override
	public List<String> getSuggestions()
	{
		throw new UnsupportedOperationException("Secondary argument needs input to compile suggestions. Use method 'getSuggestions(String argument)");
	}

	@Override
	public String getArgumentKey()
	{
		return "CON_VAR_ARG";
	}
}