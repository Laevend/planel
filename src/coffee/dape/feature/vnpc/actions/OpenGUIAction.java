package coffee.dape.feature.vnpc.actions;

import org.bukkit.entity.Player;

import coffee.dape.feature.vnpc.VNpcCtrl;

public class OpenGUIAction extends VNpcAction
{
	private String guiName;
	
	public OpenGUIAction()
	{
		super(VNpcAction.ActionType.OPEN_GUI);
	}
	
	public String getGuiName()
	{
		return guiName;
	}

	public void setGuiName(String guiName)
	{
		this.guiName = guiName;
	}

	@Override
	public void execute(Player p)
	{
		// TODO Add chaos GUI
		// ChaosFactory.open(p,guiName);
		VNpcCtrl.getSession(p).nextAction();
	}
}
