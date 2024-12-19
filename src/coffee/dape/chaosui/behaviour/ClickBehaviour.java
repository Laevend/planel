package coffee.dape.chaosui.behaviour;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import coffee.dape.chaosui.behaviour.conditional.SlotCondition;

public class ClickBehaviour
{
	private boolean conditional = false;
	private SlotCondition condition = null;
	private ClickType type;
	
	/**
	 * Create an unconditional click behaviour to whitelist
	 * @param type ClickType
	 */
	public ClickBehaviour(ClickType type)
	{
		this.type = type;
	}
	
	/**
	 * Create a conditional click behaviour to whitelist
	 * @param type ClickType
	 * @param condition Condition to allow this click behaviour to proceed
	 */
	public ClickBehaviour(ClickType type,SlotCondition condition)
	{
		this.type = type;
		this.condition = condition;
		this.conditional = true;
	}
	
	/**
	 * Validates the condition to allow this click type to proceed
	 * @param whoClicked Player who clicked
	 * @return True if condition is met or no condition exists, false otherwise
	 */
	public boolean validateCondition(Player whoClicked)
	{
		if(!conditional) { return true; }
		return condition.onCondition(whoClicked);
	}

	/**
	 * If this click behaviour is conditional
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
	 * Gets click type
	 * @return Click type
	 */
	public ClickType getType()
	{
		return type;
	}
}
