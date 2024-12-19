package coffee.dape.chaosui.components.buttons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import coffee.dape.chaosui.components.ChaosComponent;
import coffee.dape.chaosui.interfaces.paginator.PaginatorPanelItem;

/**
 * 
 * @author Laeven
 * Button that has an item below its occupying slot that changes the state of it
 */
public class PanelButton implements PaginatorPanelItem
{
	private ChaosComponent panelIcon;
	private ChaosComponent panelButton;
	
	public PanelButton(ChaosComponent panelIcon,ChaosComponent panelButton)
	{
		this.panelIcon = panelIcon;
		this.panelButton = panelButton;
	}

	@Override
	public ItemStack getStack()
	{
		throw new UnsupportedOperationException("Panel button has an icon and button! use 'getPanelIcon()' and 'getPanelButton()'");
	}
	
	@Override
	public boolean isItemComponentType()
	{
		return true;
	}

	@Override
	public ChaosComponent getComponent()
	{
		return panelIcon;
	}
	
	@Override
	public ChaosComponent getPanelIcon()
	{
		return panelIcon;
	}

	@Override
	public ChaosComponent getPanelButton(Player p)
	{
		return panelButton;
	}
}