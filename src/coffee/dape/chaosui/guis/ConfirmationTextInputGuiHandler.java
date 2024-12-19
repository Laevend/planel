package coffee.dape.chaosui.guis;

import org.bukkit.entity.Player;

import coffee.dape.chaosui.ChaosFactory;
import coffee.dape.chaosui.ChaosHandler;
import coffee.dape.chaosui.GUISession;
import coffee.dape.chaosui.events.ChaosTextInputEvent;
import coffee.dape.chaosui.handler.TextInputButtonHandler;

public class ConfirmationTextInputGuiHandler extends ChaosHandler implements TextInputButtonHandler
{
	@Override
	public void onTextInputButtonClick(ChaosTextInputEvent e)
	{
		GUISession sess = ChaosFactory.getSession((Player) e.getPlayerWhoClicked());
		String confirmPhrase = sess.getData(e.getBuilder(),"confirm_phrase").getAsString();
		
		if(e.getInput().equals(confirmPhrase))
		{
			ConfirmTextInputBox.getConfirmSession((Player) e.getPlayerWhoClicked()).getConfirmAction().run();
			return;
		}
		
		ConfirmTextInputBox.getConfirmSession((Player) e.getPlayerWhoClicked()).getCancelAction().run();
	}
}
