package coffee.dape.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Laeven
 *
 */
public class StringUtils
{
	private static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	private static DateFormat dateFileNameFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
	private static Calendar cal;
	private static Map<Character,String> upsideDownCharacterMap = new HashMap<>();
	
	/**
	 * Returns a string that represents this date
	 * @param date Date
	 * @return String representation of this date
	 */
	public static String dateToString(Date date)
	{
		return dateFormat.format(date);
	}
	
	/**
	 * Returns a Date object expressed by the formatted date string
	 * @param date Formatted string date
	 * @return Date
	 */
	public static Date stringToDate(String date)
	{
		try
		{
			return dateFormat.parse(date);
		}
		catch (ParseException e)
		{
			Logg.error("Date Parsing failed!",e);
		}
		
		return null;
	}
	
	/**
	 * Removes colour codes from a string.
	 * Returns the string without any colour codes
	 * @param stringWithColourCodes The string containing colour codes
	 * @return A string free of colour codes
	 */
	public static String removeColourFromString(String stringWithColourCodes)
	{
		StringBuilder sb = new StringBuilder();
		
		Logg.verb("String before colour code remover: " + stringWithColourCodes);
		
		for(int i = 0; i < stringWithColourCodes.length(); i++)
		{
			// &, section sign symbol
			if(stringWithColourCodes.charAt(i) != ((char) 38) && stringWithColourCodes.charAt(i) != ((char) 167))
			{
				sb.append(stringWithColourCodes.charAt(i));
			}
			else
			{
				i++;
			}
		}
		
		Logg.verb("String colour code remover produced: " + sb.toString());
		
		return sb.toString();
	}
	
	/**
	 * Replaces the section sign symbol to an and symbol (&)
	 * This is done to ensure compatibility across OS's
	 * @param list A list of strings containing section symbols
	 * @return The same list with all section symbols converted to and symbols
	 */
	public static List<String> replaceColourSymbol(List<String> list)
	{
		List<String> stringWithAndSymbols = new ArrayList<>();
		
		list.forEach(v ->
		{
			stringWithAndSymbols.add(v.replace((char) 167,(char) 38));
		});
		
		return stringWithAndSymbols;
	}
	
	/**
	 * Replaces the section sign symbol to an and symbol (&)
	 * This is done to ensure compatibility across OS's
	 * @param string A string containing section symbols
	 * @return The same string with all section symbols converted to and symbols
	 */
	public static String replaceColourSymbol(String string)
	{
		return string.replace((char) 167,(char) 38);
	}
	
	/**
	 * Replaces all underscores in a string with spaces
	 * @param s String
	 * @return
	 */
	public static String underscoresToSpaces(String s)
	{		
		return s.replaceAll("_"," ");
	}
	
	/**
	 * Replaces all spaces in a string with underscores
	 * @param s String
	 * @return
	 */
	public static String spacesToUnderscores(String s)
	{
		return s.replaceAll(" ","_");
	}
	
	/**
	 * Takes a string array and returns a string with
	 * the contents of the string array with each value
	 * comma separated
	 * @param s String array
	 * @return CSV String
	 */
	public static String arrayToCSV(String[] s)
	{
		StringBuilder sb = new StringBuilder();
		
		for(String value : s)
		{
			sb.append(value + ", ");
		}
		
		return sb.toString().substring(0,sb.toString().length() - 2);
	}
	
	/**
	 * Takes a string list and returns a string with
	 * the contents of the string array with each value
	 * comma separated
	 * @param s String array
	 * @return CSV String
	 */
	public static String arrayToCSV(List<String> s)
	{
		StringBuilder sb = new StringBuilder();
		
		for(String value : s)
		{
			sb.append(value + ", ");
		}
		
		return sb.toString().substring(0,sb.toString().length() - 2);
	}
	
	/**
	 * Takes a string array and returns a string with
	 * spaces in-between each array value
	 * @param s String array
	 * @return String
	 */
	public static String arrayToString(String[] s)
	{
		StringBuilder sb = new StringBuilder();
		
		for(String value : s)
		{
			sb.append(value + " ");
		}
		
		return sb.toString().substring(0,sb.toString().length() - 1);
	}
	
	/**
	 * Checks that the string is not null and that it
	 * has a length of at least 1.
	 * In other words, it checks that the string argument
	 * past contains something other than just being empty.
	 * @param s The string
	 * @return true/false if this string contains at least 1 character and is not null
	 */
	public static boolean hasContents(String s)
	{
		if(s == null) { return false; }
		
		if(s.isEmpty()) { return false; }
		
		return true;
	}
	
