package coffee.dape.chaosui.interfaces.paginator;

import org.bukkit.entity.Player;

import coffee.dape.chaosui.components.ChaosComponent;

public interface PaginatorPanelItem extends PaginatorItem
{
	public ChaosComponent getPanelIcon();
	public ChaosComponent getPanelButton(Player p);
}
