package coffee.dape.cmdparsers.astral.parser;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;

/**
 * @author Laeven
 * 
 */
public class ParseResult
{
	private boolean parseSuccessful;		// True or false if the command parsed correctly
	private boolean hasPermission = false;	// 
	private String pathName;				// The syntax type chosen
	private String arguments[];				// The map of arguments used for this command
	private CommandSender sender;			// The sender of this command
	private String missingPermission;
	
	// <argId,argument typed by player that has already been casted>
	private Map<String,Object> mappedArgs = new HashMap<>();	// Arguments that are entered for a argument that is mapped 
	
	/**
	 * Create a new parser result
	 * @param parse The parse result
	 * @param pathName The name of the path to execute
	 * @param arguments Array of arguments to execute with
	 * @param sender The Console or Player executing the command
	 */
	public ParseResult(boolean parse,String pathName,String arguments[],CommandSender sender)
	{
		this.parseSuccessful = parse;
		this.hasPermission = true;
		this.pathName = pathName;
		this.arguments = arguments;
		this.sender = sender;
	}
	
	/**
	 * Create a new parser result
	 * @param parse The parse result
	 * @param pathName The name of the path to execute
	 * @param arguments Array of arguments to execute with
	 * @param sender The Console or Player executing the command
	 * @param mappedArgs Mapped arguments
	 */
	public ParseResult(boolean parse,String pathName,String arguments[],CommandSender sender,Map<String,Object> mappedArgs)
	{
		this(parse,pathName,arguments,sender);
		this.mappedArgs = mappedArgs;
	}
	
	/**
	 * Create a new failed parse result
	 * @param parse The parse result
	 * @param permission If the player has permission
	 */
	public ParseResult(boolean parse,boolean permission)
	{
		this.parseSuccessful = parse;
		this.hasPermission = permission;
	}
	
	/**
	 * Create a new failed parse result
	 * @param parse The parse result
	 * @param permission If the player has permission
	 */
	public ParseResult(boolean parse,boolean permission,String missingPerm)
	{
		this.parseSuccessful = parse;
		this.hasPermission = permission;
		this.missingPermission = missingPerm;
	}
	
	/**
	 * Returns the parse result
	 * @return Parse result
	 */
	public boolean getResult()
	{
		return parseSuccessful;
	}
	
	/**
	 * Returns the result of the player having permission to parse this commands
	 * @return true if the player has permission
	 */
	public boolean hasPermission()
	{
		return hasPermission;
	}

	/**
	 * Returns the path name
	 * @return The path name string
	 */
	public String getPathName()
	{
		return pathName;
	}
	
	/**
	 * Get the map of arguments used in this commands execution
	 * @return The map of arguments
	 */
	public String[] getArguments()
	{
		return arguments;
	}

	/**
	 * Get the sender for this command
	 * @return The sender
	 */
	public CommandSender getSender()
	{
		return sender;
	}
	
	/**
	 * Get the missing permission that
	 * the player did not have
	 * @return Missing Permission String
	 */
	public String getMissingPermission()
	{
		return missingPermission;
	}

	public Map<String,Object> getMappedArgs()
	{
		return mappedArgs;
	}

	public void setMappedArgs(Map<String, Object> mappedArgs)
	{
		this.mappedArgs = mappedArgs;
	}
}