	public static boolean isNotNullEmptyOrBlank(String s)
	{
		return s != null && !s.isEmpty() && !s.isBlank();
	}
	
	/**
	 * Gets a date represented as a string
	 * Example: 20-8-2020-1-56-54-755
	 * Day-Month-Year-Hour-Minute-Second-Millisecond
	 * @return Date string
	 */
	public static String getDateAsString()
	{
		cal = Calendar.getInstance();
		String fileName = dateFileNameFormat.format(cal.getTime()) + "-" + cal.get(Calendar.MILLISECOND);
		return fileName;
	}
	
	/**
	 * Capitalises the first letter of the string
	 * @param s String
	 * @return String with first letter capitalised
	 */
	public static String capitaliseFirstLetter(String s)
	{
		String newString = String.valueOf(s.charAt(0)).toUpperCase();
		newString = newString + s.substring(1,s.length());
		return newString;
	}
	
	/**
	 * Formats a syntax string by turning anything with <> or [] into
	 * its respected colours gold and gray (Used for the /help command)
	 * @param syntax Syntax string
	 * @return formatted syntax string
	 */
	public static String formatSyntaxString(String syntax)
	{
		StringBuilder sb = new StringBuilder();
		boolean colourChange = false;
		boolean changeBack = false;
		
		String[] parts = syntax.split(" ");
		
		sb.append("&3");
		
		for(String part : parts)
		{
			if(part.startsWith("<"))
			{
				sb.append("&9");
				colourChange = true;
				changeBack = false;
			}
			else if(part.startsWith("["))
			{
				sb.append("&b");
				colourChange = true;
				changeBack = false;
			}
			else if(colourChange && (part.endsWith(">") || part.endsWith("]")))
			{
				colourChange = false;
				changeBack = true;
			}
			else if(changeBack)
			{
				sb.append("&3");
				changeBack = false;
			}
			
			sb.append(part);
			sb.append(" ");
		}
		
		return sb.toString().substring(0,sb.toString().length() - 1);
	}
	
	/**
	 * Creates a new string name Id
	 * 
	 * <p>Just like how discord account names have text followed by a set of numbers,
	 * This method will take a name and generate a string of numbers on the end to make
	 * it somewhat unique. 
	 * @param name String part of the ID
	 * @param currentNames Set of all currently in use name IDs
	 * @return new name ID
	 */
	public static String createNameID(String name_,Set<String> currentNames)
	{
		String name;
		
		if(name_.contains(" "))
		{
			name = name_.replace(" ","_");
		}
		else
		{
			name = name_;
		}
		
		String newNameID = name + "-" + MathUtils.getRandomIntString(4);
		
		while(currentNames.contains(newNameID))
		{
			newNameID = name + "-" + MathUtils.getRandomIntString(4);
		}
		
		return newNameID;
	}
	
	public static String toSnakecase(String s)
	{
		return s.toLowerCase().replaceAll("\\s+","_");
	}
	
	/**
	 * Formats a lower case string with underscores by replacing underscores with spaces and capitalising every first letter
	 * @param s String to format
	 * @return formatted string
	 */
	public static String formatSnakecase(String s)
	{
		String[] matNameParts = s.toString().toLowerCase().split("_");
		StringBuilder sb = new StringBuilder();
		
		for(String part : matNameParts)
		{
			sb.append(((char) (part.charAt(0) - 32)) + part.substring(1,part.length()) + " ");
		}
		
		String matName = sb.toString();
		return matName.substring(0,matName.length() - 1);
	}
	
	private static final String[] romanLetters = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"}; 
	private static final int[] values = {1000,900,500,400,100,90,50,40,10,9,5,4,1};  
	
