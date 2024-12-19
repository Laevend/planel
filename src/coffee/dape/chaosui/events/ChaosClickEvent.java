package coffee.dape.chaosui.events;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;

import coffee.dape.chaosui.ChaosBuilder;
import coffee.dape.chaosui.components.ChaosRegion;

public class ChaosClickEvent extends InventoryClickEvent
{    
	private ChaosBuilder builder;
	private ChaosRegion region;
	
	public ChaosClickEvent(InventoryView view,SlotType type,int slot,ClickType click,InventoryAction action,ChaosBuilder builder)
    {
        super(view,type,slot,click,action);
        this.builder = builder;
    }

    public ChaosClickEvent(InventoryView view,SlotType type,int slot,ClickType click,InventoryAction action,int key,ChaosBuilder builder)
    {
    	super(view,type,slot,click,action,key);
    	this.builder = builder;
    }
    
	public ChaosClickEvent(InventoryView view,SlotType type,int slot,ClickType click,InventoryAction action,ChaosBuilder builder,ChaosRegion region)
    {
        this(view,type,slot,click,action,builder);
        this.region = region;
    }

    public ChaosClickEvent(InventoryView view,SlotType type,int slot,ClickType click,InventoryAction action,int key,ChaosBuilder builder,ChaosRegion region)
    {
    	this(view,type,slot,click,action,key,builder);
    	this.region = region;
    }
    
    public ChaosBuilder getBuilder()
	{
		return builder;
	}

	public ChaosRegion getRegion()
	{
		return region;
	}
}
