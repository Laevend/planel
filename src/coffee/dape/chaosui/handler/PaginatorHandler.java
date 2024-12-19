package coffee.dape.chaosui.handler;

import org.bukkit.inventory.ItemStack;

import coffee.dape.chaosui.events.ChaosClickEvent;

public interface PaginatorHandler
{	
	public void onClickPaginatorItem(ChaosClickEvent e,ItemStack paginatorItem);
}
