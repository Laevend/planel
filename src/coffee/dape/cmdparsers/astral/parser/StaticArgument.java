package coffee.dape.cmdparsers.astral.parser;

import java.util.List;

import coffee.dape.cmdparsers.astral.flags.CFlag;

/**
 * @author Laeven
 *
 * This class provides implementation for static arguments.
 * 
 * <p>Static arguments are by their very nature 'static'. They have 1 suggestion to show which is
 * the exact argument that the player must type to continue down the path this argument leads to.</p>
 * 
 * <p>Examples of static arguments are hop, set, add, create
 */
public class StaticArgument extends Argument
{
	// The static argument that will be typed by the player.
	private String staticArgument;
	
	/**
	 * Creates a new static argument
	 * @param staticArgument argument
	 */
	protected StaticArgument(String staticArgument)
	{
		this.staticArgument = staticArgument;
		compiledSuggestions.add(this.staticArgument);
	}

	public String getArgument()
	{
		return staticArgument;
	}

	@Override
	public List<String> getSuggestions()
	{
		compileSuggestions();
		return suggestions;
	}

	@Override
	public String getArgumentKey()
	{
		return staticArgument;
	}
	
	@Override
	protected void setMapId(String mapId)
	{
		throw new UnsupportedOperationException("Static arguments cannot be mapped! Static Argument: " + staticArgument);
	}
	
	@Override
	protected void addFlag(CFlag flag)
	{
		throw new UnsupportedOperationException("Static arguments cannot have flags! Static Argument: " + staticArgument);
	}
}