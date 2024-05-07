package coffee.dape.event.hook;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.RegisteredListener;

import coffee.dape.Dape;

/**
 * Hook class that gets injected into each type of Event, to redirect execution to Dape's listener handlers.
 *
 * @author Khyonie
 */
public class DapeListenerRedirector<T extends Event> implements Listener
{
	/** Lowest priority handler. */
	@EventHandler(priority = EventPriority.LOWEST)
	public void lowestPriority(T event) 
	{
		redirect(event, EventPriority.LOWEST);
	}

	/** Low priority handler. */
	@EventHandler(priority = EventPriority.LOW)
	public void lowPriority(T event) 
	{
		redirect(event, EventPriority.LOW);
	}

	/** Normal priority handler. */
	@EventHandler
	public void normalPriority(T event) 
	{
		redirect(event, EventPriority.NORMAL);
	}

	/** High priority handler. */
	@EventHandler(priority = EventPriority.HIGH)
	public void highPriority(T event) 
	{
		redirect(event, EventPriority.HIGH);
	}

	/** Highest priority handler. */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void highestPriority(T event) 
	{
		redirect(event, EventPriority.HIGHEST);
	}

	/** Monitor priority handler. */
	@EventHandler(priority = EventPriority.MONITOR)
	public void monitorPriority(T event) 
	{
		redirect(event, EventPriority.MONITOR);
	}

	/**
	 * Gets a map of event priorities and methods associated with them.
	 */
	public List<RegisteredListener> getRegisteredListeners()
	{
		List<RegisteredListener> data = new ArrayList<>();

		for (Method method : this.getClass().getMethods())
		{
			if (!method.isAnnotationPresent(EventHandler.class))
			{
				continue;
			}

			// Anonymous event executor, to grab the target method easily
			EventExecutor executor = new EventExecutor() 
			{
				@Override
				public void execute(Listener listener, Event event) throws EventException 
				{
					try {
						method.invoke(listener, event);
					} catch (InvocationTargetException | IllegalAccessException e) {
						// TODO Flesh this out a bit more
						throw new EventException(e);
					}
				}
			};

			// Create the registered listener
			EventPriority priority = method.getAnnotation(EventHandler.class).priority();
			RegisteredListener listener = new RegisteredListener(this, executor, priority, Dape.instance(), true);
			data.add(listener);
		}

		return data;
	}

	private void redirect(
		T event, 
		EventPriority priority
	) {
		// TODO Redirect to a listener manager
	}
}
