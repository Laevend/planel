package coffee.dape.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import coffee.dape.cmdparsers.astral.parser.AstralExecutor;

/**
 * @author Laeven (Zack)
 * @since 0.6.0
 * 
 * Called when a new command gets registered to Vertex
 */
public class CommandRegisterEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private AstralExecutor commandRegistered;
	
	public CommandRegisterEvent(AstralExecutor cmd)
	{
		this.commandRegistered = cmd;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public AstralExecutor getCommandRegistered()
	{
		return commandRegistered;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}