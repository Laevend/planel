package coffee.dape.chaosui.test;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.InventoryView;

import coffee.dape.chaosui.ChaosBuilder;
import coffee.dape.chaosui.ChaosDecor.DecorType;
import coffee.dape.chaosui.ChaosFactory.InvTemplate;
import coffee.dape.chaosui.anno.ChaosGUI;
import coffee.dape.chaosui.components.buttons.Button;
import coffee.dape.chaosui.components.buttons.ChoiceButton;
import coffee.dape.chaosui.components.buttons.TextInputButton;
import coffee.dape.chaosui.components.buttons.ToggleButton;
import coffee.dape.chaosui.events.ChaosClickEvent;
import coffee.dape.chaosui.interfaces.common.DefaultCI;
import coffee.dape.chaosui.listeners.ChaosActionListener;
import coffee.dape.utils.ColourUtils;
import coffee.dape.utils.HeadUtils;
import coffee.dape.utils.ItemBuilder;
import coffee.dape.utils.PrintUtils;
import coffee.dape.utils.SoundUtils;
import coffee.dape.utils.data.DataUtils;

@ChaosGUI(name = "TestGUI",handler = TestChaosGuiHandler.class,template = InvTemplate.CHEST_6)
public class TestChaosGuiBuilder extends ChaosBuilder
{
	@Override
	public void init()
	{
		setInterface(new DefaultCI());
		
		setStaticComponent(new Button(4,ItemBuilder.of(HeadUtils.INFO_ICON.clone())
				.name("Information",ColourUtils.VISTA_BLUE)
				.lore()
				.wrap(ColourUtils.applyColour("Hello there this is some info. It has multiple lines as you can see.",ColourUtils.TEXT))
				.commit()
				.create()));
		
		setStaticComponent(new Button(10,ItemBuilder.of(Material.ACACIA_BOAT).name("Boat Button").create(),new ChaosActionListener()
		{
			@EventHandler
			public void onClick(ChaosClickEvent e)
			{
				PrintUtils.actionBar(e.getWhoClicked(),"Hello there");
				SoundUtils.playSound((Player) e.getWhoClicked(),Sound.ENTITY_BOAT_PADDLE_LAND,1.0f);
				e.getWhoClicked().closeInventory();
				((TestChaosGuiHandler) getHandler()).boatButtonClick(e);
			}
		}));
		
		setStaticComponent(new TextInputButton(12,"Text Input","Enter something rad","Enter new Input","123",Material.CHERRY_HANGING_SIGN));
		
		setStaticComponent(new ChoiceButton(14,"Choice Input",Material.HOPPER,"Choice 1","Choice 2","Choice 3","Choice 4","Choice 5","Choice 6","Choice 7"));
		
		setNavigationBack("TestGUIPaginator",16);
		
		setStaticComponent(new ToggleButton(28,true));
		
		setDecor(DecorType.HEADER_CAVE,0,1,2,3,5,6,7,8);
		setDecor(DecorType.FOOTER_GRASSLANDS,45,46,47,48,49,50,51,52,53);
	}

	@Override
	public void buildGUI(InventoryView view)
	{
		//GUISession session = getSession(view);
		
		System.out.println("REEEEE");
		DataUtils.printData(view.getItem(14));
	}
}
