package coffee.dape.chaosui.interfaces.paginator;

import org.bukkit.inventory.ItemStack;

import coffee.dape.chaosui.components.ChaosComponent;

public interface PaginatorItem
{
	public ItemStack getStack();
	
	public boolean isItemComponentType();
	
	public ChaosComponent getComponent();
}
