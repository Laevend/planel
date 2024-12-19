package coffee.dape.chaosui.behaviour;

import java.util.EnumMap;
import java.util.Map;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;

import coffee.dape.chaosui.behaviour.conditional.SlotCondition;

public class Behaviours
{
	private Map<ClickType,ClickBehaviour> whitelistedClicks = new EnumMap<>(ClickType.class);
	private Map<DragType,DragBehaviour> whitelistedDrags = new EnumMap<>(DragType.class);
	
	// Click
	
	public void whitelistClickBehaviour(ClickBehaviour cb)
	{
		whitelistedClicks.put(cb.getType(),cb);
	}
	
	public void whitelistClickBehaviour(ClickType type)
	{
		whitelistedClicks.put(type,new ClickBehaviour(type));
	}
	
	public void whitelistClickBehaviour(ClickType type,SlotCondition condition)
	{
		whitelistedClicks.put(type,new ClickBehaviour(type,condition));
	}
	
	public void setWhitelistedClickBehaviours(Map<ClickType,ClickBehaviour> behaviours)
	{
		whitelistedClicks.clear();
		whitelistedClicks.putAll(behaviours);
	}
	
	public boolean hasClickBehaviour(ClickType type)
	{
		return whitelistedClicks.containsKey(type);
	}
	
	public ClickBehaviour getBehaviour(ClickType type)
	{
		return whitelistedClicks.get(type);
	}
	
	// Drag
	
	public void whitelistDragBehaviour(DragBehaviour db)
	{
		whitelistedDrags.put(db.getType(),db);
	}
	
	public void whitelistDragBehaviour(DragType type)
	{
		whitelistedDrags.put(type,new DragBehaviour(type));
	}
	
	public void whitelistDragBehaviour(DragType type,SlotCondition condition)
	{
		whitelistedDrags.put(type,new DragBehaviour(type,condition));
	}
	
	public void setWhitelistedDragBehaviours(Map<DragType,DragBehaviour> behaviours)
	{
		whitelistedDrags.clear();
		whitelistedDrags.putAll(behaviours);
	}
	
	public boolean hasDragBehaviour(DragType type)
	{
		return whitelistedDrags.containsKey(type);
	}
	
	public DragBehaviour getBehaviour(DragType type)
	{
		return whitelistedDrags.get(type);
	}
}
