package coffee.dape.utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import coffee.dape.Dape;

public class ParticleUtils
{
	/**
	 * Create a particle spawn
	 * @param interval How often to refresh the particle
	 * @param loc Location of where particle will spawn
	 * @param count Number of particles to spawn
	 * @return 
	 */
	public static ParticleSpawn createSpawn(long interval,Particle part,Location loc,int count)
	{
		return createSpawn(interval,part,loc,count,1.0d,0.0d,0.0d,0.0d);
	}
	
	/**
	 * Create a particle spawn
	 * @param interval How often to refresh the particle
	 * @param loc Location of where particle will spawn
	 * @param count Number of particles to spawn
	 * @param extra The extra data for this particle, depends on the particle used (normally speed)
	 * @return 
	 */
	public static ParticleSpawn createSpawn(long interval,Particle part,Location loc,int count,double extra)
	{
		return createSpawn(interval,part,loc,count,extra,0.0d,0.0d,0.0d);
	}
	
	/**
	 * Create a particle spawn
	 * @param interval How often to refresh the particle
	 * @param loc Location of where particle will spawn
	 * @param count Number of particles to spawn
	 * @param extra The extra data for this particle, depends on the particle used (normally speed)
	 * @param offsetX the maximum random offset on the X axis
	 * @param offsetY the maximum random offset on the Y axis
	 * @param offsetZ the maximum random offset on the Z axis
	 * @return 
	 */
	public static ParticleSpawn createSpawn(long interval,Particle part,Location loc,int count,double extra,double offsetX,double offsetY,double offsetZ)
	{
		return new ParticleSpawn(interval,part,loc,count,extra,offsetX,offsetY,offsetZ);
	}
	
	/**
	 * Creates a circle of particles
	 * @param particle Particle type
	 * @param e Entity who's location will be used as the centre point
	 * @param radius Radius of the spiral from the centre
	 */
	public static void circle(Particle particle,Entity e,double radius)
	{
		circle(particle,e.getLocation(),radius,360,0f);
	}
	
	/**
	 * Creates a circle of particles
	 * @param particle Particle type
	 * @param e Entity who's location will be used as the centre point
	 * @param radius Radius of the spiral from the centre
	 * @param frequency Number of particles to be used in one 360 degree rotation (360 max, 2 min)
	 */
	public static void circle(Particle particle,Entity e,double radius,int frequency)
	{
		circle(particle,e.getLocation(),radius,frequency,0f);
	}
	
	/**
	 * Creates a circle of particles
	 * @param particle Particle type
	 * @param centre Centre of the spiral
	 * @param radius Radius of the spiral from the centre
	 */
	public static void circle(Particle particle,Location centre,double radius)
	{
		circle(particle,centre,radius,360,0f);
	}
	
	/**
	 * Creates a circle of particles
	 * @param particle Particle type
	 * @param e Entity who's location will be used as the centre point
	 * @param radius Radius of the spiral from the centre
	 * @param speed Speed of particles movement
	 */
	public static void circle(Particle particle,Entity e,double radius,float speed)
	{
		circle(particle,e.getLocation(),radius,360,speed);
	}
	
	/**
	 * Creates a circle of particles
	 * @param particle Particle type
	 * @param e Entity who's location will be used as the centre point
	 * @param radius Radius of the spiral from the centre
	 * @param frequency Number of particles to be used in one 360 degree rotation (360 max, 2 min)
	 * @param speed Speed of particles movement
	 */
	public static void circle(Particle particle,Entity e,double radius,int frequency,float speed)
	{
		circle(particle,e.getLocation(),radius,frequency,speed);
	}
	
	/**
	 * Creates a circle of particles
	 * @param particle Particle type
	 * @param centre Centre of the spiral
	 * @param radius Radius of the spiral from the centre
	 * @param speed Speed of particles movement
	 */
	public static void circle(Particle particle,Location centre,double radius,float speed)
	{
		circle(particle,centre,radius,360,speed);
	}
	
	/**
	 * Creates a circle of particles
	 * @param particle Particle type
	 * @param centre Centre of the spiral
	 * @param radius Radius of the spiral from the centre
	 * @param frequency Number of particles to be used in one 360 degree rotation (360 max, 2 min)
	 * @param speed Speed of particles movement
	 */
	public static void circle(Particle particle,Location centre,double radius,int frequency,float speed)
	{
		if(frequency > 360) { frequency = 360; }
		if(frequency < 2) {  frequency = 2; }
		
		double particleSpacing = 360 / frequency;
		
		for(double degree = 0; degree < 360; degree+= particleSpacing)
		{
		    double radians = Math.toRadians(degree);
		    double x = Math.cos(radians) * radius;
		    double z = Math.sin(radians) * radius;
		    centre.add(x,0,z);
		    centre.getWorld().spawnParticle(particle,centre,1,0,0,0,speed);
		    centre.subtract(x,0,z);
		}
	}
	
