package coffee.dape.chaosui.events;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import coffee.dape.chaosui.ChaosBuilder;
import coffee.dape.chaosui.components.buttons.ToggleButton;

public class ChaosToggleButtonClickEvent extends ChaosButtonClickEvent
{
	private ToggleButton.ToggleStatus oldStatus;
	private ToggleButton.ToggleStatus newStatus;
	
	public ChaosToggleButtonClickEvent(InventoryView view,int rawSlot,ChaosBuilder builder,Player playerWhoClicked,ToggleButton.ToggleStatus oldStat,ToggleButton.ToggleStatus newStat)
    {
		super(view,rawSlot,builder,playerWhoClicked);
		this.oldStatus = oldStat;
		this.newStatus = newStat;
    }

	public ToggleButton.ToggleStatus getOldStatus()
	{
		return oldStatus;
	}

	public ToggleButton.ToggleStatus getNewStatus()
	{
		return newStatus;
	}
}
