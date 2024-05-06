package coffee.dape.utils;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import coffee.dape.Dape;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class DelayUtils
{
	/**
	 * Executes a delayed task
	 * @param runn The runnable object to execute
	 */
	public static void executeDelayedTask(Runnable runn)
	{
		executeDelayedTask(runn,1L);
	}
	
	/**
	 * Executes a delayed task
	 * @param runn The runnable object to execute
	 * @param ticksToWait Number of ticks to wait before executing this runnable object
	 * @return task id
	 */
	public static int executeDelayedTask(Runnable runn,long ticksToWait)
	{
        return Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Dape.instance(),runn,ticksToWait);
	}
	
	/**
	 * Executes a delayed bukkit task
	 * @param runn The runnable object to execute
	 * @param ticksToWait Number of ticks to wait before executing this runnable object
	 * @return BukkitTask
	 */
	public static BukkitTask executeDelayedBukkitTask(Runnable runn,long ticksToWait)
	{
        return Bukkit.getServer().getScheduler().runTaskLater(Dape.instance(),runn,ticksToWait);
	}
	
	/**
	 * Executes a task asynchronously 
	 * @param runn The runnable object to execute
	 * @return task id
	 */
	public static BukkitTask executeTaskAsynchronously(Runnable runn)
	{
		return Bukkit.getServer().getScheduler().runTaskAsynchronously(Dape.instance(), runn);
	}
	
	/**
	 * Cancels a scheduled task
	 * @param taskId
	 */
	public static void cancelTask(int taskId)
	{
		Bukkit.getServer().getScheduler().cancelTask(taskId);
	}
}
