package coffee.dape.chaosui.events;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;

import coffee.dape.chaosui.ChaosBuilder;

public class ChaosCloseEvent extends InventoryCloseEvent
{    
	private ChaosBuilder builder;
	
	public ChaosCloseEvent(InventoryView view,ChaosBuilder builder)
    {
        super(view);
        this.builder = builder;
    }
    
    public ChaosBuilder getBuilder()
	{
		return builder;
	}
}
