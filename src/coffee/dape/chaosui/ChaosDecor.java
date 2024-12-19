package coffee.dape.chaosui;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;

import coffee.dape.chaosui.ChaosFactory.InvTemplate;
import coffee.dape.chaosui.components.ChaosRegion;
import coffee.dape.utils.Logg;
import coffee.dape.utils.structs.StaticWeightedPool;

/**
 * 
 * @author Laeven
 * 
 * Generates random decor for the header and footer of GUIs
 */
public class ChaosDecor
{
	private static StaticWeightedPool<Material> FOOTER_GRASSLANDS = new StaticWeightedPool<>();
	private static StaticWeightedPool<Material> FOOTER_OCEAN = new StaticWeightedPool<>();
	private static StaticWeightedPool<Material> FOOTER_FLOWERBED = new StaticWeightedPool<>();
	private static StaticWeightedPool<Material> FOOTER_NETHER = new StaticWeightedPool<>();
	
	private static StaticWeightedPool<Material> HEADER_CAVE = new StaticWeightedPool<>();
	
	private static Map<InvTemplate,Set<DecorType>> compatibleDecors = new EnumMap<>(InvTemplate.class);
	
	static
	{
		compatibleDecors.put(InvTemplate.CHEST_1,new HashSet<>());
		compatibleDecors.put(InvTemplate.CHEST_2,new HashSet<>());
		compatibleDecors.put(InvTemplate.CHEST_3,new HashSet<>());
		compatibleDecors.put(InvTemplate.CHEST_4,new HashSet<>());
		compatibleDecors.put(InvTemplate.CHEST_5,new HashSet<>());
		compatibleDecors.put(InvTemplate.CHEST_6,new HashSet<>());
		compatibleDecors.put(InvTemplate.BARREL,new HashSet<>());
		compatibleDecors.put(InvTemplate.DROPPER,new HashSet<>());
		
		compatibleDecors.get(InvTemplate.CHEST_1).add(DecorType.HEADER_CAVE);
		
		compatibleDecors.get(InvTemplate.CHEST_2).add(DecorType.FOOTER_GRASSLANDS);
		compatibleDecors.get(InvTemplate.CHEST_2).add(DecorType.FOOTER_OCEAN);
		compatibleDecors.get(InvTemplate.CHEST_2).add(DecorType.FOOTER_FLOWERBED);
		compatibleDecors.get(InvTemplate.CHEST_2).add(DecorType.FOOTER_NETHER);
		compatibleDecors.get(InvTemplate.CHEST_2).add(DecorType.HEADER_CAVE);
		
		compatibleDecors.get(InvTemplate.CHEST_3).add(DecorType.FOOTER_GRASSLANDS);
		compatibleDecors.get(InvTemplate.CHEST_3).add(DecorType.FOOTER_OCEAN);
		compatibleDecors.get(InvTemplate.CHEST_3).add(DecorType.FOOTER_FLOWERBED);
		compatibleDecors.get(InvTemplate.CHEST_3).add(DecorType.FOOTER_NETHER);
		compatibleDecors.get(InvTemplate.CHEST_3).add(DecorType.HEADER_CAVE);
		
		compatibleDecors.get(InvTemplate.CHEST_4).add(DecorType.FOOTER_GRASSLANDS);
		compatibleDecors.get(InvTemplate.CHEST_4).add(DecorType.FOOTER_OCEAN);
		compatibleDecors.get(InvTemplate.CHEST_4).add(DecorType.FOOTER_FLOWERBED);
		compatibleDecors.get(InvTemplate.CHEST_4).add(DecorType.FOOTER_NETHER);
		compatibleDecors.get(InvTemplate.CHEST_4).add(DecorType.HEADER_CAVE);

		compatibleDecors.get(InvTemplate.CHEST_5).add(DecorType.FOOTER_GRASSLANDS);
		compatibleDecors.get(InvTemplate.CHEST_5).add(DecorType.FOOTER_OCEAN);
		compatibleDecors.get(InvTemplate.CHEST_5).add(DecorType.FOOTER_FLOWERBED);
		compatibleDecors.get(InvTemplate.CHEST_5).add(DecorType.FOOTER_NETHER);
		compatibleDecors.get(InvTemplate.CHEST_5).add(DecorType.HEADER_CAVE);
		
		compatibleDecors.get(InvTemplate.CHEST_6).add(DecorType.FOOTER_GRASSLANDS);
		compatibleDecors.get(InvTemplate.CHEST_6).add(DecorType.FOOTER_OCEAN);
		compatibleDecors.get(InvTemplate.CHEST_6).add(DecorType.FOOTER_FLOWERBED);
		compatibleDecors.get(InvTemplate.CHEST_6).add(DecorType.FOOTER_NETHER);
		compatibleDecors.get(InvTemplate.CHEST_6).add(DecorType.HEADER_CAVE);
		
		compatibleDecors.get(InvTemplate.BARREL).add(DecorType.FOOTER_GRASSLANDS);
		compatibleDecors.get(InvTemplate.BARREL).add(DecorType.FOOTER_OCEAN);
		compatibleDecors.get(InvTemplate.BARREL).add(DecorType.FOOTER_FLOWERBED);
		compatibleDecors.get(InvTemplate.BARREL).add(DecorType.FOOTER_NETHER);
		compatibleDecors.get(InvTemplate.BARREL).add(DecorType.HEADER_CAVE);
		
		compatibleDecors.get(InvTemplate.DROPPER).add(DecorType.FOOTER_GRASSLANDS);
		compatibleDecors.get(InvTemplate.DROPPER).add(DecorType.FOOTER_OCEAN);
		compatibleDecors.get(InvTemplate.DROPPER).add(DecorType.FOOTER_FLOWERBED);
		compatibleDecors.get(InvTemplate.DROPPER).add(DecorType.FOOTER_NETHER);
		compatibleDecors.get(InvTemplate.DROPPER).add(DecorType.HEADER_CAVE);
	}
	
