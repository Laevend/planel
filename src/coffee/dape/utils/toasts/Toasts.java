package coffee.dape.utils.toasts;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import coffee.dape.Dape;
import coffee.dape.utils.DelayUtils;
import coffee.dape.utils.toasts.Toast.Frame;

/**
 * @author Laeven
 * @since 1.0.0
 * 
 * Toasts are little UI elements that appear at the top right of a players screen.
 * They usually appear in vanilla when a player meets the criteria for an advancement.
 * This class allows custom toasts to be sent to a player or many players.
 * Custom toasts only need to be registered once per server restart.
 * Custom toasts will be forgotten by the server after a restart and will need to be re-added.
 * 
 * Use the {@link #register(String, String, Material, Frame)} method to register a new toast
 * then call
 */
public class Toasts
{
	private static Map<String,Toast> toasts = new HashMap<>();
	
	/**
	 * Registers a toast to be later displayed to a player
	 * @param toastName Name of this toast e.g transactions/chatgradients/bluelagoon
	 * @param text The text to display on the toast
	 * @param icon The material icon to display on the toast
	 * @param frame 
	 */
	public static void register(String toastName,String text,Material icon,Frame frame)
	{
		Toast t = new Toast(toastName,text,icon,frame);
		t.add();
		toasts.put(toastName,t);
	}
	
	@SuppressWarnings("deprecation")
	public static void unregister(String toastName)
	{
		if(toasts.containsKey(toastName))
		{
			toasts.get(toastName).remove();
			toasts.remove(toastName);
		}
		else
		{
			// In the event a toast exists in the server but not in the map.
			Bukkit.getUnsafe().removeAdvancement(new NamespacedKey(Dape.instance(),toastName));
			Bukkit.getServer().reloadData();
		}
	}
	
	public static void unregisterAll()
	{
		toasts.forEach((k,v) ->
		{
			v.remove();
		});
		
		toasts.clear();
	}
	
	public static void sendToast(String toastName,Player player)
	{
		sendToast(toastName,Arrays.asList(player));
	}
	
	public static void sendToast(String toastName,Collection<? extends Player> players)
	{
		if(!toasts.containsKey(toastName)) { return; }
		
		toasts.get(toastName).grant(players);
		
		DelayUtils.executeDelayedTask(() ->
		{
			toasts.get(toastName).revoke(players);
		},5);
	}
}
