package coffee.dape.chaosui.events;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import coffee.dape.chaosui.ChaosBuilder;
import coffee.dape.chaosui.components.buttons.ToggleButton;

public class ChaosPaginatorToggleButtonClickEvent extends ChaosButtonClickEvent
{
	private ToggleButton.ToggleStatus oldStatus;
	private ToggleButton.ToggleStatus newStatus;
	private ItemStack itemToToggle;
	
	public ChaosPaginatorToggleButtonClickEvent(InventoryView view,int rawSlot,ChaosBuilder builder,Player playerWhoClicked,ToggleButton.ToggleStatus oldStat,ToggleButton.ToggleStatus newStat,ItemStack itemToToggle)
    {
		super(view,rawSlot,builder,playerWhoClicked);
		this.oldStatus = oldStat;
		this.newStatus = newStat;
		this.itemToToggle = itemToToggle;
    }

	public ToggleButton.ToggleStatus getOldStatus()
	{
		return oldStatus;
	}

	public ToggleButton.ToggleStatus getNewStatus()
	{
		return newStatus;
	}

	public ItemStack getItemToToggle()
	{
		return itemToToggle;
	}
}
