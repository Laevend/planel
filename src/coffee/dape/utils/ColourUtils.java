package coffee.dape.utils;

import java.awt.Color;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class ColourUtils
{	
	// The colour code char used to represent a colour code in the following character
	public static final char COLOUR_CODE_CHARACTER = '&';
	
	/**
	 * Translates '&' symbols to section symbol/section sign
	 * @param s String to format
	 * @return String with colour code symbols translated
	 */
	public static String transCol(String s)
	{
		return ChatColor.translateAlternateColorCodes(COLOUR_CODE_CHARACTER,s);
	}
	
	/**
	 * Returns a random colour
	 * @return random colour
	 */
	public static Color getRandomColour()
	{
		return new Color(MathUtils.getRandom(0,255),MathUtils.getRandom(0,255),MathUtils.getRandom(0,255));
	}
	
	/**
	 * Converts a hexadecimal colour string to RGB
	 * @param hexColour hexadecimal colour string
	 * @return integer array with a length of 3
	 */
	public static int[] hexToRgb(String hexColour)
	{
		if(!hexColour.matches("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$")) { Logg.error(hexColour + " is not a hex colour!"); return null; }
		
		Color col = new Color(Integer.valueOf(hexColour.substring(1,3),16),Integer.valueOf(hexColour.substring(3,5),16),Integer.valueOf(hexColour.substring(5,7),16));
	    int[] rgb = new int[3];
	    rgb[0] = col.getRed();
	    rgb[1] = col.getGreen();
	    rgb[2] = col.getBlue();
	    
	    return rgb;
	}
	
	/**
	 * Converts red, green, and blue values to a hexadecimal colour string
	 * @param red 1-255 of red
	 * @param green 1-255 of green
	 * @param blue 1-255 of blue
	 * @return hexadecimal colour string
	 */
	public static String rgbToHex(int red,int green,int blue)
	{
		if(red < 0 || red > 255) { Logg.error("Red is not within the rang of 0-255!"); return null; }
		if(green < 0 || green > 255) { Logg.error("Green is not within the rang of 0-255!"); return null; }
		if(blue < 0 || blue > 255) { Logg.error("Blue is not within the rang of 0-255!"); return null; }
		
		return Integer.toHexString(new Color(red,green,blue).getRGB());
	}
	
	/**
	 * Converts a hexadecimal colour string to a colour object
	 * @param hexColour hexadecimal colour string
	 * @return Colour object
	 */
	public static Color hexToColour(String hexColour)
	{
		if(!hexColour.matches("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$")) { Logg.error(hexColour + " is not a hex colour!"); return null; }
		
		return new Color(Integer.valueOf(hexColour.substring(1,3),16),Integer.valueOf(hexColour.substring(3,5),16),Integer.valueOf(hexColour.substring(5,7),16));
	}
	
	/**
	 * Converts red, green, and blue values to a colour object
	 * @param red 1-255 of red
	 * @param green 1-255 of green
	 * @param blue 1-255 of blue
	 * @return Colour object
	 */
	public static Color rgbToColour(int red,int green,int blue)
	{
		return new Color(red,green,blue);
	}
	
	/**
	 * Mixes two colours
	 * @param color1 Colour 1 to mix
	 * @param color2 Colour 2 to mix
	 * @param percent The percent mix rate, 0.0 being 0% and 1.0 being 100%
	 * @return Mixed colour
	 */
	public static Color mixColors(Color col1,Color col2,double percent)
	{
		double inverse_percent = 1.0 - percent;
		int redPart = (int) (col1.getRed()*percent + col2.getRed()*inverse_percent);
		int greenPart = (int) (col1.getGreen()*percent + col2.getGreen()*inverse_percent);
		int bluePart = (int) (col1.getBlue()*percent + col2.getBlue()*inverse_percent);
		
		Logg.verb("NewCol: " + redPart + " " + greenPart + " " + bluePart,Logg.VerbGroup.COLOUR_UTILS);
		
		return new Color(redPart, greenPart, bluePart);
	}
	
	/**
	 * Converts a Awt colour object to a Bukkit colour object
	 * @param col Awt colour object
	 * @return Bukkit colour
	 */
	public static org.bukkit.Color toBukkitColour(Color col)
	{
		return org.bukkit.Color.fromRGB(col.getRed(),col.getGreen(),col.getBlue());
	}
	
	/**
	 * Converts a Bukkit colour object to a AWT colour object
	 * @param col Bukkit colour object
	 * @return Awt colour object
	 */
	public static Color toAwtColour(org.bukkit.Color col)
	{
		return new Color(col.getRed(),col.getGreen(),col.getBlue());
	}
	
	/**
	 * Applies a linear gradient to a string
	 * @param string String to apply the gradient to
	 * @param grad LinearGradient
	 * @return String with gradient applied
	 */
	public static String applyRandomColoursToString(String string)
	{
		StringBuilder sb = new StringBuilder();
		char[] charArray = string.toCharArray();
		
		for(int i = 0; i < charArray.length; i++)
		{
			if(charArray[i] == ((char) 32))
			{
				sb.append(" ");
				continue;
			}
			
			sb.append(ChatColor.of(getRandomColour()) + String.valueOf(charArray[i]));
		}
		
		return sb.toString();
	}
	
	public static String applyColour(String string,Color colour)
	{
		return ChatColor.of(colour) + string;
	}
	
	public static String applyColour(String string,String hex)
	{
		return ChatColor.of(hexToColour(hex)) + string;
	}
	
	private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)(?:" + StringUtils.AND_SIGN + "|" + StringUtils.SECTION_SIGN + ")[0-9A-FK-ORX]");
	
	/**
	 * Strips colour codes from the string (Better than ChatColor.stripColor() as we also strip colour codes using & sign)
	 * @param input String to strip colour codes from
	 * @return String without colour
	 */
	public static String strip(String input)
	{
		return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
	}
	
	public static final Color LOGO = hexToColour("#af0e4b");
	
	public static final Color TEXT = hexToColour("#8e9eab");
	public static final Color TEXT_ERROR = hexToColour("#ff512f");
	public static final Color TEXT_WARNING = hexToColour("#e8ff1c");
	public static final Color TEXT_SUCCESS = hexToColour("#26edb1");
	
	public static final Color DARK_WHITE = hexToColour("#c4e0e5");
	public static final Color LIGHT_GREEN = hexToColour("#9EE493");
	public static final Color RUSTY_RED = hexToColour("#D64550");
	public static final Color COLUMBIA_BLUE = hexToColour("#BFDBF7");
	public static final Color TEAL = hexToColour("#1F7A8C");
	public static final Color ROJO = hexToColour("#DB222A");
	public static final Color MELON = hexToColour("#FBC3BC");
	public static final Color MELON_DARKER = hexToColour("#F7A399");
	public static final Color JUNGLE_GREEN = hexToColour("#49A078");	
	public static final Color VISTA_BLUE = hexToColour("#7699D4");
}