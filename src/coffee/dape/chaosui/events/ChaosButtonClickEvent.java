package coffee.dape.chaosui.events;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import coffee.dape.chaosui.ChaosBuilder;

public class ChaosButtonClickEvent
{
	private InventoryView view;
	private int rawSlot;
	private ChaosBuilder builder;
	private Player playerWhoClicked;
	
	public ChaosButtonClickEvent(InventoryView view,int rawSlot,ChaosBuilder builder,Player playerWhoClicked)
    {
        this.view = view;
        this.rawSlot = rawSlot;
        this.builder = builder;
        this.playerWhoClicked = playerWhoClicked;
    }
    
	public InventoryView getView()
	{
		return view;
	}	
	
	public int getRawSlot()
	{
		return rawSlot;
	}

    public ChaosBuilder getBuilder()
	{
		return builder;
	}

	public Player getPlayerWhoClicked()
	{
		return playerWhoClicked;
	}
}
