package coffee.dape.cmdparsers.astral.parser;

import java.util.HashSet;
import java.util.Set;

import coffee.dape.cmdparsers.astral.suggestions.Suggestions;
import coffee.dape.cmdparsers.astral.types.ArgTypes;
import coffee.dape.utils.Logg;
import coffee.dape.utils.structs.Namespace;

/**
 * @author Laeven
 * 
 * Argument Set Parser
 * 
 * Takes a set of argument strings and converts them to an argument set
 */
public class ArgumentSetParser
{
	private static ArgSet set;
	private static int index = 0;
	private static String cmdName;
	
	/**
	 * Parses a string based arguments to convert them into an argument set
	 * @param args String args
	 * @param commandName Name of the command (used in error logging)
	 * @return ArgSet if parsing is successful, null otherwise
	 */
	public static ArgSet parse(String[] args,String commandName)
	{
		set = new ArgSet();
		cmdName = commandName;
		
		for(String arg : args)
		{
			if(!parseArg(arg)) { return null; }
		}
		
		return set;
	}
	
	private static String argumentText = null;
	private static String type = null;
	private static Set<String> suggestions = new HashSet<>();
	private static int suggestionIndexOffset = 0;
	
	private static boolean parseArg(String arg_)
	{
		index = 0;
		argumentText = null;
		type = null;
		suggestions.clear();
		suggestionIndexOffset = 0;
		
		// If extra spaces are found or tabs then normalise them all to single spaces and split
		String[] args = CommandParser.concatenateQuoteArguments(arg_.replaceAll("\\s++"," ").split(" "));
		
		if(args.length <= 0) { Logg.error("[" + cmdName + "] contains a string arg set that is empty or blank!"); return false; }
		
		return parsePlaceholder(args);
	}
	
	/**
	 * Parse a place holder. (Starts with '<')
	 * @return true if parsing was successful, false otherwise
	 */
	private static boolean parsePlaceholder(String[] args)
	{
		if(args[index].startsWith("<") || !args[index].startsWith("-"))
		{
			if(args.length == 1)
			{
				set.of(args[index]);
				return true;
			}
			
			argumentText = args[index];
			index++;
			
			return parseType(args);
		}
		else if(args[index].startsWith("-cs"))
		{
			if(args.length != 3) { Logg.error("[" + cmdName + "] contains the wrong number of args for a conditional suggestion!"); return false; }
			
			set.of(Integer.parseInt(args[index + 2]),Suggestions.get(Namespace.fromString(args[index + 1])).asConditional());
			return true;
		}
		else
		{
			Logg.error("[" + cmdName + "] incorrect arguments!");
			return false;
		}
	}
	
	/**
	 * Parses a type
	 * @param args
	 * @return true if parsing was successful, false otherwise
	 */
	private static boolean parseType(String[] args)
	{
		if(args.length < 3) { Logg.error("[" + cmdName + "] does not have enough arguments for parsing type!"); return false; }
		if(!args[index].startsWith("-t")) { Logg.error("[" + cmdName + "] -t not defined! cannot parse type!"); return false; }
		
		index++;
		
		if(args.length == 3)
		{
			set.of(argumentText,ArgTypes.get(args[index]));			
			return true;
		}
		else
		{
			type = args[index];
			index++;
			
			return parseSuggestions(args);
		}
	}
	
	/**
	 * Parses a unconditional o player suggestion
	 * @param args
	 * @return true if parsing was successful, false otherwise
	 */
	private static boolean parseSuggestions(String[] args)
	{
		if(args.length < 5) { Logg.error("[" + cmdName + "] does not have enough arguments for parsing suggestion!"); return false; }
		if(!args[index].startsWith("-s") && !args[index].startsWith("-ps")) { Logg.error("[" + cmdName + "] -s or -ps not defined! cannot parse suggestion!"); return false; }
		
		boolean isUnconSuggestion = args[index].startsWith("-s");
		boolean isPlayerSuggestion = args[index].startsWith("-ps");
		
		index++;
		
		if(!Suggestions.hasSuggestionList(Namespace.fromString(args[index]))) { return false; }
		
		if(args.length == (5 + suggestionIndexOffset))
		{
			if(isUnconSuggestion)
			{
				set.of(argumentText,ArgTypes.get(type),Suggestions.get(Namespace.fromString(args[index])).asUnconditional());	
			}
			else if(isPlayerSuggestion)
			{
				set.of(argumentText,ArgTypes.get(type),Suggestions.get(Namespace.fromString(args[index])).asPlayer());	
			}
					
			return true;
		}
		else if(args.length > (index + 2))
		{
			suggestions.add(args[index]);
			index++;
			suggestionIndexOffset+=2;
			
			return parseSuggestions(args);
		}
		else 
		{
			Logg.error("[" + cmdName + "] incorrect number of arguments!");
			return false;
		}
	}
}
