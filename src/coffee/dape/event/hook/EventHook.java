package coffee.dape.event.hook;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;

import coffee.dape.utils.interfaces.Hook;

public class EventHook<T extends Event> implements Hook<T>
{
	@Override
	public boolean hookClass(Class<? extends T> type) 
	{
		try {
			Method getHandlersMethod = type.getDeclaredMethod("getHandlerList");
			HandlerList handlers = (HandlerList) getHandlersMethod.invoke(null);

			DapeListenerRedirector<T> listener = new DapeListenerRedirector<>();
			List<RegisteredListener> registeredListeners = listener.getRegisteredListeners();

			handlers.registerAll(registeredListeners);
			
			// Rebake to confirm the registration
			handlers.bake();

			// And just to make sure the hook was successful
			int hookListeners = 0;
			top: for (RegisteredListener rl : registeredListeners)
			{
				for (RegisteredListener target : handlers.getRegisteredListeners())
				{
					if (target.equals(rl))
					{
						hookListeners++;
						continue top;
					}
				}
			}

			if (hookListeners != registeredListeners.size())
			{
				throw new IllegalStateException("Failed to register an event handler for each priority for event " + type.getName() + ", expected " + registeredListeners.size() + ", found " + hookListeners);
			}
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | IllegalStateException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
