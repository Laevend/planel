package coffee.dape.cmdparsers.astral.parser;

import java.util.List;

import coffee.dape.cmdparsers.astral.suggestions.UnconditionalSuggestionList;
import coffee.dape.cmdparsers.astral.types.ArgumentType;

/**
 * @author Laeven
 *
 * This class provides implementation for variable arguments.
 * 
 * <p>Variable arguments can be any value provided it complies with the type and flags imposed on it.
 * 
 * <p>By default they have 1 suggestion to display which is represented as the placeholder for the 
 * argument. * E.g '\<amount\>', '\<name\>', '\<player\>' etc.
 * 
 * <p>This placeholder is used in the event that there are no other suggestions that can be used.
 * The the case of '\<name\>' we might not know what names to suggest. So the suggestion just remains as '\<name\>'.
 * 
 * <p>However, for a suggestion like '\<player\>' we can replace this placeholder with suggestions of
 * players that are currently online.
 */
public class VarArgument extends Argument
{
	// Placeholder reserved for when no other suggestions are available
	// Examples: <amount>, <id>, <message>
	private String suggestionPlaceholder;
	
	// The expected data type of the user inputed argument
	private ArgumentType argumentType;
	
	/**
	 * Creates a new variable argument
	 * @param suggestionPlaceholder A suggestion placeholder for when no suggestions are available
	 * @param type The data type that the user inputed argument is expected to have
	 */
	protected VarArgument(String suggestionPlaceholder,ArgumentType type)
	{
		this.suggestionPlaceholder = suggestionPlaceholder;
		this.argumentType = type;
	}
	
	/**
	 * Creates a new variable argument
	 * @param suggestionPlaceholder A suggestion placeholder for when no suggestions are available
	 * @param type The data type that the user inputed argument is expected to have
	 * @param optional If this argument is optional
	 * @param suggestions list of strings that represent suggestions for input or strings that call lists of more suggestions
	 */
	protected VarArgument(String suggestionPlaceholder,ArgumentType type,UnconditionalSuggestionList... suggestions)
	{
		this(suggestionPlaceholder,type);
		
		for(var suggestionList : suggestions)
		{
			if(!suggestionList.isBuildOnRequest())
			{
				// Collect suggestions that don't need recompiling to reduce
				// unnecessary isBuildOnRequest() checking on suggestion requesting
				compiledSuggestions.addAll(suggestionList.get());
			}
			else
			{
				compileOnRequestSuggestionLists.add(suggestionList);
			}
		}
	}
	
	public String getSuggestionPlaceholder()
	{
		return suggestionPlaceholder;
	}

	public ArgumentType getArgumentType()
	{
		return argumentType;
	}

	@Override
	public List<String> getSuggestions()
	{
		compileSuggestions();
		
		if(suggestions.isEmpty()) { return List.of(suggestionPlaceholder); }
		
		return suggestions;
	}

	@Override
	public String getArgumentKey()
	{
		return argumentType.getTypeName();
	}
}