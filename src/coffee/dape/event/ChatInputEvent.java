package coffee.dape.event;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import coffee.dape.utils.ChatUtils;

/**
 * @author Laeven (Zack)
 * @since 0.6.0
 * 
 * Called when chat input is accepted
 * @see ChatUtils {@link coffee.dape.utils.ChatUtils#requestInput(Player, String) requestInputMethod} to request input via chat
 */
public class ChatInputEvent extends PlayerEvent
{
	private static final HandlerList handlers = new HandlerList();
	private String input;
	private UUID inputSessionId;
	private UUID sessionOwner;
	
	public ChatInputEvent(Player who,String input,UUID inputSessionId,UUID sessionOwner)
	{
		super(who);
		this.input = input;
		this.inputSessionId = inputSessionId;
		this.sessionOwner = sessionOwner;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public String getInput()
	{
		return input;
	}

	public UUID getInputSessionId()
	{
		return inputSessionId;
	}
	
	public UUID getSessionOwner()
	{
		return sessionOwner;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}