package coffee.dape.cmdparsers.astral.parser;

import java.util.ArrayList;
import java.util.List;

import coffee.dape.cmdparsers.astral.flags.CFlag;
import coffee.dape.cmdparsers.astral.suggestions.ConditionalSuggestionList;
import coffee.dape.cmdparsers.astral.suggestions.PlayerSuggestionList;
import coffee.dape.cmdparsers.astral.suggestions.UnconditionalSuggestionList;
import coffee.dape.cmdparsers.astral.types.ArgumentType;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class ArgSet
{
	private List<Argument> argumentList = new ArrayList<>();
	private Argument arg = null;
	
	/**
	 * Creates a new static argument and adds it to the list.
	 * @param staticArgument argument
	 * @return ArgumentSet
	 */
	public ArgSet of(String staticArgument)
	{
		if(arg != null) { argumentList.add(arg); arg = null; }
		arg = new StaticArgument(staticArgument);
		return this;
	}
	
	/**
	 * Creates a new dynamic argument and adds it to the list.
	 * @param type The data type that the user inputed argument is expected to have
	 * @param if the suggestion is to be a placeholder for this argument. Examples: <\amount>, <\id>, <\message>
	 * @param suggestions list of strings that represent suggestions for input or strings that call lists of more suggestions
	 * @return ArgumentSet
	 */
	public ArgSet of(String placeholder,ArgumentType type)
	{
		if(arg != null) { argumentList.add(arg); arg = null; }
		arg = new VarArgument(placeholder,type);
		return this;
	}
	
	/**
	 * Creates a dynamic argument and adds it to the list
	 * @param placeholder Placeholder used as tab complete
	 * @param type Type of argument
	 * @param suggestions List of suggestions
	 * @return ArgumentSet
	 */
	public ArgSet of(String placeholder,ArgumentType type,UnconditionalSuggestionList suggestions)
	{
		if(arg != null) { argumentList.add(arg); arg = null; }
		arg = new VarArgument(placeholder,type,suggestions);
		return this;
	}
	
	/**
	 * Creates an argument with suggestion values dependent on
	 * a previously typed argument and adds it to the list.
	 * @param argumentIndex index of argument to lookup
	 * @param suggestions Suggestions generated from the player typed argument
	 * @return ArgumentSet
	 */
	public ArgSet of(int argumentIndex,ConditionalSuggestionList suggestions)
	{
		if(arg != null) { argumentList.add(arg); arg = null; }
		arg = new ConVarArgument(argumentIndex,suggestions);
		return this;
	}
	
	/**
	 * Creates a player dynamic argument and adds it to the list
	 * @param placeholder Placeholder used as tab complete
	 * @param type Type of argument
	 * @param suggestions List of player suggestions
	 * @return ArgumentSet
	 */
	public ArgSet of(String placeholder,ArgumentType type,PlayerSuggestionList suggestions)
	{
		if(arg != null) { argumentList.add(arg); arg = null; }
		arg = new PlayerVarArgument(placeholder,type,suggestions);
		return this;
	}
	
	/**
	 * Maps this argument to a paths logic method parameter using \@Arg(id = )
	 * @param argId Argument Id
	 * @return ArgumentSet
	 */
	public ArgSet mapTo(String argId)
	{
		arg.setMapId(argId);
		return this;
	}
	
	/**
	 * Adds an additional validation flag to this argument
	 * @param flag Command Flag
	 * @return ArgumentSet
	 */
	public ArgSet flag(CFlag flag)
	{
		arg.addFlag(flag);
		return this;
	}
	
	/**
	 * Gets the list of arguments created
	 * @return List of arguments
	 */
	protected List<Argument> getArguments()
	{
		if(arg != null) { argumentList.add(arg); arg = null; }
		return this.argumentList;
	}
}