	public static String numberToRomanNumerals(int number)
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < values.length; i++)
		{
			while(number >= values[i])
			{
				number = number - values[i];
				sb.append(romanLetters[i]);
			}
		}
		
		return sb.toString();
	}
	
	/**
     * Constructs lines of lore using a single line of lore. Each line length is specified by numberOfCharactersPerLine
     * @param loreList Existing lore list
     * @param numberOfCharactersPerLine Maximum number of characters that should be displayed in each line of lore
     * @param overflow If complete words should have the exception to overflow instead of being added to the next line
     * @param loreString The single line of lore to be divided up into multiple lines
     * @return ItemBuilder
     */  
	public static List<String> constructLines(List<String> loreList,int numberOfCharactersPerLine,boolean overflow,String loreString)
    {
    	StringBuilder colourBuffer = new StringBuilder();
    	StringBuilder finalLoreLineBuffer = new StringBuilder();
    	
    	char[] loreCharArray = loreString.toCharArray();
    	
    	// index of last space in loreLineBuffer
    	int lastSpaceCharIndex = 0;
    	
    	// if we're collecting colour codes
    	boolean isCollectingColour = false;
    	
    	// colour code offset to have the colour code characters be not included in the numberOfCharactersPerLine
    	int colourLengthOffset = 0;
    	
    	for(int i = 0; i < loreCharArray.length; i++)
    	{
    		// Get next character
    		char c = loreCharArray[i];
    		
    		// If character is & of colour symbol then we're collecting a colour
    		if((c == ((char) 38) || (c == ((char) 167))) && ((i + 1) < loreCharArray.length))
    		{
    			// If we're not already collecting colours
    			if(!isCollectingColour)
    			{    				
    				// Empty colour buffer as we're about to fill it with new colours
    				colourBuffer.setLength(0);
        			isCollectingColour = true;
    			}
    			
    			// add another colour to buffer
    			colourBuffer.append(c);
    			colourBuffer.append(loreCharArray[i + 1]);
    			i++;
    			continue;
    		}
    		else if(isCollectingColour)
    		{
    			// If we were collecting colour, disable
    			isCollectingColour = false;
    			finalLoreLineBuffer.append(colourBuffer.toString());
				colourLengthOffset += colourBuffer.length();
    		}
    		
    		// Add another character as long as it's less than numberOfCharactersPerLine
    		if((finalLoreLineBuffer.length() - colourLengthOffset) <= numberOfCharactersPerLine)
    		{    			
    			finalLoreLineBuffer.append(c);
    			
    			if(c == ((char) 32))
    			{
    				lastSpaceCharIndex = finalLoreLineBuffer.length() - 1;
    			}
    		}
    		// If overflow is allowed, the current word will be completed before a line return
    		else if(overflow)
    		{
    			if(c == ((char) 32))
    			{
    				loreList.add(finalLoreLineBuffer.toString());
    				finalLoreLineBuffer.setLength(0);
    				lastSpaceCharIndex = 0;
    				colourLengthOffset = 0;
    			}
    			else
    			{
    				finalLoreLineBuffer.append(c);
    			}
    		}
    		// No overflow allowed, current word will be pushed onto next line
    		else
    		{
    			finalLoreLineBuffer.append(c);
    			
    			// Grab text from last space to the end of the lore line buffer
    			String extra = finalLoreLineBuffer.substring(lastSpaceCharIndex + 1,finalLoreLineBuffer.length());
    			
    			// Grab text from start to the last space of the lore line buffer
    			String toAdd = finalLoreLineBuffer.substring(0,lastSpaceCharIndex);
    			
    			// Add colour and lore line to lore list
    			loreList.add(ColourUtils.transCol(toAdd));
    			
    			// reset buffers
        		finalLoreLineBuffer.setLength(0);
        		lastSpaceCharIndex = 0;
        		colourLengthOffset = 0;
        		
        		for(int j = 0; j < extra.length(); j++)
        		{
        			if((extra.charAt(j) == ((char) 38) || (extra.charAt(j) == ((char) 167))))
        			{
        				colourLengthOffset+=2;
        				j++;
        			}
        		}
        		
        		colourLengthOffset += colourBuffer.length();
        		
        		// Add extra to new line
        		finalLoreLineBuffer.append(colourBuffer.toString() + extra);
    		}
    	}
    	
    	if(finalLoreLineBuffer.length() > 0)
    	{
    		loreList.add(ColourUtils.transCol(finalLoreLineBuffer.toString()));
    	}
        
        return loreList;
    }
	
	/**
	 * Flips text to upside down
	 * @param text
	 * @return
	 */
	public static String flipText(String text)
	{
		StringBuilder sb = new StringBuilder(text.length());
		char[] textArr = text.toCharArray();
		
		for(int i = (textArr.length - 1); i >= 0; i--)
		{
			if(!upsideDownCharacterMap.containsKey(textArr[i]))
			{
				sb.append(textArr[i]);
				continue;
			}
			
			sb.append(upsideDownCharacterMap.get(textArr[i]));
		}
		
		return sb.toString();
	}
	
	static
	{
		upsideDownCharacterMap.put('A',new String(Character.toChars(0x2c6f)));
		upsideDownCharacterMap.put('B',new String(Character.toChars(0xa4ed)));
		upsideDownCharacterMap.put('C',new String(Character.toChars(0x0186)));
		upsideDownCharacterMap.put('D',new String(Character.toChars(0xa4f7)));
		upsideDownCharacterMap.put('E',new String(Character.toChars(0x018e)));
		upsideDownCharacterMap.put('F',new String(Character.toChars(0xa4de)));
		upsideDownCharacterMap.put('G',new String(Character.toChars(0xa4e8)));
		upsideDownCharacterMap.put('H',new String(Character.toChars(0x0048)));
		upsideDownCharacterMap.put('I',new String(Character.toChars(0x0049)));
		upsideDownCharacterMap.put('J',new String(Character.toChars(0x017f)));
		upsideDownCharacterMap.put('K',new String(Character.toChars(0xa4d8)));
		upsideDownCharacterMap.put('L',new String(Character.toChars(0xa4f6)));
		upsideDownCharacterMap.put('M',new String(Character.toChars(0x0057)));
		upsideDownCharacterMap.put('N',new String(Character.toChars(0x004e)));
		upsideDownCharacterMap.put('O',new String(Character.toChars(0x004f)));
		upsideDownCharacterMap.put('P',new String(Character.toChars(0xa4d2)));
		upsideDownCharacterMap.put('Q',new String(Character.toChars(0x1ff8)));
		upsideDownCharacterMap.put('R',new String(Character.toChars(0xa4e4)));
		upsideDownCharacterMap.put('S',new String(Character.toChars(0x0053)));
		upsideDownCharacterMap.put('T',new String(Character.toChars(0xa4d5)));
		upsideDownCharacterMap.put('U',new String(Character.toChars(0xa4f5)));
		upsideDownCharacterMap.put('V',new String(Character.toChars(0x039b)));
		upsideDownCharacterMap.put('W',new String(Character.toChars(0x004d)));
		upsideDownCharacterMap.put('X',new String(Character.toChars(0x0058)));
		upsideDownCharacterMap.put('Y',new String(Character.toChars(0x2144)));
		upsideDownCharacterMap.put('Z',new String(Character.toChars(0x005a)));
		
		upsideDownCharacterMap.put('a',new String(Character.toChars(0x0250)));
		upsideDownCharacterMap.put('b',new String(Character.toChars(0x0071)));
		upsideDownCharacterMap.put('c',new String(Character.toChars(0x0254)));
		upsideDownCharacterMap.put('d',new String(Character.toChars(0x0070)));
		upsideDownCharacterMap.put('e',new String(Character.toChars(0x01dd)));
		upsideDownCharacterMap.put('f',new String(Character.toChars(0x025f)));
		upsideDownCharacterMap.put('g',new String(Character.toChars(0x0183)));
		upsideDownCharacterMap.put('h',new String(Character.toChars(0x0265)));
		upsideDownCharacterMap.put('i',new String(Character.toChars(0x1d09)));
		upsideDownCharacterMap.put('j',new String(Character.toChars(0x027e)));
		upsideDownCharacterMap.put('k',new String(Character.toChars(0x029e)));
		upsideDownCharacterMap.put('l',new String(Character.toChars(0x006c)));
		upsideDownCharacterMap.put('m',new String(Character.toChars(0x026f)));
		upsideDownCharacterMap.put('n',new String(Character.toChars(0x0075)));
		upsideDownCharacterMap.put('o',new String(Character.toChars(0x006f)));
		upsideDownCharacterMap.put('p',new String(Character.toChars(0x0064)));
		upsideDownCharacterMap.put('q',new String(Character.toChars(0x0062)));
		upsideDownCharacterMap.put('r',new String(Character.toChars(0x0279)));
		upsideDownCharacterMap.put('s',new String(Character.toChars(0x0073)));
		upsideDownCharacterMap.put('t',new String(Character.toChars(0x0287)));
		upsideDownCharacterMap.put('u',new String(Character.toChars(0x006e)));
		upsideDownCharacterMap.put('v',new String(Character.toChars(0x028c)));
		upsideDownCharacterMap.put('w',new String(Character.toChars(0x028d)));
		upsideDownCharacterMap.put('x',new String(Character.toChars(0x0078)));
		upsideDownCharacterMap.put('y',new String(Character.toChars(0x028e)));
		upsideDownCharacterMap.put('z',new String(Character.toChars(0x007a)));
		
		upsideDownCharacterMap.put('?',new String(Character.toChars(0x00bf)));
		upsideDownCharacterMap.put('!',new String(Character.toChars(0x00a1)));
		upsideDownCharacterMap.put('\'',new String(Character.toChars(0x002c)));
		upsideDownCharacterMap.put(',',new String(Character.toChars(0x0027)));
		upsideDownCharacterMap.put('>',new String(Character.toChars(0x003c)));
		upsideDownCharacterMap.put('<',new String(Character.toChars(0x003e)));
		upsideDownCharacterMap.put('_',new String(Character.toChars(0x203e)));
		upsideDownCharacterMap.put('&',new String(Character.toChars(0x214b)));
	}
}