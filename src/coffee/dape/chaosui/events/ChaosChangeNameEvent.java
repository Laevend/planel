package coffee.dape.chaosui.events;

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
public class ChaosChangeNameEvent implements Cancellable
{    
	private Player player;
	private int slot;
	private ChaosBuilder builder;
	private ItemStack stack;
	private String oldName;
	private String newName;
	private boolean cancelled;
	
	public ChaosChangeNameEvent(InventoryView view,ChaosBuilder builder,ItemStack stack,String oldName,String newName,Player player,int slot)
    {
        this.builder = builder;
        this.stack = stack;
        this.oldName = oldName;
        this.newName = newName;
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

	public String getOldName()
	{
		return oldName;
	}

	public String getNewName()
	{
		return newName;
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
