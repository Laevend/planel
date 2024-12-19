package coffee.dape.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

import coffee.dape.utils.clocks.LimitedCyclesClock;

public class EntityUtils
{
	private static int transitionSteps = 30;
	private static final Map<UUID,ScaleTransitionClock> transitionClock = new HashMap<>();
	
	public static void setSize(LivingEntity livingEntity,double newScale)
	{
		final double currentScale = Objects.requireNonNull(livingEntity.getAttribute(Attribute.GENERIC_SCALE)).getBaseValue();
		
		if(currentScale == newScale) { return; }
		
		if(transitionClock.containsKey(livingEntity.getUniqueId()))
		{
			transitionClock.get(livingEntity.getUniqueId()).stop();
			transitionClock.remove(livingEntity.getUniqueId());
		}
		
		transitionClock.put(livingEntity.getUniqueId(),new ScaleTransitionClock(livingEntity,newScale,transitionSteps));
		
	    if(livingEntity.getAttribute(Attribute.GENERIC_JUMP_STRENGTH) != null)
	    {
	        livingEntity.getAttribute(Attribute.GENERIC_JUMP_STRENGTH).setBaseValue(getValidBase(0.41D,32,newScale));
	        livingEntity.getAttribute(Attribute.GENERIC_JUMP_STRENGTH).setBaseValue(Math.max(getValidBase(0.41D,32,newScale),0.2));
	    }
	    
		if(livingEntity.getAttribute(Attribute.PLAYER_BLOCK_INTERACTION_RANGE) != null)
		{
		    livingEntity.getAttribute(Attribute.PLAYER_BLOCK_INTERACTION_RANGE).setBaseValue(getValidBase(4.5D,64,newScale));
		}
		
		if(livingEntity.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE) != null)
		{
		    livingEntity.getAttribute(Attribute.PLAYER_ENTITY_INTERACTION_RANGE).setBaseValue(getValidBase(3D,64,newScale));
		}
		
		if(livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null)
		{
		    livingEntity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(Math.max(getValidBase(0.1D,1024,newScale),0.03));
		}
		
		if(livingEntity.getAttribute(Attribute.GENERIC_FLYING_SPEED) != null)
		{
		    livingEntity.getAttribute(Attribute.GENERIC_FLYING_SPEED).setBaseValue(Math.max(getValidBase(0.1D,1024,newScale),0.03));
		}
		
		if(livingEntity.getAttribute(Attribute.GENERIC_STEP_HEIGHT) != null)
		{
		    livingEntity.getAttribute(Attribute.GENERIC_STEP_HEIGHT).setBaseValue(getValidBase(0.6D,10,newScale));
		}
		
		if(livingEntity.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE) != null)
		{
		    livingEntity.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue(getValidBase(3.0D,1024,newScale));
		}
		
		if(livingEntity.getAttribute(Attribute.GENERIC_GRAVITY) != null)
		{
		    livingEntity.getAttribute(Attribute.GENERIC_GRAVITY).setBaseValue(Math.max(getValidBase(0.08D,1,newScale),-1));
		}
		
		if(livingEntity.getAttribute(Attribute.GENERIC_WATER_MOVEMENT_EFFICIENCY) != null)
		{
		    livingEntity.getAttribute(Attribute.GENERIC_WATER_MOVEMENT_EFFICIENCY).setBaseValue(Math.max(getValidBase(0.1D,1,newScale),0.03));
		}
    }
	
    private static double getValidBase(double defaultValue,double maxValue,double multiplier)
    {
		double finalVal = defaultValue * multiplier;
		if(finalVal > maxValue) finalVal = maxValue;
		if(finalVal < 0) finalVal = 0.01F;
		return finalVal;
    }
    
    protected static class ScaleTransitionClock extends LimitedCyclesClock
    {
    	private UUID uuid;
    	private LivingEntity le;
    	private double currentScale;
    	private double newScale;
    	private boolean bigger;
    	
		public ScaleTransitionClock(LivingEntity le,double newScale,int cycles)
		{
			super("ScaleTransitionClock for " + le.getName(),1,cycles);
			this.uuid = le.getUniqueId();
			this.le = le;
			this.currentScale = le.getAttribute(Attribute.GENERIC_SCALE).getBaseValue();
			this.bigger = newScale > currentScale;
			this.newScale = newScale;
			start();
		}

		@Override
		public void execute() throws Exception
		{
			double stepSize = Math.abs(newScale - currentScale) / transitionSteps;
		    AtomicReference<Double> scale = new AtomicReference<>(currentScale);
		    
		    scale.updateAndGet(v -> bigger ? v + stepSize : v - stepSize);
		    
	        if(scale.get() == currentScale) { return; }
	        
	        if((bigger && scale.get() >= newScale) || (!bigger && scale.get() <= newScale))
	        {
	            stop();
	            finalExecute();
	            return;
	        }
	        
	        if(le == null || le.isDead()) { stop(); transitionClock.remove(this.uuid); return; }
	        Objects.requireNonNull(le.getAttribute(Attribute.GENERIC_SCALE)).setBaseValue(MathUtils.clamp(0.0625d,16d,scale.get()));
		}

		@Override
		public void finalExecute() throws Exception
		{
			if(le == null || le.isDead()) { stop(); transitionClock.remove(this.uuid); return; }
			if(le.getAttribute(Attribute.GENERIC_SCALE) != null) { le.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(MathUtils.clamp(0.0625d,16d,newScale)); }
			transitionClock.remove(this.uuid);
		}
    }
}
