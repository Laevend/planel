package coffee.dape.chaosui.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;

import coffee.dape.chaosui.components.ChaosRegion;
import coffee.dape.chaosui.events.ChaosClickEvent;
import coffee.dape.chaosui.events.ChaosDepositEvent;
import coffee.dape.chaosui.events.ChaosDragEvent;
import coffee.dape.chaosui.handler.DepositHandler;
import coffee.dape.utils.DelayUtils;
import coffee.dape.utils.ItemUtils;

public class ChaosActionListener
{
	public void onClick(ChaosClickEvent e) {}
	
	public void onDrag(ChaosDragEvent e) {}
	
	public static class Common
	{
		public static final ChaosActionListener ALLOW_CLICK_ON_EMPTY_SLOTS = new ChaosActionListener()
		{
			public void onClick(ChaosClickEvent e)
			{
				if(ItemUtils.isNullOrAir(e.getView().getItem(e.getRawSlot())))
				{
					e.setCancelled(true);
				}
			}
		};
		
		/**
		 * Allows items to be dragged into an empty area of the region for depositing
		 * 
		 * <p>A ChaosDepositEvent is fired for each slot inside a region that allows depositing
		 */
		public static final ChaosActionListener ALLOW_ITEM_DEPOSITING_PER_SLOT = new ChaosActionListener()
		{
			public void onDrag(ChaosDragEvent e)
			{
				// No use running this method if there is no handling of deposited materials
				e.setCancelled(true);
				if(!(e.getBuilder().getHandler() instanceof DepositHandler)) { return; }
				
				// Attempt to place dragged materials into block bank by adding up the blocks dragged into the chest inventory and
				// repeatedly setting the cursor to stacks of this item while calling the click event to handle it				
				InventoryType topInvType = e.getView().getTopInventory().getType();
				InventoryType bottomInvType = e.getView().getBottomInventory().getType();
				
				boolean dragInGUI = false;
				boolean dragInPlayersInv = false;
				
				int originAmount = e.getOldCursor().getAmount();
				int afterAmount = 0;
				
				// Pre check to make sure only dragging into regions that can be deposited into
				for(int slot : e.getRawSlots())
				{
					// Dragging in the GUI and the region is null or region will not accept deposits
					if(e.getView().getInventory(slot).getType() == topInvType && (e.getBuilder().getRegion(slot) == null || !e.getBuilder().getRegion(slot).canDeposit()))
					{
						return;
					}
					
					// Dragging in the GUI and the region allows deposits
					if(e.getView().getInventory(slot).getType() == topInvType && e.getBuilder().getRegion(slot).canDeposit())
					{
						dragInGUI = true;
					}
					
					// Dragging in the players inventory
					if(e.getView().getInventory(slot).getType() == bottomInvType)
					{
						dragInPlayersInv = true;
					}
					
					// Dragging in the GUI and players inventory
					if(dragInGUI && dragInPlayersInv)
					{ 
						return;
					}
					
					// Dragging only in the players inventory
					if(!dragInGUI && dragInPlayersInv)
					{
						e.setCancelled(false);
						return;
					}
				}
				
				// Cancel if dragging 
				for(int slot : e.getRawSlots())
				{					
					DepositHandler handler = (DepositHandler) e.getBuilder().getHandler();
					
					// Clicked in a region that exists and that allows items to be deposited into
					ItemStack depoItem = e.getNewItems().get(slot);
					ChaosRegion region = e.getBuilder().getRegion(slot);
					ChaosDepositEvent chaosDepoEvent = new ChaosDepositEvent(e.getView(),SlotType.CONTAINER,slot,ClickType.LEFT,InventoryAction.PLACE_ALL,e.getBuilder(),region);
					handler.onItemDeposit(chaosDepoEvent);
					
					if(chaosDepoEvent.isCancelled())
					{
						continue;
					}
					
					afterAmount+= depoItem.getAmount();
				}
				
				Material type = e.getOldCursor().getType();
				ItemStack stack = null;
				
				if(!ItemUtils.isNullOrAir(e.getCursor()))
				{
					stack = new ItemStack(e.getCursor().getType(),afterAmount);
				}
				
				int totalToBank = originAmount - afterAmount;
				final ItemStack afterStack = stack;
				final Player p = (Player) e.getWhoClicked();
				e.setCursor(null);
				
				DelayUtils.executeDelayedTask(() ->
				{			
					// Set cursor to amount to bank
					p.setItemOnCursor(new ItemStack(type,totalToBank));

					// cursor has been made null from the click event, set the amount to the 'afterAmount'
					p.setItemOnCursor(afterStack == null ? null : afterStack);
				},1);
			}
		};
		
