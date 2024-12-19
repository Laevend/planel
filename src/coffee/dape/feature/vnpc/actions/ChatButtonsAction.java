package coffee.dape.feature.vnpc.actions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import coffee.dape.feature.vnpc.VNpcCtrl;
import coffee.dape.utils.ChatBuilder;
import coffee.dape.utils.PrintUtils;
import coffee.dape.utils.structs.UID4;

public class ChatButtonsAction extends VNpcAction
{
	private List<VNpcChatButton> buttons = new ArrayList<>();
	
	public ChatButtonsAction()
	{
		super(VNpcAction.ActionType.CHAT);
	}
	
	public List<VNpcChatButton> getButtons()
	{
		return buttons;
	}

	public void setButtons(List<VNpcChatButton> buttons)
	{
		this.buttons = buttons;
	}

	@Override
	public void execute(Player p)
	{
		ChatBuilder builder = new ChatBuilder();
		
		for(VNpcChatButton button : buttons)
		{
			builder.addComponents(button.getButton().getResult());
		}
		
		PrintUtils.sendComp(p,builder.getResult());
		VNpcCtrl.getSession(p).nextAction();
	}
	
	public class VNpcChatButton
	{
		private final String buttonText;
		private final UID4 nextActionListId;
		private ChatBuilder builder = new ChatBuilder();
		
		public VNpcChatButton(String buttonText,UID4 nextActionListId)
		{
			this.buttonText = buttonText;
			this.nextActionListId = nextActionListId;
			
			// TODO Implement /internal command.
			// The idea is to temporarily give the player permission for the command as soon as its executed and then immediately revoke permission.
			int REMOVE_ME_WHEN_DONE;
			
			builder.setMessage("&8[&r" + buttonText + "&8]&r")
				.setHoverShowTextEvent("&eSelect Option")
				.setClickRunCommandEvent("/internal setVillagerBehaviourKey " + nextActionListId.toString())
				.nextComp();
			builder.setMessage(" ").nextComp();
		}
		
		public String getButtonText()
		{
			return buttonText;
		}

		public UID4 getNextActionListId()
		{
			return nextActionListId;
		}

		public ChatBuilder getButton()
		{
			return builder;
		}
	}
}
