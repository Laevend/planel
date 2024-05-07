package coffee.dape.event;

import java.lang.reflect.Method;

import org.bukkit.event.Listener;

public record DapeRegisteredListener(
	Listener listener,
	Method method
) {}
