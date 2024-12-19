package coffee.dape.chaosui.events;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import coffee.dape.chaosui.ChaosBuilder;

public class ChaosChoiceButtonClickEvent extends ChaosButtonClickEvent
{
	private int newChoiceIndex;
	private int oldChoiceIndex;
	private ItemStack choiceButton;
	
	public ChaosChoiceButtonClickEvent(InventoryView view,int rawSlot,ChaosBuilder builder,Player playerWhoClicked,int oldChoiceIndex,int newChoiceIndex,ItemStack choiceButton)
    {
		super(view,rawSlot,builder,playerWhoClicked);
		this.oldChoiceIndex = oldChoiceIndex;
        this.newChoiceIndex = newChoiceIndex;
        this.choiceButton = choiceButton;
    }
	
	public int getOldChoiceIndex()
	{
		return oldChoiceIndex;
	}

	public int getNewChoiceIndex()
	{
		return newChoiceIndex;
	}

	public ItemStack getChoiceButton()
	{
		return choiceButton;
	}
}