		/**
		 * Allows items to be dragged into an empty area of the region for depositing
		 * 
		 * <p>A ChaosDepositEvent is fired for each unique region that allows depositing
		 */
		public static final ChaosActionListener ALLOW_ITEM_DEPOSITING_PER_REGION = new ChaosActionListener()
		{
			public void onDrag(ChaosDragEvent e)
			{
				// No use running this method if there is no handling of deposited materials
				e.setCancelled(true);
				if(!(e.getBuilder().getHandler() instanceof DepositHandler)) { return; }
				
				// Attempt to place dragged materials into block bank by adding up the blocks dragged into the chest inventory and
				// repeatedly setting the cursor to stacks of this item while calling the click event to handle it
				InventoryType topInvType = e.getView().getTopInventory().getType();
				InventoryType bottomInvType = e.getView().getBottomInventory().getType();
				Map<String,Integer> regionAmounts = new HashMap<>();
				
				boolean dragInGUI = false;
				boolean dragInPlayersInv = false;
				
				// Cancel if dragging 
				for(int slot : e.getRawSlots())
				{
					// Dragging in the GUI and the region is null or region will not accept deposits
					if(e.getView().getInventory(slot).getType() == topInvType && (e.getBuilder().getRegion(slot) == null || !e.getBuilder().getRegion(slot).canDeposit()))
					{
						return;
					}
					
					// Dragging in the GUI and the region allows deposits
					if(e.getView().getInventory(slot).getType() == topInvType && e.getBuilder().getRegion(slot).canDeposit())
					{
						dragInGUI = true;
					}
					
					// Dragging in the players inventory
					if(e.getView().getInventory(slot).getType() == bottomInvType)
					{
						dragInPlayersInv = true;
					}
					
					// Dragging in the GUI and players inventory
					if(dragInGUI && dragInPlayersInv)
					{ 
						return;
					}
					
					// Dragging only in the players inventory
					if(!dragInGUI && dragInPlayersInv)
					{
						e.setCancelled(false);
						return;
					}
					
					// Clicked in a region that exists and that allows items to be deposited into
					ItemStack depoItem = e.getNewItems().get(slot);
					ChaosRegion region = e.getBuilder().getRegion(slot);
					
					if(!regionAmounts.containsKey(region.getName()))
					{
						regionAmounts.put(region.getName(),depoItem.getAmount());
					}
					else
					{
						int currentAmount = regionAmounts.get(region.getName());
						regionAmounts.put(region.getName(),currentAmount + depoItem.getAmount());
					}
				}
				
				int originAmount = e.getOldCursor().getAmount();
				int afterAmount = 0;
				Material type = e.getOldCursor().getType();
				ItemStack stack = null;
				
				for(String regionName : regionAmounts.keySet())
				{
					DepositHandler handler = (DepositHandler) e.getBuilder().getHandler();
					
					// Not accurate but its inside the region at least
					ChaosRegion region = e.getBuilder().getRegion(regionName);
					int regionSlot = region.getArea().iterator().next();
					ChaosDepositEvent chaosDepoEvent = new ChaosDepositEvent(e.getView(),SlotType.CONTAINER,regionSlot,ClickType.LEFT,InventoryAction.PLACE_ALL,e.getBuilder(),region);
					handler.onItemDeposit(chaosDepoEvent);
					
					if(chaosDepoEvent.isCancelled())
					{
						continue;
					}
					
					afterAmount += regionAmounts.get(regionName);
				}
				
				if(!ItemUtils.isNullOrAir(e.getCursor()))
				{
					stack = new ItemStack(e.getCursor().getType(),afterAmount);
				}
				
				int totalToBank = originAmount - afterAmount;
				final ItemStack afterStack = stack;
				final Player p = (Player) e.getWhoClicked();
				e.setCursor(null);
				
				DelayUtils.executeDelayedTask(() ->
				{			
					// Set cursor to amount to bank
					p.setItemOnCursor(new ItemStack(type,totalToBank));
					
					// cursor has been made null from the click event, set the amount to the 'afterAmount'
					p.setItemOnCursor(afterStack == null ? null : afterStack);
				},1);
			}
		};
	}
}
