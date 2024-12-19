package coffee.dape.feature.vnpc.actions;

import org.bukkit.entity.Player;

public abstract class VNpcAction
{
	private final VNpcAction.ActionType type;
	
	public VNpcAction(ActionType action)
	{
		this.type = action;
	}
	
	public abstract void execute(Player p);

	public VNpcAction.ActionType getType()
	{
		return type;
	}
	
	public enum ActionType
	{
		CHAT,
		DISPLAY_CHAT_BUTTONS,
		EXECUTE_COMMAND,
		OPEN_GUI
	}
}
