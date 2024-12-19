package coffee.dape.chaosui.components.buttons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import coffee.dape.chaosui.ChaosFactory;
import coffee.dape.chaosui.GUISession;
import coffee.dape.chaosui.components.ChaosComponent;
import coffee.dape.chaosui.events.ChaosClickEvent;
import coffee.dape.chaosui.listeners.ChaosActionListener;
import coffee.dape.utils.HeadUtils;
import coffee.dape.utils.ItemBuilder;
import coffee.dape.utils.Logg;
import coffee.dape.utils.SoundUtils;

/**
 * 
 * @author Laeven
 *
 */
public class BackButton extends ChaosComponent
{
	/**
	 * Creates a back navigation button to navigate back to the previous GUI
	 * the player was in.
	 * 
	 * <p>Should this fail either because the GUI was not navigated to and was directly opened,
	 * Chaos will instead navigate to a fallback default GUI
	 * @param defaultGuiToNavigateTo The default GUI to navigate back to should no previous GUI be found
	 */
	public BackButton(String defaultGuiToNavigateTo)
	{
		this(defaultGuiToNavigateTo,0);
	}
	
	/**
	 * Creates a back navigation button to navigate back to the previous GUI
	 * the player was in.
	 * 
	 * <p>Should this fail either because the GUI was not navigated to and was directly opened,
	 * Chaos will instead navigate to a fallback default GUI
	 * @param defaultGuiToNavigateTo The default GUI to navigate back to should no previous GUI be found
	 * @param slot The slot that this button should occupy
	 */
	public BackButton(String defaultGuiToNavigateTo,int slot)
	{
		super(slot,ItemBuilder.of(HeadUtils.LEFT_ARROW.clone()).name("&c<- Back").create(),Type.BUTTON);
		this.setSound(SoundUtils.HEAVY_WOOD_BANGING);
		this.addActionListener(new ChaosActionListener()
		{
			public void onClick(ChaosClickEvent e)
			{
				GUISession session = ChaosFactory.getSession((Player) e.getWhoClicked());
				
				if(!session.hasPreviousGUI(e.getBuilder()))
				{
					if(!ChaosFactory.isGUI(defaultGuiToNavigateTo))
					{
						SoundUtils.playErrorSound((Player) e.getWhoClicked());
						Logg.error(e.getWhoClicked().getName() + " attempted to navigate back to a GUI from GUI '" + e.getBuilder().getName() + "' but GUI '" + defaultGuiToNavigateTo + "' does not exist!");
						return;
					}
					
					session.setNavigatingUsingBackButton(true);
					ChaosFactory.open((Player) e.getWhoClicked(),defaultGuiToNavigateTo);
					return;
				}
				
				if(!ChaosFactory.isGUI(session.getPreviousGUI(e.getBuilder())))
				{
					Logg.warn(e.getWhoClicked().getName() + " attempted to navigate back dynamically to a GUI from GUI '" + e.getBuilder().getName() + "' but GUI '" + session.getPreviousGUI(e.getBuilder()) + "' does not exist!");
					
					if(!ChaosFactory.isGUI(defaultGuiToNavigateTo))
					{
						SoundUtils.playErrorSound((Player) e.getWhoClicked());
						Logg.error(e.getWhoClicked().getName() + " attempted to navigate back to a GUI from GUI '" + e.getBuilder().getName() + "' but GUI '" + defaultGuiToNavigateTo + "' does not exist!");
						return;
					}
					
					session.setNavigatingUsingBackButton(true);
					ChaosFactory.open((Player) e.getWhoClicked(),defaultGuiToNavigateTo);
					return;
				}
				
				session.setNavigatingUsingBackButton(true);
				ChaosFactory.open((Player) e.getWhoClicked(),session.getPreviousGUI(e.getBuilder()));
			}
		});
	}

	@Override
	public ItemStack getStack()
	{
		return getAppearance();
	}
	
	@Override
	public boolean isItemComponentType()
	{
		return true;
	}

	@Override
	public ChaosComponent getComponent()
	{
		return this;
	}
}