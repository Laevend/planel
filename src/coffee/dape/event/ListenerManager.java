package coffee.dape.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import coffee.dape.config.Configurable;
import coffee.dape.utils.Logg;
import coffee.khyonieheart.lilac.Lilac;
import coffee.khyonieheart.lilac.TomlConfiguration;
import coffee.khyonieheart.lilac.value.TomlObject;

public class ListenerManager implements Configurable
{
	private static Map<Class<? extends Event>, List<DapeRegisteredListener>[]> registeredListeners = new HashMap<>();

	private static TomlConfiguration defaultConfiguration = Lilac.newConfiguration()
		.addComment(" Configuration for Dape event handling")
		.addTable("event_handling")
		.addBoolean("event_handling.autohook", true)
		.setInlineComment(" Set to false to disable Dape event handling and auto-hooking.")
		.finish();

	public static void callEvent(Event event, EventPriority priority) 
	{
		if (!registeredListeners.containsKey(event.getClass()))
		{
			return;
		}

		for (DapeRegisteredListener handler : registeredListeners.get(event.getClass())[priority.ordinal()])
		{
			try {
				handler.method().invoke(handler.listener(), event);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void register(Listener listener)
	{
		Objects.requireNonNull(listener);

		try {
			List<Method> handlerMethods = getValidHandlers(listener);

			for (Method method : handlerMethods)
			{
				DapeRegisteredListener registeredListener = new DapeRegisteredListener(listener, method);

				if (!registeredListeners.containsKey(method.getParameterTypes()[0]))
				{
					List<DapeRegisteredListener>[] priorities = new List[EventPriority.values().length];
					for (EventPriority p : EventPriority.values())
					{
						priorities[p.ordinal()] = new ArrayList<>();
					}
				}

				EventHandler handlerInfo = method.getAnnotation(EventHandler.class);
				List<DapeRegisteredListener> handlers = registeredListeners.get(method.getParameterTypes()[0])[handlerInfo.priority().ordinal()];

				handlers.add(handlerInfo.order(), registeredListener); // Inserting at the given index ensures that the list will always be sorted by order
			}
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("Failed to register listener " + listener.getClass().getName(), e);
		}
	}

	private static List<Method> getValidHandlers(Listener listener) throws IllegalArgumentException
	{
		List<Method> methods = new ArrayList<>();

		processMethod(listener.getClass().getMethods(), methods);
		processMethod(listener.getClass().getDeclaredMethods(), methods);

		return methods;
	}

	private static void processMethod(Method[] classMethods, List<Method> validMethods) throws IllegalArgumentException
	{
		for (Method m : classMethods)
		{
			if (!m.isAnnotationPresent(EventHandler.class))
			{
				continue;
			}

			// Probably a touch pedantic, but filter methods that aren't a void type
			if (!m.getReturnType().equals(Void.TYPE))
			{
				continue;
			}

			EventHandler handlerInfo = m.getAnnotation(EventHandler.class);
			if (handlerInfo.order() < 0)
			{
				throw new IllegalArgumentException("Event order cannot be negative for handler " + m.getName());
			}

			if (m.getParameterCount() != 1)
			{
				throw new IllegalArgumentException("Event handler method " + m.getName() + " must have only one parameter");
			}

			Class<?> eventParameter = m.getParameterTypes()[0];
			if (!Event.class.isAssignableFrom(eventParameter))
			{
				throw new IllegalArgumentException("Invalid event type " + eventParameter.getName() + " in event handler " + m.getName());
			}

			validMethods.add(m);
		}
	}

	@Override
	public Map<String, TomlObject<?>> getDefaults() 
	{
		Logg.info("Config: " + Lilac.tomlParser().getEncoder().encode(defaultConfiguration, Lilac.tomlParser().setPreserveComments(true)));
		return defaultConfiguration.getBacking();
	}
}
