package coffee.dape.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import coffee.dape.Dape;
import coffee.dape.utils.clocks.RepeatingClock;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class DelayUtils
{
	private static Map<UUID,TaskPoller> tasks = new HashMap<>();
	
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
	
	public static void submitAsync(String taskName,long pollInterval,Runnable taskLogic,Runnable logicWhenTaskDone)
	{
		TaskPoller poller = new TaskPoller(UUID.randomUUID(),taskName,pollInterval,taskLogic,logicWhenTaskDone);
		tasks.put(poller.getId(),poller);
	}
	
	public static void submitAsync(String taskName,long pollInterval,Runnable taskLogic)
	{
		TaskPoller poller = new TaskPoller(UUID.randomUUID(),taskName,pollInterval,taskLogic,null);
		tasks.put(poller.getId(),poller);
	}
	
	private static class TaskPoller extends RepeatingClock
	{
		private final UUID id;
		private ExecutorService executor = Executors.newSingleThreadExecutor();
		private Future<?> task;
		private Runnable logicWhenTaskDone;
		
		public TaskPoller(UUID uuid,String taskName,long pollInterval,Runnable taskLogic,Runnable logicWhenTaskDone)
		{
			super(taskName,pollInterval);
			this.id = uuid;
			task = executor.submit(taskLogic);
			this.logicWhenTaskDone = logicWhenTaskDone;
		}

		public UUID getId()
		{
			return id;
		}

		@Override
		public void execute() throws Exception
		{
			if(!task.isDone()) { return; }
			
			if(logicWhenTaskDone != null)
			{
				logicWhenTaskDone.run();
			}
			
			stop();
			tasks.remove(id);
		}
	}
}
