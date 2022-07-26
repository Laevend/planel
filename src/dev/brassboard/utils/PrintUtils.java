package dev.brassboard.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import dev.brassboard.Brassboard;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class PrintUtils
{	
	/**
	 * Prints a string to the console
	 * @param s String
	 */
	public static void println(String s)
	{
		Bukkit.getServer().getConsoleSender().sendMessage(toColour(Brassboard.prefix + " " + s));
	}
	
	/**
	 * Returns a colour formatted string
	 * @param s String of text
	 * @return The formatted string
	 */
	public static String toColour(String s)
	{
		// Return a string with colour codes formated
		return ChatColor.translateAlternateColorCodes('&',s);
	}
	
	/**
	 * Sends a message to the player using a command sender
	 * @param sender The player
	 * @param message The message to send
	 */
	public static void sendMsg(CommandSender sender,String message)
	{
		if(sender instanceof Player)
		{
			sendMsg((Player) sender,message);
		}
		else
		{
			sendMsg((ConsoleCommandSender) sender,message);
		}
	}
	
	/**
	 * Sends a message to the player
	 * @param player The player
	 * @param message The message to send
	 */
	public static void sendMsg(Player player,String message)
	{
		player.sendMessage(toColour(Brassboard.prefix + "&r&7 " + message));
	}
	
	/**
	 * Sends a message to the player
	 * @param player The player
	 * @param message The message to send
	 */
	public static void sendMsg(ConsoleCommandSender console,String message)
	{
		console.sendMessage(toColour(Brassboard.prefix + "&r&7 " + message));
	}
}