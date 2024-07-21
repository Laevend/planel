package coffee.dape.utils;

import org.bukkit.Location;
import org.bukkit.Particle;

import coffee.dape.utils.clocks.RepeatingClock;

public class ParticleSpawn extends RepeatingClock
{
	private Location location;
	private Particle particle;
	private int count = 10;
	private double extra = 0.1d;
	private double offsetX = 0.0d;
	private double offsetY = 0.0d;
	private double offsetZ = 0.0d;
	
	/**
	 * Create a particle spawn
	 * @param interval How often to refresh the particle
	 * @param isInSeconds if the interval argument is in seconds. True = is in seconds, False = is in ticks
	 * @param loc Location of where particle will spawn
	 * @param count Number of particles to spawn
	 * @param extra The extra data for this particle, depends on the particle used (normally speed)
	 */
	protected ParticleSpawn(long interval,Particle part,Location loc,int count,double extra)
	{
		this(interval,part,loc,count,extra,0.0d,0.0d,0.0d);
	}
	
	/**
	 * Create a particle spawn
	 * @param interval How often to refresh the particle
	 * @param isInSeconds if the interval argument is in seconds. True = is in seconds, False = is in ticks
	 * @param loc Location of where particle will spawn
	 * @param count Number of particles to spawn
	 * @param extra The extra data for this particle, depends on the particle used (normally speed)
	 * @param offsetX the maximum random offset on the X axis
	 * @param offsetY the maximum random offset on the Y axis
	 * @param offsetZ the maximum random offset on the Z axis
	 */
	protected ParticleSpawn(long interval,Particle part,Location loc,int count,double extra,double offsetX,double offsetY,double offsetZ)
	{
		super("Particle Clock",interval);
		this.particle = part;
		this.location = loc;
		this.count = count;
		this.extra = extra;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
	}

	@Override
	public void execute() throws Exception
	{
		this.location.getWorld().spawnParticle(this.particle,this.location,this.count,this.offsetX,this.offsetY,this.offsetZ,this.extra);
	}
}