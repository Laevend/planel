package coffee.dape.chaosui;

import org.bukkit.inventory.InventoryView;

public class CachedGUI
{
	private InventoryView view;
	private ChaosBuilder builder;
	private int componentSlotClicked;
	
	/**
	 * Creates a new cached GUIView
	 * @param view The inventory view of the GUI
	 * @param builder The builder this view relates to
	 * @param componentSlotClicked The slot clicked that caused the view to be cached
	 */
	public CachedGUI(InventoryView view,ChaosBuilder builder,int componentSlotClicked)
	{
		this.view = view;
		this.builder = builder;
		this.componentSlotClicked = componentSlotClicked;
	}
	
	/**
	 * Gets the view
	 * @return InventoryView
	 */
	public InventoryView getView()
	{
		return view;
	}
	
	/**
	 * Gets the associating ChaosBuilder
	 * @return ChaosBuilder
	 */
	public ChaosBuilder getBuilder()
	{
		return builder;
	}
	
	/**
	 * Gets the slot clicked that caused the cached view
	 * 
	 * <p>This slot is where you would find the ChaosComponent that
	 * was clicked and thus can store data on it
	 * @return componentSlotClicked
	 */
	public int getComponentSlotClicked()
	{
		return componentSlotClicked;
	}
}
