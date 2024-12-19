package coffee.dape.chaosui.events;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import coffee.dape.chaosui.ChaosBuilder;
import coffee.dape.chaosui.components.ChaosRegion;

public class ChaosDepositEvent extends ChaosClickEvent
{
	private ItemStack itemToBeDeposited;
	
	public ChaosDepositEvent(InventoryView view,SlotType type,int slot,ClickType click,InventoryAction action,ChaosBuilder builder,ChaosRegion region)
    {
        super(view,type,slot,click,action,builder,region);
    }

	public ItemStack getItemToBeDeposited()
	{
		return itemToBeDeposited;
	}
}
