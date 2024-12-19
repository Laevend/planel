package coffee.dape.chaosui.guis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import coffee.dape.chaosui.ChaosFactory;
import coffee.dape.chaosui.GUISession;

public class ConfirmTextInputBox
{
	private static Map<UUID,ConfirmTextInputSession> confirmSessions = new HashMap<>();
	
	public static void openConfirmBox(Player p,String confirmMessage,String confirmPhrase,Runnable confirmAction,Runnable cancelAction)
	{
		confirmSessions.put(p.getUniqueId(),new ConfirmTextInputSession(p,confirmAction,cancelAction));
		GUISession sess = ChaosFactory.getSession(p);
		
		sess.setData(ChaosFactory.getGUI(ChaosFactory.Common.CONFIRMATION_INPUT),"confirm_msg",confirmMessage);
		sess.setData(ChaosFactory.getGUI(ChaosFactory.Common.CONFIRMATION_INPUT),"confirm_phrase",confirmPhrase);
		
		ChaosFactory.open(p,ChaosFactory.Common.CONFIRMATION_INPUT);
	}
	
	public static ConfirmTextInputSession getConfirmSession(Player p)
	{
		if(!confirmSessions.containsKey(p.getUniqueId()))
		{
			return null;
		}
		
		return confirmSessions.get(p.getUniqueId());
	}
	
	static class ConfirmTextInputSession
	{
		private UUID owner;
		private Runnable confirmAction = null;
		private Runnable cancelAction = null;
		
		public ConfirmTextInputSession(Player p,Runnable confirmAction,Runnable cancelAction)
		{
			this.owner = p.getUniqueId();
			this.confirmAction = confirmAction;
			this.cancelAction = cancelAction;
		}
		
		public UUID getOwner()
		{
			return owner;
		}
		
		public Runnable getConfirmAction()
		{
			return confirmAction;
		}
		
		public Runnable getCancelAction()
		{
			return cancelAction;
		}
	}
}
