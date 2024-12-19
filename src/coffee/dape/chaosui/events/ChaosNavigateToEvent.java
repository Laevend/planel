package coffee.dape.chaosui.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import coffee.dape.chaosui.ChaosBuilder;

public class ChaosNavigateToEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private ChaosBuilder navFrom;
	private ChaosBuilder navTo;
	private boolean usingBackButton = false;
	private Player p;
	private boolean cancelled;
	
	public ChaosNavigateToEvent(Player navigator,ChaosBuilder navigatedFrom,ChaosBuilder navigatedTo,boolean usingBackButton)
    {
		this.p = navigator;
		this.navFrom = navigatedFrom;
		this.navTo = navigatedTo;
		this.usingBackButton = usingBackButton;
    }
	
	/**
	 * Gets the GUI the player navigated from. NULL if there was no previous GUI
	 * @return ChaosBuilder, otherwise NULL
	 */
	public ChaosBuilder getNavFrom()
	{
		return navFrom;
	}
	
	/**
	 * Gets the GUI the player is navigating to
	 * @return ChaosBuilder
	 */
	public ChaosBuilder getNavTo()
	{
		return navTo;
	}
	
	/**
	 * The player navigating
	 * @return
	 */
	public Player getWhoNavigated()
	{
		return p;
	}
	
	public boolean isUsingBackButton()
	{
		return usingBackButton;
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

	@Override
	public boolean isCancelled()
	{
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel)
	{
		this.cancelled = cancel;
	}
}
