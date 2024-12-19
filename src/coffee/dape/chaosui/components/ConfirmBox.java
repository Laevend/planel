package coffee.dape.chaosui.components;

import org.bukkit.inventory.InventoryView;

public interface ConfirmBox
{
	public void buildGUI(InventoryView view,Runnable confirmAction,Runnable cancelAction);
}
