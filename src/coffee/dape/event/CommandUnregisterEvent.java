package coffee.dape.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @author Laeven (Zack)
 * @since 0.6.0
 * 
 * Called when a command gets unregistered/removed from Vertex
 */
public class CommandUnregisterEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private String commandNameUnregistered;
	
	public CommandUnregisterEvent(String cmdName)
	{
		this.commandNameUnregistered = cmdName;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public String getCommandNameUnregistered()
	{
		return commandNameUnregistered;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}