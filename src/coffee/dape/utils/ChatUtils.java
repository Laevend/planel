package coffee.dape.utils;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import coffee.dape.Dape;
import coffee.dape.event.ChatInputEvent;

/**
 * 
 * @author Laeven
 * Temporarily uses chat as a way of accepting text input.
 * Listen to {@linkplain coffee.dape.event.ChatInputEvent}
 */
public class ChatUtils
{
	private static ConcurrentHashMap<UUID,ChatInputSession> inputSessions = new ConcurrentHashMap<>();
	
	/**
	 * Request text input from player
	 * @param p Player to request input from
	 * @param message Message explaining what input is needed
	 * @return input session Id to check for when {@linkplain coffee.dape.event.ChatInputEvent} event fires
	 */
	public static UUID requestInput(Player p,String message)
	{
		Objects.requireNonNull(p,"Player cannot be null!");
		Objects.requireNonNull(message,"Message cannot be null!");
		
		// Remove existing session
		if(inputSessions.contains(p.getUniqueId()))
		{
			inputSessions.get(p.getUniqueId()).endSession();
		}
		
		inputSessions.put(p.getUniqueId(),new ChatInputSession(p.getUniqueId()));
		PrintUtils.info(p,message);
		return inputSessions.get(p.getUniqueId()).getInputSessionId();
	}
	
	private static class ChatInputSession implements Listener
	{
		private final UUID inputSessionId = UUID.randomUUID();
		private UUID owner;
		
		public ChatInputSession(UUID owner)
		{
			this.owner = owner;
			
			// Register this temporary listener
			Bukkit.getPluginManager().registerEvents(this,Dape.instance());
		}
		
		@EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
		public void onLeave(PlayerQuitEvent e)
		{
			if(!e.getPlayer().getUniqueId().equals(owner)) { return; }
			endSession();
		}
		
		@EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
		public void onChat(AsyncPlayerChatEvent e)
		{
			if(!e.getPlayer().getUniqueId().equals(owner)) { return; }
			e.setCancelled(true);
			
			// Call ChatInputEvent synchronously
			DelayUtils.executeDelayedTask(() ->
			{
				Bukkit.getPluginManager().callEvent(new ChatInputEvent(e.getPlayer(),e.getMessage(),this.inputSessionId,this.owner));
			});
			
			endSession();
		}
		
		public void endSession()
		{
			PlayerQuitEvent.getHandlerList().unregister(this);
			AsyncPlayerChatEvent.getHandlerList().unregister(this);
			inputSessions.remove(this.owner);
		}

		public UUID getInputSessionId()
		{
			return inputSessionId;
		}
	}
}
