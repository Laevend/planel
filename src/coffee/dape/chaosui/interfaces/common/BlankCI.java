package coffee.dape.chaosui.interfaces.common;

import org.bukkit.inventory.InventoryView;

import coffee.dape.chaosui.ChaosBuilder;
import coffee.dape.chaosui.events.ChaosClickEvent;
import coffee.dape.chaosui.events.ChaosDragEvent;
import coffee.dape.chaosui.interfaces.ChaosInterface;

/**
 * 
 * @author Laeven
 *
 * A blank interface.
 * <p>Used when the dev will be manually handling regions and general behaviour of GUI
 */
public class BlankCI extends ChaosInterface
{

	public BlankCI()
	{
		super(Type.BLANK);
	}

	@Override
	public void init(ChaosBuilder builder)
	{
		// Must set parent
		setParent(builder);
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
