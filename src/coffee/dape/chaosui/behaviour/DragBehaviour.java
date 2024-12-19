package coffee.dape.chaosui.behaviour;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.DragType;

import coffee.dape.chaosui.behaviour.conditional.SlotCondition;

public class DragBehaviour
{
	private boolean conditional = false;
	private SlotCondition condition = null;
	private DragType type;
	
	/**
	 * Create an unconditional drag behaviour to whitelist
	 * @param type DragType
	 */
	public DragBehaviour(DragType type)
	{
		this.type = type;
	}
	
	/**
	 * Create a conditional drag behaviour to whitelist
	 * @param type DragType
	 * @param condition Condition to allow this drag behaviour to proceed
	 */
	public DragBehaviour(DragType type,SlotCondition condition)
	{
		this.type = type;
		this.condition = condition;
		this.conditional = true;
	}
	
	/**
	 * Validates the condition to allow this drag type to proceed
	 * @param whoDragged Player who dragged
	 * @return True if condition is met or no condition exists, false otherwise
	 */
	public boolean validateCondition(Player whoDragged)
	{
		if(!conditional) { return true; }
		return condition.onCondition(whoDragged);
	}

	/**
	 * If this drag behaviour is conditional
	 * @return True if slot behaviour is conditional, false otherwise
	 */
	public boolean isConditional()
	{
		return conditional;
	}
	
	/**
	 * Gets slot condition
	 * @return Slot condition
	 */
	public SlotCondition getCondition()
	{
		return condition;
	}
	
	/**
	 * Gets drag type
	 * @return Drag type
	 */
	public DragType getType()
	{
		return type;
	}
}
