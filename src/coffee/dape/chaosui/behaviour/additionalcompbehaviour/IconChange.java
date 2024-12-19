package coffee.dape.chaosui.behaviour.additionalcompbehaviour;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import coffee.dape.chaosui.events.ChaosChangeIconEvent;
import coffee.dape.chaosui.events.ChaosClickEvent;
import coffee.dape.chaosui.handler.ChangeIconHandler;
import coffee.dape.utils.ItemBuilder;
import coffee.dape.utils.ItemUtils;
import coffee.dape.utils.SoundUtils;

public class IconChange
{
	/**
	 * Changes the icon of a paginator item
	 * @param e ChaosClickEvent
	 */
	public static void onChangeIcon(ChaosClickEvent e)
	{
		if(e.getBuilder().getHandler() instanceof ChangeIconHandler handler)
		{
			if(ItemUtils.isNullOrAir(e.getWhoClicked().getItemOnCursor())) { SoundUtils.playSound((Player) e.getWhoClicked(),Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO,1.0f,0.1f); return; }
			ItemStack nameTag = e.getWhoClicked().getItemOnCursor();
			ItemStack stack = e.getView().getItem(e.getRawSlot());
			
			if(nameTag.getType() == Material.NAME_TAG || nameTag.getType() == Material.BUNDLE) { return; }
			
			System.out.println(e.getRawSlot() + " - " + e.getSlot() + " - " + e.getView().getTitle());
			
			ChaosChangeIconEvent ccie = new ChaosChangeIconEvent(e.getView(),e.getBuilder(),e.getView().getItem(e.getRawSlot()),e.getView().getItem(e.getRawSlot()).getType(),e.getWhoClicked().getItemOnCursor().getType(),(Player) e.getWhoClicked(),e.getRawSlot());
			handler.onIconChange(ccie);
			
			if(ccie.isCancelled()) { return; }
			
			e.getView().setItem(e.getRawSlot(),ItemBuilder.of(stack).mat(e.getWhoClicked().getItemOnCursor().getType()).create());
			SoundUtils.playSound((Player) e.getWhoClicked(),Sound.BLOCK_BONE_BLOCK_BREAK,2.0f);
			
			//int pageNum = DataUtils.get(PAGE_TAG,e.getView().getItem(pageNumberSlot)).asInt();
			//buildInterface(e.getView(),pageNum);
		}
	}
}
