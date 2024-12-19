package coffee.dape.chaosui.interfaces.common;

import org.bukkit.inventory.InventoryView;

import coffee.dape.chaosui.ChaosBuilder;
import coffee.dape.chaosui.components.ChaosRegion;
import coffee.dape.chaosui.events.ChaosClickEvent;
import coffee.dape.chaosui.events.ChaosDragEvent;
import coffee.dape.chaosui.interfaces.ChaosInterface;

/**
 * 
 * @author Laeven
 *
 * The default interface used by a ChaosGUI if no interface is supplied
 */
public class DefaultCI extends ChaosInterface
{

	public DefaultCI()
	{
		super(Type.DEFAULT);
	}

	@Override
	public void init(ChaosBuilder builder)
	{
		// Must set parent
		setParent(builder);
		builder.defineHeaderRegion(ChaosRegion.Common.HEADER);
		builder.defineBodyRegion(ChaosRegion.Common.BODY);
		builder.defineFooterRegion(ChaosRegion.Common.FOOTER);
	}

	@Override
	public void buildInterface(InventoryView view)
	{
		// N/A
	}

	@Override
	public void handleClickEvent(ChaosClickEvent e)
	{
		// N/A
	}

	@Override
	public void handleDragEvent(ChaosDragEvent e)
	{
		// N/A
	}
}
