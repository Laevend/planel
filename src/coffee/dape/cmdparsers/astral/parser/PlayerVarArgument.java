package coffee.dape.cmdparsers.astral.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import coffee.dape.cmdparsers.astral.suggestions.PlayerSuggestionList;
import coffee.dape.cmdparsers.astral.types.ArgumentType;

/**
 * @author Laeven
 *
 * This class provides implementation for player variable arguments.
 * 
 * <p>Player variable arguments can be any value provided it complies with the type and flags imposed on it.
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
public class PlayerVarArgument extends Argument
{
	// This holds all player variable suggestions
	private List<PlayerSuggestionList> playerSuggestionLists = new ArrayList<>();
	
	// Placeholder reserved for when no other suggestions are available
	// Examples: <amount>, <id>, <message>
	private String suggestionPlaceholder;
	
	// The expected data type of the user inputed argument
	private ArgumentType argumentType;
	
	/**
	 * Creates a new player variable argument
	 * @param suggestionPlaceholder A suggestion placeholder for when no suggestions are available
	 * @param type The data type that the user inputed argument is expected to have
	 */
	protected PlayerVarArgument(String suggestionPlaceholder,ArgumentType type)
	{
		this.suggestionPlaceholder = suggestionPlaceholder;
		this.argumentType = type;
	}
	
	/**
	 * Creates a new player variable argument
	 * @param suggestionPlaceholder A suggestion placeholder for when no suggestions are available
	 * @param type The data type that the user inputed argument is expected to have
	 * @param optional If this argument is optional
	 * @param suggestions list of strings that represent suggestions for input or strings that call lists of more suggestions
	 */
	protected PlayerVarArgument(String suggestionPlaceholder,ArgumentType type,PlayerSuggestionList... suggestions)
	{
		this(suggestionPlaceholder,type);
		Arrays.stream(suggestions).forEach(v -> playerSuggestionLists.add(v));
	}
	
	public String getPlayerSuggestionPlaceholder()
	{
		return suggestionPlaceholder;
	}

	public ArgumentType getPlayerArgumentType()
	{
		return argumentType;
	}
	
	/**
	 * Returns suggestions based on a player.
	 * A previously typed argument is used as input
	 * @param player The player used to generate suggestions
	 * @return Player suggestions
	 */
	public List<String> getSuggestions(Player player)
	{
		suggestions.clear();
		
		for(PlayerSuggestionList list : playerSuggestionLists)
		{
			suggestions.addAll(list.get(player));
		}
		
		if(suggestions.isEmpty()) { return List.of(suggestionPlaceholder); }
		
		return suggestions;
	}

	@Override
	public List<String> getSuggestions()
	{
		throw new UnsupportedOperationException("Secondary argument (player) needs input to compile suggestions. Use method 'getSuggestions(String argument)");
	}

	@Override
	public String getArgumentKey()
	{
		return argumentType.getTypeName();
	}
}