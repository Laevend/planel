package coffee.dape.chaosui.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import coffee.dape.chaosui.ChaosBuilder;

/**
 * 
 * @author Laeven
 * 
 * Event fired when a name tag with a custom display name is being
 * used on a paginator item
 *
 */
public class ChaosChangeIconEvent implements Cancellable
{    
	private Player player;
	private int slot;
	private ChaosBuilder builder;
	private ItemStack stack;
	private Material oldIconType;
	private Material newIconType;
	private boolean cancelled;
	
	public ChaosChangeIconEvent(InventoryView view,ChaosBuilder builder,ItemStack stack,Material oldIconType,Material newIconType,Player player,int slot)
    {
        this.builder = builder;
        this.stack = stack;
        this.oldIconType = oldIconType;
        this.newIconType = newIconType;
        this.player = player;
        this.slot = slot;
    }
    
    public ChaosBuilder getBuilder()
	{
		return builder;
	}

	public ItemStack getStack()
	{
		return stack;
	}

	public Material getOldIconType()
	{
		return oldIconType;
	}

	public Material getNewIconType()
	{
		return newIconType;
	}

	public Player getWhoClicked()
	{
		return player;
	}

	public int getRawSlot()
	{
		return slot;
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
