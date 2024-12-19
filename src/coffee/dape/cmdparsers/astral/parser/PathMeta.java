package coffee.dape.cmdparsers.astral.parser;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.bukkit.entity.Player;

import coffee.dape.cmdparsers.astral.parser.CommandParser.CmdSender;
import coffee.dape.utils.Logg;
import coffee.dape.utils.structs.Pair;

public class PathMeta
{
	private AstralExecutor parentExecutor;
	private Method pathMethod;						// Name of logic method
	private CmdSender sender;						// Which sender is allowed to execute this command
	private Argument[] args;						// Arguments of this command
	private boolean noArgs = false;					// If this command has no arguments
	private boolean usingMappedArguments = false;	// If path logic method contains mapped arguments
	private int numberOfMappedArguments = 0;		// Number of arguments that are mapped (used for creating array size)
	private Map<UUID,String> tokens;				// Token string for path's that need a token to be executed
	// TODO Tokens are dynamic and don't belong in meta, they are generated on use
	private List<Pair<String,Class<?>>> mappedParameterTypes; // Mapped logic method types
	
	private String pathName;
	private String description;
	private String permission;
	private String syntax;
	private String usageExample;
	private boolean hidden = false;
	private boolean elevated = false;
	
	/**
	 * Creates a new path
	 * @param parentExecutor The executor this path meta belongs to
	 * @param pathLogicMethodName The path's logic method which is called upon execution
	 * @param sender The type of sender that's allowed to execute this path
	 * @param args The arguments of this path
	 */
	public PathMeta(AstralExecutor parentExecutor,Method pathLogicMethod,CmdSender sender,Argument... args)
	{
		this.parentExecutor = parentExecutor;
		this.pathMethod = pathLogicMethod;
		this.sender = sender;
		this.args = args;
		
		for(Argument arg : args)
		{
			if(arg.getMapId() == null || arg.getMapId().isEmpty() || arg.getMapId().isBlank()) { continue; }
			
			numberOfMappedArguments++;
			Logg.verb("Mapped argument method '" + arg.getMapId() + "' for path " + this.pathMethod.getName(),Logg.VerbGroup.ASTRAL_PARSER);
			usingMappedArguments = true;
		}
	}
	
	/**
	 * Creates a new path
	 * @param parentExecutor The executor this path meta belongs to
	 * @param pathLogicMethodName The path's logic method which is called upon execution
	 */
	public PathMeta(AstralExecutor parentExecutor,Method pathLogicMethod,CmdSender sender)
	{
		this.parentExecutor = parentExecutor;
		this.pathMethod = pathLogicMethod;
		this.sender = sender;
		this.noArgs = true;
	}
	
	public Method getPathMethod()
	{
		return pathMethod;
	}

	public CmdSender getSender()
	{
		return sender;
	}

	public Argument[] getArgs()
	{
		return args;
	}

	public boolean isNoArgs()
	{
		return noArgs;
	}

	public String getPathName()
	{
		return pathName;
	}

	protected void setPathName(String pathName)
	{
		this.pathName = pathName;
	}

	public String getDescription()
	{
		return description;
	}

	protected void setDescription(String description)
	{
		this.description = description;
	}

	public String getPermission()
	{
		return permission;
	}

	protected void setPermission(String permission)
	{
		this.permission = permission;
	}

	public String getSyntax()
	{
		return syntax;
	}

	protected void setSyntax(String syntax)
	{
		this.syntax = syntax;
	}

	public String getUsageExample()
	{
		return usageExample;
	}

	protected void setUsageExample(String usageExample)
	{
		this.usageExample = usageExample;
	}

	public boolean isHidden()
	{
		return hidden;
	}

	protected void setHidden(boolean hidden)
	{
		this.hidden = hidden;
	}

	public boolean isElevated()
	{
		return elevated ? true : parentExecutor.isElevated() ? true : false;
	}
	
	protected void setUsingMappedArguments(boolean usingMappedArguments)
	{
		this.usingMappedArguments = usingMappedArguments;
	}
	
	public boolean isUsingMappedArguments()
	{
		return usingMappedArguments;
	}
	
	protected void setElevated(boolean elevated)
	{
		this.elevated = elevated;
	}
	
	public Map<UUID,String> getTokens()
	{
		if(tokens == null) { tokens = new HashMap<>(); }
		
		return tokens;
	}

	protected void setToken(Player p,String token)
	{
		if(tokens == null) { tokens = new HashMap<>(); }
		this.tokens.put(p.getUniqueId(),token);
	}
	
	protected void setToken(UUID playerUUID,String token)
	{
		if(tokens == null) { tokens = new HashMap<>(); }
		this.tokens.put(playerUUID,token);
	}
	
	public int getNumberOfMappedArguments()
	{
		return numberOfMappedArguments;
	}

	public List<Pair<String,Class<?>>> getMappedParameterTypes()
	{
		return mappedParameterTypes;
	}

	protected void setMappedParameterTypes(List<Pair<String,Class<?>>> mappedParameterTypes)
	{
		this.mappedParameterTypes = mappedParameterTypes;
	}

	public AstralExecutor getParent()
	{
		return parentExecutor;
	}
	
	/**
	 * Returns a string representing this path
	 * @return Path summary
	 */
	public String getPathSummary()
	{
		if(noArgs) { return "NO_ARGS"; }
		
		StringBuilder sb = new StringBuilder();
		
		for(Argument arg : args)
		{
			sb.append(arg.getArgumentKey() + ",");
		}
		
		String pathSummary = sb.toString();
		return pathSummary.substring(0,pathSummary.length() - 1);
	}
	
	/**
	 * Returns a checksum of the path summary for use
	 * in quickly checking for duplicate paths
	 * @return Path checksum
	 */
	public long getChecksum()
	{
		byte[] pathSummary = getPathSummary().getBytes();
		
		Checksum crc32 = new CRC32();
	    crc32.update(pathSummary,0,pathSummary.length);
	    return crc32.getValue();
	}
}
