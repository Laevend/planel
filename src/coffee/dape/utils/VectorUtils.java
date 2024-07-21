package coffee.dape.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class VectorUtils
{
	public static Vector getDirectionBetweenLocations(Location start,Location end)
	{
		Vector from = start.toVector();
		Vector to = end.toVector();
		
		return to.subtract(from);
	}
	
	public static double getDistanceBetweenLocations(Location pointA,Location pointB)
	{
		double x1 = pointA.getX();
		double z1 = pointA.getZ();
		double x2 = pointB.getX();
		double z2 = pointB.getZ();
		
		return Math.sqrt((z2 - z1) * (z2 - z1) + (x2 - x1) * (x2 - x1));
	}
	
	public static Vector invert(Vector vec)
	{
		return vec.clone().setX(vec.getX()*-1).setY(vec.getY()*-1).setZ(vec.getZ()*-1);
	}
}
