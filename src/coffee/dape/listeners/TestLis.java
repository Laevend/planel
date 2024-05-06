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
			PrintUtils.sendToast(e.getPlayer(),"Hello I am Toast",Material.COPPER_BULB,Frame.TASK);
		},60L);
	}
}
