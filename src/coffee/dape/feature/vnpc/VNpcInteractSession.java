package coffee.dape.feature.vnpc;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.bukkit.Bukkit;

import com.google.gson.JsonObject;

import coffee.dape.feature.vnpc.actions.ChatAction;
import coffee.dape.feature.vnpc.actions.VNpcAction;
import coffee.dape.utils.Logg;
import coffee.dape.utils.PlayerUtils;
import coffee.dape.utils.PrintUtils;
import coffee.dape.utils.VectorUtils;
import coffee.dape.utils.structs.UID4;

public class VNpcInteractSession
{
	private UUID sessionOwner;
	private long sessionCreationTime;
	private long duration;
	private UID4 currentVNpcId = null;
	private UID4 currentActionListId = null;
	private int currentActionListIndex = 0;
	private JsonObject data;
	private SessionTerminator sessionTerminator;
	private ChatAction.ChatClock chatClock;
	private UUID pendingChatButtonBehaviourExecution;
	
	public VNpcInteractSession(UUID owner,UID4 vNpcId,long duration)
	{
		this.sessionOwner = owner;
		this.currentVNpcId = vNpcId;
		this.duration = duration;
		this.sessionCreationTime = System.currentTimeMillis();
		this.sessionTerminator = new SessionTerminator(duration);
		this.sessionTerminator.start();
	}
	
	public void resetDuration()
	{
		if(this.sessionTerminator == null || !this.sessionTerminator.isAlive()) { return; }
		
		this.sessionTerminator.reset();
	}
	
	public UUID getSessionOwner()
	{
		return sessionOwner;
	}

	public long getSessionCreationTime()
	{
		return sessionCreationTime;
	}

	public long getDuration()
	{
		return duration;
	}
	
	public UID4 getCurrentVNpcId()
	{
		return currentVNpcId;
	}

	public void setCurrentVNpcId(UID4 currentVNpcId)
	{
		this.currentVNpcId = currentVNpcId;
	}

	public UID4 getCurrentActionListId()
	{
		return currentActionListId;
	}

	public void setCurrentActionListId(UID4 currentActionListId)
	{
		this.currentActionListId = currentActionListId;
		this.currentActionListIndex = 0;
	}

	public UUID getPendingChatButtonExecution()
	{
		return pendingChatButtonBehaviourExecution;
	}

	public void setPendingChatButtonExecution(UUID pendingChatButtonBehaviourExecution)
	{
		this.pendingChatButtonBehaviourExecution = pendingChatButtonBehaviourExecution;
	}
	
	public boolean hasPendingChatButtonExecution()
	{
		return this.pendingChatButtonBehaviourExecution != null;
	}

	public void nextAction()
	{
		this.currentActionListIndex++;
	}
	
	public VNpcAction getCurrentAction()
	{
		// Check if Npc exists at all
		if(!VNpcCtrl.contains(currentVNpcId)) { Logg.error("VillagerNpc session for " + PlayerUtils.getName(sessionOwner) + " could not get next behaviour for last interacted villager! Villager is null"); return null; }
		
		VNpc npc = VNpcCtrl.getVNpc(currentVNpcId);
		
		// If this sessions current action list id is null, set it to default
		if(this.currentActionListId == null) { this.currentActionListId = npc.getDefaultListId(); }
		
		// Check the action id exists on the npc
		if(!npc.getActions().containsKey(this.currentActionListId))
		{
			Logg.warn("VillagerNpc session for " + PlayerUtils.getName(sessionOwner) + " could not get next action list for last interacted villager! Action is null");
			
			// set to default if cannot find current action list
			this.currentActionListId = npc.getDefaultListId();
		}
		
		ArrayList<VNpcAction> actions = VNpcCtrl.getVNpc(currentVNpcId).getActions().get(this.currentActionListId);
		
		// Check the action index is within bounds of the array
		if(this.currentActionListIndex < 0 || this.currentActionListIndex >= actions.size())
		{
			Logg.error("VillagerNpc session for " + PlayerUtils.getName(sessionOwner) + " could not get next action in action list " + currentVNpcId.toString() + " as index was outside bounds! (" + this.currentActionListIndex + ")");
			this.currentActionListIndex = 0;
			this.currentActionListId = npc.getDefaultListId();
		}
		
		return actions.get(this.currentActionListIndex);
	}
	
	public JsonObject getData()
	{
		resetDuration();
		if(this.data == null) { this.data = new JsonObject(); }
		
		return data;
	}
	
	public void clearData()
	{
		this.data = null;
	}
	
	public void endTalkClock()
	{
		if(chatClock != null && chatClock.isEnabled())
		{
			chatClock.stop();
		}
	}
	
	public ChatAction.ChatClock getTalkClock()
	{
		return chatClock;
	}

	public void setTalkClock(ChatAction.ChatClock talkClock)
	{
		this.chatClock = talkClock;
	}
	
	private class SessionTerminator extends Thread
	{
		private AtomicLong durationLeft;
		private long maxDuration;
		
		public SessionTerminator(long duration)
		{
			this.durationLeft = new AtomicLong(duration);
			this.maxDuration = duration;
		}
		
		public void reset()
		{
			this.durationLeft.set(this.maxDuration);
		}
		
		@Override
		public void run()
		{
			while(durationLeft.get() > 0)
			{
				durationLeft.decrementAndGet();
				
				if(VectorUtils.getDirectionBetweenLocations(VNpcCtrl.getVNpc(currentVNpcId).getEntity().getLocation(),Bukkit.getPlayer(sessionOwner).getLocation()).length() > 5.0d)
				{
					VNpcCtrl.removeSession(sessionOwner);
					PrintUtils.info(PlayerUtils.getPlayer(sessionOwner),"The conversation ended as you walked too far away.");
					return;
				}
				
				try
				{
					Thread.sleep(1000);
				} 
				catch (InterruptedException e)
				{
					Logg.error("VillagerNpcSessionTerminator thread for " + sessionOwner.toString() + " failed to sleep",e);
				}
			}
			
			VNpcCtrl.removeSession(sessionOwner);
			PrintUtils.info(PlayerUtils.getPlayer(sessionOwner),"The conversation ended as you didn't interact.");
		}
	}
}
