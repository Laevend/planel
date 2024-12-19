package coffee.dape.chaosui.events;

import java.util.Map;

import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import coffee.dape.chaosui.ChaosBuilder;
import coffee.dape.chaosui.components.ChaosRegion;

public class ChaosDragEvent extends InventoryDragEvent
{    
	private ChaosBuilder builder;
	private Map<String,ChaosRegion> regions = null;
	
	public ChaosDragEvent(InventoryView view,ItemStack newCursor,ItemStack oldCursor,boolean right,Map<Integer,ItemStack> slots,ChaosBuilder builder,Map<String,ChaosRegion> regions)
    {
        super(view,newCursor,oldCursor,right,slots);
        this.builder = builder;
    }
    
	public ChaosBuilder getBuilder()
	{
		return builder;
	}
	
	public Map<String, ChaosRegion> getRegions()
	{
		return regions;
	}
}
