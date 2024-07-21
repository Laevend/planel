package coffee.dape.utils.clocks;

import java.util.Objects;

import org.bukkit.scheduler.BukkitTask;

/**
 * 
 * @author Laeven
 * Represents an abstract clock that can run a task repeatedly
 */
public abstract class AbstractBukkitClock 
{
	protected String clockName;
	protected int continueAttempts = 3;		// Number of attempts to re-run the clock cycle if it fails
	protected int attempts = 1;				// Attempt number of running the clock cycle
	protected long interval;					// The interval time in ticks
	protected BukkitTask clock = null;
	
	/**
	 * Creates a clock
	 * @param clockName Name of clock
	 * @param intervalInTicks Interval before 
	 */
	public AbstractBukkitClock(String clockName,long intervalInTicks)
	{
		Objects.requireNonNull(clockName,"clockName cannot be null!");
		Objects.requireNonNull(intervalInTicks,"intervalInTicks cannot be null!");
		
		if(clockName.isEmpty() || clockName.isBlank())
		{
			throw new NullPointerException("clockName cannot be empty or blank!");
		}
		
		this.clockName = clockName;
		this.interval = intervalInTicks;
	}
	
	/**
	 * Creates a clock
	 * @param clockName Name of clock
	 * @param intervalInTicks Interval before
	 * @param continueAttempts Number of attempts to restart the clock (should an exception be thrown) before it will give up
	 */
	public AbstractBukkitClock(String clockName,long intervalInTicks,int continueAttempts)
	{
		Objects.requireNonNull(clockName,"clockName cannot be null!");
		Objects.requireNonNull(intervalInTicks,"intervalInTicks cannot be null!");
		Objects.requireNonNull(continueAttempts,"continueAttempts cannot be null!");
		
		if(clockName.isEmpty() || clockName.isBlank())
		{
			throw new NullPointerException("clockName cannot be empty or blank!");
		}
		
		this.clockName = clockName;
		this.interval = intervalInTicks;
		this.continueAttempts = continueAttempts;
	}
	
	protected abstract void run();
	
	public abstract void execute() throws Exception;
	
	public void start()
	{
		if(clock != null && !clock.isCancelled()) { return; }
		run();
	}
	
	public void stop()
	{
		if(clock == null || clock.isCancelled()) { return; }		
		clock.cancel();
		clock = null;
	}
	
	public boolean isEnabled()
	{
		return clock == null ? false : clock.isCancelled() ? false : true;
	}

	public String getClockName()
	{
		return clockName;
	}

	public void setClockName(String clockName)
	{
		Objects.requireNonNull(clockName,"clockName cannot be null!");
		this.clockName = clockName;
	}

	public int getContinueAttempts()
	{
		return continueAttempts;
	}

	public void setContinueAttempts(int continueAttempts)
	{
		Objects.requireNonNull(continueAttempts,"continueAttempts cannot be null!");
		this.continueAttempts = continueAttempts;
	}

	public int getAttempts()
	{
		return attempts;
	}

	public long getInterval()
	{
		return interval;
	}

	public void setInterval(long interval)
	{
		Objects.requireNonNull(interval,"interval cannot be null!");
		this.interval = interval;
	}
}
