package coffee.dape.chaosui.events;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import coffee.dape.chaosui.ChaosBuilder;

public class ChaosTextInputEvent extends ChaosButtonClickEvent
{
	private String input;
	
	public ChaosTextInputEvent(InventoryView view,int rawSlot,ChaosBuilder builder,Player playerWhoClicked,String input)
    {
		super(view,rawSlot,builder,playerWhoClicked);
        this.input = input;
    }

	public String getInput()
	{
		return input;
	}
}
