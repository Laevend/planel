package coffee.dape.chaosui.guis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import coffee.dape.chaosui.ChaosFactory;
import coffee.dape.chaosui.GUISession;

public class ConfirmBox
{
	private static Map<UUID,ConfirmSession> confirmSessions = new HashMap<>();
	
	public static void openConfirmBox(Player p,String confirmQuestion,Runnable confirmAction,Runnable cancelAction)
	{
		confirmSessions.put(p.getUniqueId(),new ConfirmSession(p,confirmAction,cancelAction));
		GUISession sess = ChaosFactory.getSession(p);
		
		sess.setData(ChaosFactory.getGUI(ChaosFactory.Common.CONFIRMATION),"confirm_question",confirmQuestion);
		
		ChaosFactory.open(p,ChaosFactory.Common.CONFIRMATION);
	}
	
	public static void openConfirmBox(Player p,String confirmQuestion,ItemStack item,Runnable confirmAction,Runnable cancelAction)
	{
		confirmSessions.put(p.getUniqueId(),new ConfirmSession(p,confirmAction,cancelAction));
		GUISession sess = ChaosFactory.getSession(p);
		
		sess.setData(ChaosFactory.getGUI(ChaosFactory.Common.CONFIRMATION),"confirm_question",confirmQuestion);
		sess.setGlobalData("confirm_item",item);
		
		ChaosFactory.open(p,ChaosFactory.Common.CONFIRMATION);
	}
	
	public static ConfirmSession getConfirmSession(Player p)
	{
		if(!confirmSessions.containsKey(p.getUniqueId()))
		{
			return null;
		}
		
		return confirmSessions.get(p.getUniqueId());
	}
	
	static class ConfirmSession
	{
		private UUID owner;
		private Runnable confirmAction = null;
		private Runnable cancelAction = null;
		
		public ConfirmSession(Player p,Runnable confirmAction,Runnable cancelAction)
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
