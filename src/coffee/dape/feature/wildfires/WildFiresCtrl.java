package coffee.dape.feature.wildfires;

import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import coffee.dape.utils.BlockUtils;
import coffee.dape.utils.BlockUtils.FastBlockSetSession;
import coffee.dape.utils.Logg;
import coffee.dape.utils.MathUtils;
import coffee.dape.utils.ParticleUtils;
import coffee.dape.utils.RNG;

public class WildFiresCtrl implements Listener
{
	private int spreadDistance;
	private FastBlockSetSession session = new FastBlockSetSession();
	
	@EventHandler
	public void onIgnite(BlockIgniteEvent e)
	{
		if(e.getCause() == IgniteCause.SPREAD)
		{
			spreadDistance = MathUtils.getRandom(4,16);
			//Logg.verb("Spread!");
			flashSpread(e.getBlock());
			spit(e.getBlock());
			return;
		}
	}
	
	/**
	 * Creates a spit (fire will sometimes spit and cause blocks further away from it to catch fire)
	 * @param block Block to spit from
	 */
	private void spit(Block block)
	{
		if(!RNG.roll(0.05f)) { return; }
		
		int spits = MathUtils.getRandom(8,16);
		ParticleUtils.pulse(block.getLocation().add(0.5,0,0.5),Particle.LAVA,10,0.1f);
		
		for(int i = 0; i < spits; i++)
		{
			Vector direction = new Vector(MathUtils.getRandom(-1d,1d),MathUtils.getRandom(0.3d,0.9d),MathUtils.getRandom(-1d,1d));
			Location above = block.getLocation().clone().add(direction.clone().normalize().multiply(5));
			Vector newDirection = new Vector(direction.getX(),direction.getY() *-1,direction.getZ());
			RayTraceResult result = block.getWorld().rayTraceBlocks(above,newDirection,MathUtils.getRandom(10,16),FluidCollisionMode.NEVER,true);
			
//			BlockDisplay bd = (BlockDisplay) block.getWorld().spawnEntity(above,EntityType.BLOCK_DISPLAY);
//			bd.setBlock(Material.MAGMA_BLOCK.createBlockData());
//			
//			DelayUtils.executeDelayedTask(() -> bd.remove(),40);
			
			if(result == null) { continue; }
			if(result.getHitBlockFace() == null) { continue; }
			
			igniteBlocks(result.getHitBlock());
		}
	}
	
	/**
	 * Creates a flash spread of fire by jumping to an adjacent blocks a number of times and attempting to burn each block and/or its surrounding blocks.
	 * <p>Triggered by natural fire spread.</p>
	 * @param block Block to jump from
	 */
	private void flashSpread(Block block)
	{
		if(spreadDistance <= 0) { return; }
		spreadDistance--;
		
		// Get random direction;
		BlockFace direction = MathUtils.getRandom(List.of(BlockFace.NORTH,BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST,BlockFace.UP,BlockFace.DOWN));
		Block newBlock = block.getRelative(direction);
		
		igniteBlocks(newBlock);
		flashSpread(newBlock);
	}
	
	/**
	 * Ignites this block or surrounding blocks if burnable
	 * @param block Block to ignite or adjacent blocks to this block if burnable
	 */
	private void igniteBlocks(Block block)
	{
		// Default block data for fire
		String blockData = Material.FIRE.createBlockData().getAsString();
		
		// If the new block chosen is a burnable block, attempt to set fire to all exposed surfaces of it
		if(block.getType().isBurnable())
		{
			for(Entry<BlockFace,Block> relativeBlockEntry : BlockUtils.getAdjacentBlocksMap(block).entrySet())
			{
				// Adjacent block is burnable while the 'newBlock' is air
				if(relativeBlockEntry.getValue().getType().isAir())
				{
					String newblockData = modifyFireBlockDataNeighbour(new String(blockData),relativeBlockEntry.getKey());
					relativeBlockEntry.getValue().setBlockData(Bukkit.createBlockData(newblockData));
					continue;
				}
				// Modify fire data to make the 'relativeBlockEntry' side of the block burn
				else if(relativeBlockEntry.getValue().getType() == Material.FIRE)
				{
					String newblockData = relativeBlockEntry.getValue().getBlockData().getAsString();
					newblockData = modifyFireBlockDataNeighbour(newblockData,relativeBlockEntry.getKey());
					relativeBlockEntry.getValue().setBlockData(Bukkit.createBlockData(newblockData));
					continue;
				}
			}
			
			return;
		}
		
		// No reason to attempt to burn if the new block is not burnable and is not air or already fire
		if(!block.getType().isAir() || block.getType() != Material.FIRE) { return; }
		
		// New block is already on fire, so we need the block data from it to modify the fire spread
		if(block.getType() == Material.FIRE)
		{
			blockData = block.getBlockData().getAsString();
		}
		
		// If new block is air or fire, modify block data so that any adjacent block faces that point to a burnable block start burning
		for(Entry<BlockFace,Block> relativeBlockEntry : BlockUtils.getAdjacentBlocksMap(block).entrySet())
		{
			// Adjacent block is air while the 'newBlock' is burnable
			if(relativeBlockEntry.getValue().getType().isBurnable())
			{
				blockData = modifyFireBlockData(blockData,relativeBlockEntry.getKey());
			}
		}
		
		block.setBlockData(Bukkit.createBlockData(blockData));
	}
	
	private String modifyFireBlockData(String blockData,BlockFace face)
	{
		switch(face)
		{
			case NORTH -> blockData = blockData.replace("north=false","north=true");
			case EAST -> blockData = blockData.replace("east=false","east=true");
			case SOUTH -> blockData = blockData.replace("south=false","south=true");
			case WEST -> blockData = blockData.replace("west=false","west=true");
			case UP -> blockData = blockData.replace("up=false","up=true");
			default -> { /* No changes to data needed */ }
		}
		
		return blockData;
	}
	
	private String modifyFireBlockDataNeighbour(String blockData,BlockFace face)
	{
		switch(face)
		{
			case NORTH -> blockData = blockData.replace("south=false","south=true");
			case EAST -> blockData = blockData.replace("west=false","west=true");
			case SOUTH -> blockData = blockData.replace("north=false","north=true");
			case WEST -> blockData = blockData.replace("east=false","east=true");
			case DOWN -> blockData = blockData.replace("up=false","up=true");
			default -> { /* No changes to data needed */ }
		}
		
		return blockData;
	}
}

