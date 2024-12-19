package coffee.dape.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import coffee.dape.utils.DelayUtils;
import coffee.dape.utils.PrintUtils;
import coffee.dape.utils.toasts.Toast.Frame;

/**
 * 
 * @author Laeven
 * TODO Delete later, this class is just for testing
 */
public class TestLis implements Listener
{
	@EventHandler
	public static void pjoin(PlayerJoinEvent e)
	{
		DelayUtils.executeDelayedBukkitTask(() ->
		{
			PrintUtils.sendToast(e.getPlayer(),"Hi there do you have time to talk about our grate lord and saviour cheesus ghrist?",Material.TOTEM_OF_UNDYING,Frame.GOAL);
			PrintUtils.sendToast(e.getPlayer(),"We're calling you today about your car's extended warranty",Material.MINECART,Frame.GOAL);
		},60L);
	}
}
