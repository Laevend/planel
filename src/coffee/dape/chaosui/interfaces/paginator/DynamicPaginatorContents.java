package coffee.dape.chaosui.interfaces.paginator;

import java.util.List;

import org.bukkit.entity.Player;

public interface DynamicPaginatorContents
{
	public List<PaginatorItem> refreshPaginator(Player p);
}