	public static void init()
	{
		FOOTER_GRASSLANDS.add(Material.SHORT_GRASS,50);
		FOOTER_GRASSLANDS.add(Material.TALL_GRASS,25);
		FOOTER_GRASSLANDS.add(Material.POPPY,10);
		FOOTER_GRASSLANDS.add(Material.DANDELION,10);
		FOOTER_GRASSLANDS.add(Material.DEAD_BUSH,10);
		FOOTER_GRASSLANDS.add(Material.FERN,20);
		FOOTER_GRASSLANDS.add(Material.LARGE_FERN,20);
		FOOTER_GRASSLANDS.add(Material.BROWN_MUSHROOM,2);
		FOOTER_GRASSLANDS.add(Material.RED_MUSHROOM,2);
		FOOTER_GRASSLANDS.compileRangeMap();
		
		FOOTER_OCEAN.add(Material.SEAGRASS,50);
		FOOTER_OCEAN.add(Material.SEA_PICKLE,10);
		FOOTER_OCEAN.add(Material.BRAIN_CORAL,5);
		FOOTER_OCEAN.add(Material.BUBBLE_CORAL,5);
		FOOTER_OCEAN.add(Material.FIRE_CORAL,5);
		FOOTER_OCEAN.add(Material.BUBBLE_CORAL_FAN,5);
		FOOTER_OCEAN.add(Material.BRAIN_CORAL_FAN,5);
		FOOTER_OCEAN.add(Material.TUBE_CORAL_FAN,5);
		FOOTER_OCEAN.add(Material.HORN_CORAL,5);
		FOOTER_OCEAN.add(Material.FIRE_CORAL_FAN,5);
		FOOTER_OCEAN.add(Material.HORN_CORAL_FAN,5);
		FOOTER_OCEAN.add(Material.DEAD_BRAIN_CORAL,1);
		FOOTER_OCEAN.add(Material.DEAD_BUBBLE_CORAL,1);
		FOOTER_OCEAN.add(Material.DEAD_FIRE_CORAL,1);
		FOOTER_OCEAN.add(Material.DEAD_BUBBLE_CORAL_FAN,1);
		FOOTER_OCEAN.add(Material.DEAD_BRAIN_CORAL_FAN,1);
		FOOTER_OCEAN.add(Material.DEAD_TUBE_CORAL_FAN,1);
		FOOTER_OCEAN.add(Material.DEAD_HORN_CORAL,1);
		FOOTER_OCEAN.add(Material.DEAD_FIRE_CORAL_FAN,1);
		FOOTER_OCEAN.add(Material.DEAD_HORN_CORAL_FAN,1);
		FOOTER_OCEAN.compileRangeMap();
		
		FOOTER_FLOWERBED.add(Material.ALLIUM,10);
		FOOTER_FLOWERBED.add(Material.BLUE_ORCHID,10);
		FOOTER_FLOWERBED.add(Material.POPPY,10);
		FOOTER_FLOWERBED.add(Material.LILAC,10);
		FOOTER_FLOWERBED.add(Material.CORNFLOWER,10);
		FOOTER_FLOWERBED.add(Material.DANDELION,10);
		FOOTER_FLOWERBED.add(Material.PINK_PETALS,10);
		FOOTER_FLOWERBED.add(Material.WITHER_ROSE,10);
		FOOTER_FLOWERBED.add(Material.TORCHFLOWER,10);
		FOOTER_FLOWERBED.add(Material.LILY_OF_THE_VALLEY,10);
		FOOTER_FLOWERBED.add(Material.PEONY,10);
		FOOTER_FLOWERBED.add(Material.ROSE_BUSH,10);
		FOOTER_FLOWERBED.add(Material.PINK_TULIP,10);
		FOOTER_FLOWERBED.add(Material.ORANGE_TULIP,10);
		FOOTER_FLOWERBED.add(Material.WHITE_TULIP,10);
		FOOTER_FLOWERBED.add(Material.RED_TULIP,10);
		FOOTER_FLOWERBED.add(Material.OXEYE_DAISY,10);
		FOOTER_FLOWERBED.add(Material.AZURE_BLUET,10);
		FOOTER_FLOWERBED.add(Material.LARGE_FERN,25);
		FOOTER_FLOWERBED.add(Material.FERN,25);
		FOOTER_FLOWERBED.add(Material.BROWN_MUSHROOM,2);
		FOOTER_FLOWERBED.add(Material.RED_MUSHROOM,2);
		FOOTER_FLOWERBED.compileRangeMap();
		
		FOOTER_NETHER.add(Material.CRIMSON_ROOTS,50);
		FOOTER_NETHER.add(Material.WARPED_ROOTS,50);
		FOOTER_NETHER.add(Material.CRIMSON_FUNGUS,25);
		FOOTER_NETHER.add(Material.WARPED_FUNGUS,25);
		FOOTER_NETHER.compileRangeMap();
		
		HEADER_CAVE.add(Material.HANGING_ROOTS,90);
		HEADER_CAVE.add(Material.POINTED_DRIPSTONE,10);
		HEADER_CAVE.compileRangeMap();
	}
	
