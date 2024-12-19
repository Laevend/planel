package coffee.dape.chaosui.components;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 
 * @author Laeven
 *
 */
public abstract class ChaosMultiComponent
{
	private Map<Integer,ChaosComponent> components = new HashMap<>();
	private UUID id;
	private Type type;
	
	/**
	 * Creates a Chaos multi compound that occupies 1 slot
	 * @param slots Slots this component occupies
	 * @param icon The ItemStack that will represent this element as an icon
	 */
	public ChaosMultiComponent(Type type)
	{
		this.type = type;
		this.id = UUID.randomUUID();
	}
	
	public void addComponent(ChaosComponent comp)
	{
		components.put(comp.getOccupyingSlot(),comp);
	}
	
	public enum Type
	{
		PANEL_BUTTON
	}
	
	public UUID getId()
	{
		return id;
	}

	public Map<Integer,ChaosComponent> getComponents()
	{
		return components;
	}

	public Set<Integer> getOccupyingSlots()
	{
		return components.keySet();
	}
	
	public Type getType()
	{
		return type;
	}
}