package coffee.dape.chaosui.slots;

import coffee.dape.chaosui.components.ChaosComponent;

public class ChaosSlot
{
	private int rawSlot;
	private ChaosComponent slotComponent = null;
	
	public ChaosSlot(int rawSlot)
	{
		this.rawSlot = rawSlot;
	}

	public int getRawSlot()
	{
		return rawSlot;
	}
	
	public boolean isOccupied()
	{
		return slotComponent != null;
	}

	public void setSlotComponent(ChaosComponent slotElement)
	{
		this.slotComponent = slotElement;
	}

	public ChaosComponent getSlotComponent()
	{
		return slotComponent;
	}
}
