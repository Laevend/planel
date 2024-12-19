package coffee.dape.chaosui;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import coffee.dape.chaosui.ChaosDecor.DecorLocation;
import coffee.dape.chaosui.ChaosDecor.DecorType;
import coffee.dape.chaosui.ChaosFactory.InvTemplate;
import coffee.dape.chaosui.anno.ChaosGUI;
import coffee.dape.chaosui.behaviour.Behaviours;
import coffee.dape.chaosui.components.ChaosComponent;
import coffee.dape.chaosui.components.ChaosMultiComponent;
import coffee.dape.chaosui.components.ChaosRegion;
import coffee.dape.chaosui.components.buttons.AnimatedButton;
import coffee.dape.chaosui.components.buttons.BackButton;
import coffee.dape.chaosui.interfaces.ChaosInterface;
import coffee.dape.chaosui.interfaces.common.DefaultCI;
import coffee.dape.chaosui.interfaces.paginator.Paginator;
import coffee.dape.chaosui.slots.ChaosSlot;
import coffee.dape.exception.MissingAnnotationException;
import coffee.dape.utils.ItemBuilder;
import coffee.dape.utils.Logg;
import coffee.dape.utils.Logg.Common.Component;
import coffee.dape.utils.MathUtils;
import coffee.dape.utils.SoundUtils;
import coffee.dape.utils.SoundUtils.SoundMixer;
import coffee.dape.utils.structs.Pair;

public abstract class ChaosBuilder
{
	private String name;					// GUI Name
	private ChaosHandler handler = null;	// Handler to handle click and drag interactions
	private InvTemplate template;			// Vanilla inventory template used as a base for the GUI
	private ChaosInterface cInterface;		// Interface determines how the GUI will behave when interacted with
	
	// Data about regions
	private Map<String,ChaosRegion> regions = new HashMap<>();
	
	// Data about slots
	private Map<Integer,ChaosSlot> slots = new HashMap<>();
	
	// Stores data about who is viewing this GUI
	private Set<UUID> viewers = new HashSet<>();
	
	// Material used to fill the GUI
	private Material fillMaterial = null;
	
	// If the GUI has any decor
	// <DecorLocation,<DecorType,DecorRegionName>>
	private Map<DecorLocation,Pair<DecorType,String>> decorRegions = new HashMap<>();
	
	private SoundMixer openSoundMixer = null;
	private SoundMixer closeSoundMixer = null;
	
	// Prefix used in setting and getting data from players session for a group of GUI's
	private String GUIPrefix;
	