	/**
	 * Creates a spiral of particles
	 * @param particle Particle type
	 * @param centre Centre of the spiral
	 * @param radius Radius of the spiral from the centre
	 * @param frequency Number of particles to be used in one 360 degree rotation (360 max, 2 min)
	 * @param spiralUp If the spiral will spawn particles in a way that shows it spiralling up, false for spiral down
	 * @param height Maximum height to spiral to on the Y axis
	 * @param startingDegree The angle to start spawning particles at
	 */
	public static void circleSpiral(Particle particle,Location centre,double radius,int frequency,boolean spiralDown,double height,double startingDegree)
	{
		if(frequency > 360) { frequency = 360; }
		if(frequency < 2) {  frequency = 2; }
		
		double particleSpacing = 360 / frequency;
		int yDeltaMultiplier = 1;
		double yDelta = height / frequency;
		
		if(spiralDown)
		{
			for(double degree = startingDegree; degree < (startingDegree + 360); degree+= particleSpacing)
			{
				double radians = Math.toRadians(degree);
			    double x = Math.cos(radians) * radius;
			    double z = Math.sin(radians) * radius;
			    double y = yDelta * yDeltaMultiplier;
			    centre.add(x,y,z);
			    centre.getWorld().spawnParticle(particle,centre,1,0,0,0,0);
			    centre.subtract(x,y,z);
			    yDeltaMultiplier++;
			}
		}
		else
		{
			for(double degree = (startingDegree + 360); degree > startingDegree; degree-= particleSpacing)
			{
				double radians = Math.toRadians(degree);
			    double x = Math.cos(radians) * radius;
			    double z = Math.sin(radians) * radius;
			    double y = yDelta * yDeltaMultiplier;
			    centre.add(x,y,z);
			    centre.getWorld().spawnParticle(particle,centre,1,0,0,0,0);
			    centre.subtract(x,y,z);
			    yDeltaMultiplier++;
			}
		}
	}
	
	/**
	 * Creates a spiral of particles that spin around the player
	 * @param particle Particle type
	 * @param centre Centre of the spiral
	 * @param radius Radius of the spiral from the centre
	 * @param frequency Number of particles to be used in one 360 degree rotation (360 max, 2 min)
	 * @param clockwise If the spiral will spawn in a clockwise direction
	 * @param spiralDown If the spiral will spawn particles in a way that shows it spiralling down, false for spiral up
	 * @param height Maximum height to spiral to on the Y axis
	 * @param speedMultiplier How fast the spiral will spin, 1.0 being normal 2.0 being 2x faster (0.1 min)
	 * @param tickDuration Duration of how long this spiral spin will last
	 * @param degreesBetweenEachSpiral Angle of degrees distance between each spiral when spawned
	 * @return 
	 */
	public static BukkitTask circleSpiralSpin(Particle particle,Location centre,double radius,int frequency,boolean clockwise,boolean spiralDown,double height,double speedMultiplier,int tickDuration,double degreesBetweenEachSpiral)
	{
		if(speedMultiplier < 0) { speedMultiplier = 1.0; }
		if(tickDuration < 0) { tickDuration = 1; }
		
		int maxDuration = tickDuration;
		double speed = speedMultiplier;
		
		return new BukkitRunnable()
		{
			double degree = 0;
			int currentDuration = 0;
			
			@Override
			public void run() 
		    {
				if(currentDuration < maxDuration) { currentDuration++; } else { cancel(); }
				
				if(clockwise)
		    	{
			    	if(degree < 360) { degree+= (degreesBetweenEachSpiral * speed); } else { degree = 0; } 
			    	
			    	circleSpiral(particle,centre,radius,frequency,spiralDown,height,degree);
		    	}
		    	else
		    	{
			    	if(degree > 0) { degree-= (degreesBetweenEachSpiral * speed); } else { degree = 360; } 
			    	
			    	circleSpiral(particle,centre,radius,frequency,!spiralDown,height,degree);
		    	}
		    }
		}.runTaskTimer(Dape.instance(),0L,1L);
	}
	
	/**
	 * Creates a line of particles
	 * @param start Starting location to start spawning particles from
	 * @param direction Direction of where the line is going
	 * @param lineLength How long the line is
	 * @param space Space in between each particle
	 */
	public static void line(Location start,Vector direction,double lineLength,double space,Particle particle)
	{
		for(double i = 1; i <= lineLength; i += space)
		{
			direction.multiply(i);
		    start.add(direction);
		    start.getWorld().spawnParticle(particle,start,1,0,0,0,0);
		    start.subtract(direction);
		    direction.normalize();
		}
	}
	
	/**
	 * Creates a line of particles between two points
	 * @param point1 Starting location to start spawning particles from
	 * @param direction Direction of where the line is going
	 * @param lineLength How long the line is
	 * @param space Space in between each particle
	 * @param particle Type of particle to use
	 */
	public static void line(Location start,Location end,double space,Particle particle)
	{
		Vector direction = VectorUtils.getDirectionBetweenLocations(start,end);
		
		for(double i = 1; i <= start.distance(end); i += space)
		{
			direction.multiply(i);
		    start.add(direction);
		    start.getWorld().spawnParticle(particle,start,1,0,0,0,0);
		    start.subtract(direction);
		    direction.normalize();
		}
	}
	
	/**
	 * Pulses a particle at a location
	 * @param loc Location to spawn particles from
	 * @param particle Type of particle to use
	 * @param count number of particles to pulse
	 */
	public static void pulse(Location loc,Particle particle,int count,float speed)
	{
		loc.getWorld().spawnParticle(particle,loc,count,0,0,0,speed);
	}
	
	/**
	 * Pulses a particle at a location
	 * @param loc Location to spawn particles from
	 * @param particle Type of particle to use
	 * @param count number of particles to pulse
	 */
	public static void pulse(Location loc,Particle particle,int count,float speed,double offsetX,double offsetY,double offsetZ)
	{
		loc.getWorld().spawnParticle(particle,loc,count,offsetX,offsetY,offsetZ,speed);
	}
}
