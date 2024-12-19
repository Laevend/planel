package coffee.dape.chaosui.events;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;

import coffee.dape.chaosui.ChaosBuilder;

/**
 * 
 * @author Laeven
 * 
 * Unlike {@link ChaosCloseEvent}, TrueClose is when the player is no longer in a GUI.
 * <p>This is called when the player has closed a GUI instead of closing and navigating to another GUI
 */
public class ChaosTrueCloseEvent extends InventoryCloseEvent
{    
	private ChaosBuilder builder;
	
	public ChaosTrueCloseEvent(InventoryView view,ChaosBuilder builder)
    {
        super(view);
        this.builder = builder;
    }
    
    public ChaosBuilder getBuilder()
	{
		return builder;
	}
}