	public ChaosBuilder()
	{
		try
		{
			Class<?> builderClass = this.getClass();
			
			// Check that this builder has the gui annotation
			if(!builderClass.isAnnotationPresent(ChaosGUI.class))
			{
				Logg.Common.printFail(Component.GUI,"Building","? -> " + builderClass.getSimpleName());
				throw new MissingAnnotationException("ChaosGUI of class " + builderClass.getSimpleName() + " Is missing the '@ChaosGUI' annotation!");
			}
			
			ChaosGUI guiAnno = builderClass.getAnnotation(ChaosGUI.class);
			
			this.name = guiAnno.name();
			
			if(this.name.isBlank() || this.name.isEmpty())
			{
				Logg.Common.printFail(Component.GUI,"Building","? -> " + builderClass.getSimpleName());
				throw new IllegalArgumentException("ChaosGUI of class " + builderClass.getSimpleName() + " cannot have an empty or blank name!");
			}
			
			Class<?> handlerClass = guiAnno.handler();
			
			// If the Handler class is not the default Object.class meaning there is no handler
			if(!handlerClass.equals(Object.class))
			{
				if(!ChaosHandler.class.isAssignableFrom(handlerClass))
				{
					Logg.Common.printFail(Component.GUI,"Building",this.name);
					throw new IllegalArgumentException("ChaosGUI '" + this.name + "' cannot be initialised with a handler that does not extend ChaosHandler!");
				}
				
				try
				{
					Constructor<?> cons = handlerClass.getConstructor();
					this.handler = (ChaosHandler) cons.newInstance();
				}
				catch(NoSuchMethodException e)
				{
					Logg.Common.printFail(Component.GUI,"Building",this.name);
					Logg.fatal("ChaosGUI '" + this.name + "' cannot initialise handler because default constructor does not exist!",e);
					return;
				}
				catch(SecurityException e)
				{
					Logg.Common.printFail(Component.GUI,"Building",this.name);
					Logg.fatal("ChaosGUI '" + this.name + "' cannot initialise handler because a security manager is present preventing it!",e);
					return;
				}
				catch (Exception e)
				{
					Logg.Common.printFail(Component.GUI,"Building",this.name);
					Logg.fatal("ChaosGUI '" + this.name + "' cannot initialise handler!",e);
				}
			}
			
			this.template = guiAnno.template();
		    
			try
			{
				init();
				
				// Use default interface if one is not supplied
				if(!hasInterface())
				{
					setInterface(new DefaultCI());
				}
				
				checkIfRegionsOverlap(false);
			}
			catch(Exception e)
			{
				Logg.Common.printFail(Component.GUI,"Building",this.name);
				Logg.error("ChaosGUI '" + this.name + "' cannot initialise!",e);
				return;
			}
			
			ChaosFactory.addGUI(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialises any prerequisite components the GUI needs
	 */
	public abstract void init() throws Exception;
	
	/**
	 * Builds GUI
	 * @param human Player
	 */
	public void build(HumanEntity human)
	{
		if(human instanceof Player player) { build(player); }
		Logg.warn("ChaosGUI could not open " + name + " as human entity is not an instance of player!");
	}
	
	/**
	 * Builds GUI
	 * @param sender Player
	 */
	public void build(CommandSender sender)
	{
		if(sender instanceof Player player) { build(player); }
		Logg.warn("ChaosGUI could not open " + name + " as command sender is not an instance of player!");
	}
	
	/**
	 * Builds a GUI using a player
	 * @param p The player
	 * @return InventoryView
	 */
	public InventoryView build(Player p)
	{
		Inventory inv = null;
		InventoryView view = null;
		
		// Stop the clocks of animated buttons
		// Not doing this causes them to appear in other GUIS if navigated to quickly
		for(ChaosSlot slot : ChaosFactory.getSession(p).getTempSlots().values())
		{
			if(slot.getSlotComponent().getType() == ChaosComponent.Type.ANIMATED_BUTTON)
			{
				((AnimatedButton) slot.getSlotComponent()).stop();
			}
		}
		
		// Clear temporary ChaosComponents
		ChaosFactory.getSession(p).getTempSlots().clear();
		
		switch(template)
		{
			case CHEST_1:
			case CHEST_2:
			case CHEST_3:
			case CHEST_4:
			case CHEST_5:
			case CHEST_6:
			{
				inv = Bukkit.createInventory(null,template.slotCount,name);
				break;
			}
			case BARREL:
			{
				inv = Bukkit.createInventory(null,InventoryType.valueOf(template.toString()),name);
				break;
			}
			case DROPPER:
			{
				inv = Bukkit.createInventory(null,InventoryType.valueOf(template.toString()),name);
				break;
			}
			case HOPPER:
			{
				inv = Bukkit.createInventory(null,InventoryType.valueOf(template.toString()),name);
				break;
			}
			default:
			{
				Logg.fatal("ERROR! Invalid ChaosGUI template provided!");
				return null;
			}
		}
		
		if(inv == null)
		{
			Logg.fatal("ERROR! Unexpected Null Inventory on creation!");
			return null;
		}
		
		p.openInventory(inv);
		view = p.getOpenInventory();
		
		buildInterface(view);
		buildComponents(view);
		buildDecor(view);
		
		if(this.fillMaterial != null)
		{
			// Paint regions that only allow filling
			for(ChaosRegion region : regions.values())
			{
				ChaosFactory.paintGUI(view,region,fillMaterial);
			}
		}
		
		buildGUI(view);
		
		buildTempComponents(p,view);
		
		if(openSoundMixer != null)
		{
			openSoundMixer.play(p);
		}
		
		ChaosFactory.signGUI(view);
		
		viewers.add(p.getUniqueId());
		return view;
	}
	
	/**
	 * Builds a GUI using an existing InventoryView
	 * @param view The InventoryView the player is in
	 */
	public abstract void buildGUI(InventoryView view);
	
	/**
	 * Checks if any regions defined overlap each other.
	 * 
	 * <p>Overlapping regions can cause issues when attempting to retrieve a region when a slot is clicked/dragged on
	 * due a region is fetched based on what slot was clicked and depending on how the regions are ordered one region
	 * may be prioritised over another.
	 * @param silent If true, prints regions that have overlapping slots
	 * @return True if regions are overlapping, false otherwise
	 */
	public boolean checkIfRegionsOverlap(boolean silent)
	{
		Map<Integer,Set<ChaosRegion>> dupeMap = new HashMap<>();
		
		for(ChaosRegion region : regions.values())
		{
			for(int slot : region.getArea())
			{
				if(!dupeMap.containsKey(slot))
				{
					dupeMap.put(slot,new HashSet<>());
				}
				
				dupeMap.get(slot).add(region);
			}
		}
		
		boolean isOverlapping = false;
		
		for(int slot : dupeMap.keySet())
		{
			if(dupeMap.get(slot).size() > 1)
			{
				if(silent) { return true; }
				isOverlapping = true;
				
				Logg.warn("ChaosGUI " + this.name + " has overlapping region(s) for slot " + slot);
				
				for(ChaosRegion region : dupeMap.get(slot))
				{
					Logg.warn(" -> Region: " + region.getName());
				}
			}
		}
		
		return isOverlapping;
	}
	
	/**
	 * Get region for a raw slot
	 * @param rawSlot Rawslot number
	 * @return Region slot belongs to
	 */
	public ChaosRegion getRegion(int rawSlot)
	{
		for(ChaosRegion region : regions.values())
		{
			if(region.getArea().contains(rawSlot)) { return region; }
		}
		
		return null;
	}
	
	public ChaosRegion getRegion(String regionName)
	{
		if(!regions.containsKey(regionName)) { return null; }
		return regions.get(regionName);
	}
	
	/**
	 * Defines a chaos region
	 * @param region
	 */
	public void defineRegion(ChaosRegion region)
	{
		regions.put(region.getName(),region);
	}
	
	public void defineRegion(String regionName,int firstSlot,int... slotsOccupying)
	{
		ChaosRegion region = new ChaosRegion(regionName,firstSlot,slotsOccupying);
		regions.put(region.getName(),region);
	}
	
	public void defineRegion(String regionName,Set<Integer> slotsOccupying)
	{
		ChaosRegion region = new ChaosRegion(regionName,slotsOccupying);
		regions.put(region.getName(),region);
	}
	
	/**
	 * Defines a standard header region
	 */
	public void defineHeaderRegion(String headerName)
	{
		ChaosRegion region;
		
		switch(template)
		{
			case CHEST_1:
			case CHEST_2:
			case CHEST_3:
			case CHEST_4:
			case CHEST_5:
			case CHEST_6:
			case BARREL:
			{
				region = new ChaosRegion(headerName,MathUtils.getSetOfNumbers(0,8));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			case DROPPER:
			{
				region = new ChaosRegion(headerName,MathUtils.getSetOfNumbers(0,2));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			case HOPPER:
			{
				Logg.warn("ChaosGUI " + this.name + " cannot define basic header region!");
				break;
			}
			default:
			{
				Logg.fatal("ERROR! Invalid ChaosGUI template provided!");
			}
		}
	}
	
	/**
	 * Defines a standard footer region
	 */
	public void defineFooterRegion(String footerName)
	{
		ChaosRegion region;
		
		switch(template)
		{
			case CHEST_1:
			{
				Logg.warn("ChaosGUI " + this.name + " cannot define basic footer region!");
				break;
			}
			case CHEST_2:
			{
				region = new ChaosRegion(footerName,MathUtils.getSetOfNumbers(9,17));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			case BARREL:
			case CHEST_3:
			{
				region = new ChaosRegion(footerName,MathUtils.getSetOfNumbers(18,26));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			case CHEST_4:
			{
				region = new ChaosRegion(footerName,MathUtils.getSetOfNumbers(27,35));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			case CHEST_5:
			{
				region = new ChaosRegion(footerName,MathUtils.getSetOfNumbers(36,44));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			case CHEST_6:
			{
				region = new ChaosRegion(footerName,MathUtils.getSetOfNumbers(45,53));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			case DROPPER:
			{
				region = new ChaosRegion(footerName,MathUtils.getSetOfNumbers(6,8));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			case HOPPER:
			{
				Logg.warn("ChaosGUI " + this.name + " cannot define basic footer region!");
				break;
			}
			default:
			{
				Logg.fatal("ERROR! Invalid ChaosGUI template provided!");
			}
		}
	}
	
	/**
	 * Defines a standard body region
	 */
	public void defineBodyRegion(String bodyName)
	{
		ChaosRegion region;
		
		switch(template)
		{
			case CHEST_1:
			{
				region = new ChaosRegion(bodyName,MathUtils.getSetOfNumbers(0,8));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			case CHEST_2:
			{
				region = new ChaosRegion(bodyName,MathUtils.getSetOfNumbers(0,17));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			case BARREL:
			case CHEST_3:
			{
				region = new ChaosRegion(bodyName,MathUtils.getSetOfNumbers(9,17));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			case CHEST_4:
			{
				region = new ChaosRegion(bodyName,MathUtils.getSetOfNumbers(9,26));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			case CHEST_5:
			{
				region = new ChaosRegion(bodyName,MathUtils.getSetOfNumbers(9,35));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			case CHEST_6:
			{
				region = new ChaosRegion(bodyName,MathUtils.getSetOfNumbers(9,44));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			case DROPPER:
			{
				region = new ChaosRegion(bodyName,MathUtils.getSetOfNumbers(3,5));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			case HOPPER:
			{
				region = new ChaosRegion(bodyName,MathUtils.getSetOfNumbers(0,4));
				region.setFillable(true);
				regions.put(region.getName(),region);
				break;
			}
			default:
			{
				Logg.fatal("ERROR! Invalid ChaosGUI template provided!");
			}
		}
	}
	
	/**
	 * Set a static GUI component that will never change regardless of who views this GUI
	 * @param com Chaos Component to set
	 */
	public void setStaticComponent(ChaosComponent com) throws IllegalStateException
	{
		if(!slots.containsKey(com.getOccupyingSlot()))
		{
			slots.put(com.getOccupyingSlot(),new ChaosSlot(com.getOccupyingSlot()));
		}
		
		// GUI needs to know of animated button to enable their clocks
		if(com.getType() == ChaosComponent.Type.ANIMATED_BUTTON)
		{
			((AnimatedButton) com).initClock(this);
		}
		
		slots.get(com.getOccupyingSlot()).setSlotComponent(com);
	}
	
	/**
	 * Set a static GUI multi component that will never change regardless of who views this GUI
	 * @param mcom ChaosMultiComponent to set
	 */
	public void setStaticMultiComponent(ChaosMultiComponent mcom) throws IllegalStateException
	{
		for(ChaosComponent com : mcom.getComponents().values())
		{
			setStaticComponent(com);
		}
	}
	
	/**
	 * Checks if a slot is conditional meaning special logic must be tested
	 * to check if the player clicking can perform this click action
	 * @param slot Rawslot
	 * @return
	 */
	public boolean hasSlotBehaviour(int slot)
	{
		// Loop is not a big deal as there are not many regions to check in a GUI
		for(ChaosRegion region : regions.values())
		{
			if(!region.getArea().contains(slot)) { continue; }
			return region.hasBehaviour();
		}
		
		return false;
	}
	
	/**
	 * Gets behaviour of the region the slot resides in
	 * @param slot Slot to get behaviour for
	 * @return Region behaviour for this slot, null if no behaviour exists or slot does not reside in a region
	 */
	public Behaviours getSlotBehaviour(int slot)
	{
		// Loop is not a big deal as there are not many regions to check in a GUI
		for(ChaosRegion region : regions.values())
		{
			if(!region.getArea().contains(slot)) { continue; }
			return region.getBehaviour();
		}
		
		return null;
	}
	
	/**
	 * Checks if a slot has a chaos component inside it
	 * @param slot rawslot to check
	 * @return
	 */
	public boolean isSlotOccupied(int slot)
	{
		if(!slots.containsKey(slot)) { return false; }
		return slots.get(slot).isOccupied();
	}
	
	public Map<Integer,ChaosSlot> getSlots()
	{
		return slots;
	}
	
	/**
	 * Set behaviour for a region
	 * @param region The name of the region
	 * @param behaviour SlotBehaviour detailing how this slot should behave when interacted with
	 */
	public void setRegionBehaviour(String regionName,Behaviours behaviour) throws IllegalStateException
	{
		if(!regions.containsKey(regionName))
		{
			Logg.error("ChaosRegion " + regionName + " does not exist! Cannot set behaviour for this region in ChaosGUI " + this.name + "!");
			return;
		}
		
		regions.get(regionName).setBehaviour(behaviour);
	}
	
	/**
	 * Clears all existing regions
	 */
	public void clearRegions()
	{
		regions.clear();
	}
	
	public Map<String,ChaosRegion> getRegions()
	{
		return regions;
	}
	
	/**
	 * Sets the interface that defines how this gui will function
	 * @param cInterface GUI Interface
	 */
	public void setInterface(ChaosInterface cInterface)
	{
		this.cInterface = cInterface;
		this.cInterface.init(this);
	}
	
	/**
	 * Builds the interface for the GUI
	 * @param view
	 */
	private void buildInterface(InventoryView view)
	{
		if(cInterface == null) { return; }
		cInterface.buildInterface(view);
	}
	
	/**
	 * Gets the interface this GUI is using
	 * @return ChaosInterface
	 */
	public ChaosInterface getInterface()
	{
		return cInterface;
	}
	
	/**
	 * Checks if this GUI has an interface at all
	 * @return True if this GUI uses an interface, false otherwise
	 */
	public boolean hasInterface()
	{
		return (this.cInterface != null) ? true : false;
	}
	
	/**
	 * Checks if this GUI uses the default interface
	 * @return True if this GUI uses the default interface, false if there is no interface or it doesn't use the default interface
	 */
	public boolean hasDefaultInterface()
	{
		if(!hasInterface()) { return false; }
		return this.cInterface.getType() == ChaosInterface.Type.DEFAULT;
	}
	
	/**
	 * Builds components that were added to this GUI
	 * @param view
	 */
	private void buildComponents(InventoryView view)
	{
		for(ChaosSlot cs : this.slots.values())
		{
			if(!cs.isOccupied()) { continue; }
			view.setItem(cs.getRawSlot(),cs.getSlotComponent().getAppearance());
			
			if(cs.getSlotComponent().getType() == ChaosComponent.Type.ANIMATED_BUTTON)
			{
				((AnimatedButton) cs.getSlotComponent()).restartClock();
			}
		}
	}
	
	private void buildTempComponents(Player p,InventoryView view)
	{
		for(ChaosSlot cs : ChaosFactory.getSession(p).getTempSlots().values())
		{
			if(!cs.isOccupied()) { continue; }
			view.setItem(cs.getRawSlot(),cs.getSlotComponent().getAppearance());
			
			if(cs.getSlotComponent().getType() == ChaosComponent.Type.ANIMATED_BUTTON)
			{
				((AnimatedButton) cs.getSlotComponent()).restartClock();
			}
		}
	}
	
	/**
	 * Note to self,
	 * can't do a gui name check in the setNavigationBack() methods as
	 * these are usually called during an init of a GUI.
	 * At that point not all GUI's have been initialised so it's random
	 * chance if the GUI being navigated to has even been initialised yet
	 */
	
	/**
	 * Adds a navigation button to the GUI automatically
	 * This button will allow the user to navigate to another GUI
	 * 
	 * @param defaultGuiToNavigateTo Name of the GUI this button will navigate the user to
	 */
	public void setNavigationBack(String defaultGuiToNavigateTo)
	{
		BackButton bb = new BackButton(defaultGuiToNavigateTo);
		setStaticComponent(bb);
	}
	
	/**
	 * Adds a navigation button to the GUI automatically
	 * This button will allow the user to navigate to another GUI
	 * @param defaultGuiToNavigateTo Name of the GUI this button will navigate the user to
	 * @param slot The slot this button will be placed in
	 */
	public void setNavigationBack(String defaultGuiToNavigateTo,int slot)
	{
		BackButton bb = new BackButton(defaultGuiToNavigateTo,slot);
		setStaticComponent(bb);
	}
	
	/**
	 * Paints the GUI when its being built. Painting happens at the end of the
	 * GUI building pipeline
	 * @param mat Material to paint the empty spaces as
	 * @param mode PaintMode
	 */
	public void setFill(Material mat)
	{		
		this.fillMaterial = mat;
	}
	
	/**
	 * Gets this builders associated handler class used for handling interaction
	 * @return ChaosHandler
	 */
	public ChaosHandler getHandler()
	{
		if(handler == null) { return ChaosFactory.getBlankHandler(); }
		return handler;
	}
	
	/**
	 * If this ChaosBuilder has an associated handler
	 * @return True if this GUI has a handler, false otherwise
	 */
	public boolean hasHandler()
	{
		return handler != null;
	}
	
	/**
	 * Removes a player viewing this GUI
	 * @param p Player
	 */
	public void removeViewer(Player p)
	{
		removeViewer(p.getUniqueId());
	}
	
	/**
	 * Removes a player viewing this GUI
	 * @param uuid A players UUID
	 */
	public void removeViewer(UUID uuid)
	{
		this.viewers.remove(uuid);
	}
	
	/**
	 * Returns a map of players viewing this GUI
	 * @return
	 */
	public Set<UUID> getViewers()
	{
		return viewers;
	}
	
	/**
	 * Gets the template vanilla inventory used to build this GUI
	 * @return The Inventory Template type
	 */
	public InvTemplate getTemplate()
	{
		return template;
	}
	
	/**
	 * Gets the name of this GUI
	 * @return The name of the GUI
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Sets GUI decoration
	 * @param decor The decor type to set
	 * @param occupyingSlots The slots where this decor will be displayed in
	 */
	public void setDecor(DecorType decor,int... occupyingSlots)
	{
		if(!ChaosDecor.isCompatibleForDecor(template,decor))
		{
			Logg.warn("ChaosGUI " + this.name + " has defined a decor of " + decor.toString() + " but is not suitable for the template " + this.template.toString() + "!");
			return;
		}
		
		if(occupyingSlots == null || occupyingSlots.length == 0)
		{
			Logg.error("ChaosGUI " + this.name + " has defined a decor of " + decor.toString() + " but no slots have been specified as to where this decor should be set!");
			return;
		}
		
		decorRegions.put(decor.getLocation(),new Pair<>(decor,decor.getRegionName()));
		defineRegion(decor.getRegionName(),occupyingSlots[0],occupyingSlots);
	}
	
	/**
	 * Sets GUI decoration
	 * @param decor The decor type to set
	 * @param occupyingSlots The slots where this decor will be displayed in
	 */
	public void setDecor(DecorType decor)
	{
		if(!ChaosDecor.isCompatibleForDecor(template,decor))
		{
			Logg.warn("ChaosGUI " + this.name + " has defined a decor of " + decor.toString() + " but is not suitable for the template " + this.template.toString() + "!");
			return;
		}
		
		int[] occupyingSlots;
		
		if(decor.getLocation() == DecorLocation.HEADER)
		{
			occupyingSlots = new int[] {0,1,2,3,4,5,6,7,8};
		}
		else
		{
			occupyingSlots = new int[] {45,46,47,48,49,50,51,52,53};
		}
		
		if(occupyingSlots == null || occupyingSlots.length == 0)
		{
			Logg.error("ChaosGUI " + this.name + " has defined a decor of " + decor.toString() + " but no slots have been specified as to where this decor should be set!");
			return;
		}
		
		decorRegions.put(decor.getLocation(),new Pair<>(decor,decor.getRegionName()));
		defineRegion(decor.getRegionName(),occupyingSlots[0],occupyingSlots);
	}
	
	/**
	 * Builds decor for the GUI
	 * @param view InventoryView
	 */
	private void buildDecor(InventoryView view)
	{
		for(Pair<DecorType,String> decor : decorRegions.values())
		{
			if(!ChaosDecor.isCompatibleForDecor(template,decor.getValueA()))
			{
				Logg.warn("ChaosGUI " + this.name + " has defined a decor of " + decor.getValueA().toString() + " but is not suitable for the template " + this.template.toString() + "!");
				continue;
			}
			
			if(!regions.containsKey(decor.getValueB()))
			{
				Logg.error("ChaosGUI " + this.name + " has defined a decor region of " + decor.getValueA().getRegionName() + " but this region cannot be found in this builders region map!");
				continue;
			}
			
			ChaosRegion decorRegion = regions.get(decor.getValueB());
			Set<Integer> area = decorRegion.getArea();
			LinkedList<Material> materials = ChaosDecor.getDecor(decor.getValueA(),area.size()); 
			
			for(int slot : area)
			{
				Material mat = materials.remove(0);
				
				if(view.getItem(slot) == null)
				{
					ItemStack stack = ItemBuilder.of(mat).name(" ").create();
					view.setItem(slot,stack);
					continue;
				}
				
				// Do not override set slots
				//view.getItem(slot).setType(mat);
			}
		}
	}
	
	/**
	 * Set the sound to play when the GUI opens
	 * @param sound Sound
	 * @param pitch pitch
	 */
	public void setOpenSound(Sound sound,float pitch)
	{
		openSoundMixer = new SoundUtils().new SoundMixer(sound,pitch);
	}
	
	public void setOpenSound(SoundMixer mixer)
	{
		openSoundMixer = mixer;
	}
	
	public SoundMixer getOpenSoundMixer()
	{
		return openSoundMixer;
	}
	
	/**
	 * Set the sound to play when the GUI closes
	 * @param sound Sound
	 * @param pitch pitch
	 */
	public void setCloseSound(Sound sound,float pitch)
	{
		closeSoundMixer = new SoundUtils().new SoundMixer(sound,pitch);
	}
	
	public void setCloseSound(SoundMixer mixer)
	{
		closeSoundMixer = mixer;
	}
	
	public SoundMixer getCloseSoundMixer()
	{
		return closeSoundMixer;
	}

	public String getGUIPrefix()
	{
		return GUIPrefix;
	}

	public void setGUIPrefix(String gUIPrefix)
	{
		GUIPrefix = gUIPrefix;
	}
	
	public void refresh(Player p)
	{
		build(p);
	}
	
	public void refreshPaginator(InventoryView view,Player p)
	{
		if(!hasInterface()) { return; }
		if(this.cInterface.getType() != ChaosInterface.Type.PAGINATOR) { return; }
		
		Paginator paginator = (Paginator) this.cInterface;
		ChaosFactory.clearGUI(view,paginator.getPageSlots());
		cInterface.buildInterface(view);
		ChaosFactory.signGUI(view);
	}
}
