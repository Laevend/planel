package coffee.dape.event;

import org.bukkit.event.EventPriority;

public @interface EventHandler
{
	public EventPriority priority() default EventPriority.NORMAL;
	public int order() default 0;
}
