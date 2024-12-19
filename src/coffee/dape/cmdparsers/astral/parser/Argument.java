package coffee.dape.cmdparsers.astral.parser;

import java.util.ArrayList;
import java.util.List;

import coffee.dape.cmdparsers.astral.flags.CFlag;
import coffee.dape.cmdparsers.astral.suggestions.UnconditionalSuggestionList;
import coffee.dape.utils.Logg;

/**
 * @author Laeven
 *
 * This class provides an abstract implementation for an argument.
 * All arguments have at least 1 suggestion and a branch path name for the parser to navigate to.
 *
 */
public abstract class Argument
{
	// Suggestion lists that need recompiling when requested as their values may have changed since last request
	protected List<UnconditionalSuggestionList> compileOnRequestSuggestionLists = new ArrayList<>();
	
	// A compiled list of suggestions that do not need recompiling on request 
	protected List<String> compiledSuggestions = new ArrayList<>();
	
	// A complete compiled list of all suggestions for this argument
	protected List<String> suggestions = new ArrayList<>();
	
	// If this argument is to be mapped to a parameter on the paths logic method, this is id of \@Arg
	protected String mapId = null;
	
	// List of flags to validate this argument with
	protected List<CFlag> flags;
	
	/**
	 * Compiles argument suggestions to be shown to the player.
	 * <p>pre-compiled arguments are automatically added while non-compiled suggestions are compiled at
	 * the time of request and then added.</p>
	 */
	protected void compileSuggestions()
	{		
		suggestions.clear();
		suggestions.addAll(compiledSuggestions);
		
		if(compileOnRequestSuggestionLists == null) { return; }
		
		compileOnRequestSuggestionLists.forEach(suggestion ->
		{
			suggestions.addAll(suggestion.get());
			Logg.verb("Compiling and adding suggestion list name -> " + suggestion.getSuggestionListNamespace().toString(),Logg.VerbGroup.ASTRAL_PARSER);
		});
	}
	
	public List<UnconditionalSuggestionList> getUncompiledSuggestionLists()
	{
		return compileOnRequestSuggestionLists;
	}
	
	public List<String> getCompiledSuggestions()
	{
		return compiledSuggestions;
	}
	
	/**
	 * Gets the list of suggestions for this argument
	 * @return Suggestions list
	 */
	public abstract List<String> getSuggestions();

	/**
	 * Gets the key used to access this argument from a node
	 * @return Argument Key
	 */
	public abstract String getArgumentKey();
	
	/**
	 * Gets the argument id used for mapping this argument to a parameter type in the paths logic method
	 * @return argument id
	 */
	public String getMapId()
	{
		return mapId;
	}

	protected void setMapId(String mapId)
	{
		this.mapId = mapId;
	}
	
	public List<CFlag> getFlags()
	{
		return flags;
	}
	
	protected void addFlag(CFlag flag)
	{
		if(flags == null) { flags = new ArrayList<>(); }
		flags.add(flag);
	}
}