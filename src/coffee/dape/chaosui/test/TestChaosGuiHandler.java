package coffee.dape.chaosui.test;

import coffee.dape.chaosui.ChaosHandler;
import coffee.dape.chaosui.events.ChaosChoiceButtonClickEvent;
import coffee.dape.chaosui.events.ChaosClickEvent;
import coffee.dape.chaosui.events.ChaosTextInputEvent;
import coffee.dape.chaosui.handler.ChoiceButtonHandler;
import coffee.dape.chaosui.handler.TextInputButtonHandler;

public class TestChaosGuiHandler extends ChaosHandler implements TextInputButtonHandler, ChoiceButtonHandler
{
	public void boatButtonClick(ChaosClickEvent e)
	{
		
	}

	@Override
	public void onChoiceButtonClick(ChaosChoiceButtonClickEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextInputButtonClick(ChaosTextInputEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}