	public static LinkedList<Material> footerGrasslands(int numOfSlots)
	{
		LinkedList<Material> footerDecor = new LinkedList<>();
		
		for(int i = 0; i < numOfSlots; i++)
		{
			footerDecor.add(FOOTER_GRASSLANDS.next());
		}
		
		return footerDecor;
	}
	
	public static LinkedList<Material> footerOcean(int numOfSlots)
	{
		LinkedList<Material> footerDecor = new LinkedList<>();
		
		for(int i = 0; i < numOfSlots; i++)
		{
			footerDecor.add(FOOTER_OCEAN.next());
		}
		
		return footerDecor;
	}
	
	public static LinkedList<Material> footerFlowerbed(int numOfSlots)
	{
		LinkedList<Material> footerDecor = new LinkedList<>();
		
		for(int i = 0; i < numOfSlots; i++)
		{
			footerDecor.add(FOOTER_FLOWERBED.next());
		}
		
		return footerDecor;
	}
	
	public static LinkedList<Material> footerNether(int numOfSlots)
	{
		LinkedList<Material> footerDecor = new LinkedList<>();
		
		for(int i = 0; i < numOfSlots; i++)
		{
			footerDecor.add(FOOTER_NETHER.next());
		}
		
		return footerDecor;
	}
	
	public static LinkedList<Material> headerCave(int numOfSlots)
	{
		LinkedList<Material> footerDecor = new LinkedList<>();
		
		for(int i = 0; i < numOfSlots; i++)
		{
			footerDecor.add(HEADER_CAVE.next());
		}
		
		return footerDecor;
	}
	
	/**
	 * Gets decor
	 * @param type Type of decor
	 * @param numOfSlots Number of slots the decor needs to fill
	 * @return List of decor materials
	 */
	public static LinkedList<Material> getDecor(DecorType type,int numOfSlots)
	{
		switch(type)
		{
			case FOOTER_GRASSLANDS: { return footerGrasslands(numOfSlots); }
			case FOOTER_OCEAN: { return footerOcean(numOfSlots); }
			case FOOTER_FLOWERBED: { return footerFlowerbed(numOfSlots); }
			case FOOTER_NETHER: { return footerNether(numOfSlots); }
			case HEADER_CAVE: { return headerCave(numOfSlots); }
		}
		
		Logg.warn("Decor type: " + type.toString() + " could not be found!");
		return new LinkedList<>();
	}
	
	/**
	 * Checks if a vanilla inventory template used is compatible for a type of decor
	 * @param template vanilla inventory template used
	 * @param decor Type of decoration to be applied to the GUI
	 * @return True if this type of decor can be applied, false otherwise
	 */
	public static boolean isCompatibleForDecor(InvTemplate template,DecorType decor)
	{
		if(!compatibleDecors.containsKey(template)) { return false; }
		return compatibleDecors.get(template).contains(decor);
	}
	
	public enum DecorType
	{
		FOOTER_GRASSLANDS(DecorLocation.FOOTER,ChaosRegion.Common.DECOR_FOOTER),
		FOOTER_OCEAN(DecorLocation.FOOTER,ChaosRegion.Common.DECOR_FOOTER),
		FOOTER_FLOWERBED(DecorLocation.FOOTER,ChaosRegion.Common.DECOR_FOOTER),
		FOOTER_NETHER(DecorLocation.FOOTER,ChaosRegion.Common.DECOR_FOOTER),
		HEADER_CAVE(DecorLocation.HEADER,ChaosRegion.Common.DECOR_HEADER);
		
		private DecorLocation loc;
		private String regionName;
		
		DecorType(DecorLocation location,String regionName)
		{
			this.loc = location;
			this.regionName = regionName;
		}
		
		public DecorLocation getLocation()
		{
			return loc;
		}
		
		public String getRegionName()
		{
			return regionName;
		}
	}
	
	public enum DecorLocation
	{
		HEADER,
		FOOTER
	}
}
