package coffee.dape.cmdparsers.astral.parser;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;

import coffee.dape.Dape;
import coffee.dape.cmdparsers.astral.annos.CommandEx;
import coffee.dape.config.Configurable;
import coffee.dape.exception.InvalidCommandClassException;
import coffee.dape.exception.MissingAnnotationException;
import coffee.dape.utils.Logg;
import coffee.dape.utils.tools.ClasspathCollector;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class CommandFactory implements Configurable
{
	private static InternalCommandMap internalCommandMap;
	private static Map<String,AstralExecutor> commandMap = new HashMap<>();
	public static String COMMAND_PREFIX = "dape";
	
	static
	{
		internalCommandMap = new InternalCommandMap();
	}
	
	/**
	 * Initialises a set of commands
	 * @param commandClasspaths Set of command class paths
	 * @throws ClassNotFoundException Thrown if a command class path cannot be found
	 * @throws NoSuchMethodException Thrown if a command class does not have an empty constructor
	 * @throws SecurityException Thrown if for some reason the constructor is not accessible
	 * @throws InvalidCommandClassException Thrown if the command class loaded is not a class extending from BaseExecutor
	 * @throws MissingAnnotationException Thrown if the command class loaded is missing any of its annotations
	 */
	public static void initCommand(Set<String> commandClasspaths) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InvalidCommandClassException, MissingAnnotationException
	{
		CommandFactory.initCommand(commandClasspaths,Dape.class.getClassLoader());
	}
	
	/**
	 * Initialises a set of commands
	 * @param commandClasspaths Set of command class paths
	 * @param loader ClassLoader used to load this command class with
	 * @throws ClassNotFoundException Thrown if a command class path cannot be found
	 * @throws NoSuchMethodException Thrown if a command class does not have an empty constructor
	 * @throws SecurityException Thrown if for some reason the constructor is not accessible
	 * @throws InvalidCommandClassException Thrown if the command class loaded is not a class extending from BaseExecutor
	 * @throws MissingAnnotationException Thrown if the command class loaded is missing any of its annotations
	 */
	public static void initCommand(Set<String> commandClasspaths,ClassLoader loader) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InvalidCommandClassException, MissingAnnotationException
	{
		for(String cp : commandClasspaths)
		{
			CommandFactory.initCommand(cp,loader);
		}
	}
	
	/**
	 * Initialises a command
	 * @param commandClasspath Class path of the command
	 * @throws ClassNotFoundException Thrown if a command class path cannot be found
	 * @throws NoSuchMethodException Thrown if a command class does not have an empty constructor
	 * @throws SecurityException Thrown if for some reason the constructor is not accessible
	 * @throws InvalidCommandClassException Thrown if the command class loaded is not a class extending from BaseExecutor
	 * @throws MissingAnnotationException Thrown if the command class loaded is missing any of its annotations
	 */
	public static void initCommand(String commandClasspath) throws ClassNotFoundException, InvalidCommandClassException, NoSuchMethodException, SecurityException, MissingAnnotationException
	{
		initCommand(commandClasspath,Dape.class.getClassLoader());
	}
	
	/**
	 * Initialises a command
	 * @param commandClasspath Class path of the command
	 * @param loader ClassLoader used to load this command class with
	 * @throws ClassNotFoundException Thrown if a command class path cannot be found
	 * @throws NoSuchMethodException Thrown if a command class does not have an empty constructor
	 * @throws SecurityException Thrown if for some reason the constructor is not accessible
	 * @throws InvalidCommandClassException Thrown if the command class loaded is not a class extending from BaseExecutor
	 * @throws MissingAnnotationException Thrown if the command class loaded is missing any of its annotations
	 */
	public static void initCommand(String commandClasspath,ClassLoader loader) throws ClassNotFoundException, InvalidCommandClassException, NoSuchMethodException, SecurityException, MissingAnnotationException
	{
		Class<?> commandClass = null;
		commandClass = Class.forName(commandClasspath,true,loader);
		
		if(!AstralExecutor.class.isAssignableFrom(commandClass)) { throw new InvalidCommandClassException("This class is not assignable from AstralExecutor!"); }
		
		CommandFactory.initCommandWithClass(commandClass);
	}
	
	/**
	 * Initialises a command
	 * @param commandClass A command class
	 * @throws NoSuchMethodException Thrown if a command class does not have an empty constructor
	 * @throws SecurityException Thrown if for some reason the constructor is not accessible
	 * @throws MissingAnnotationException Thrown if the command class loaded is missing any of its annotations
	 */
	public static void initCommandWithClass(Class<?> commandClass) throws NoSuchMethodException, SecurityException, MissingAnnotationException
	{
		Logg.verb("Registering command at path: " + commandClass.getName());
		
		Constructor<?> cons = commandClass.getConstructor();
		AstralExecutor executor = null;
		
		try { executor = (AstralExecutor) cons.newInstance(); } catch(Exception e) { Logg.fatal("Error occured creating new instance of command!",e); return; }
		
		commandMap.put(executor.getCommandName(),executor);
		
		try
		{
			internalCommandMap.addCommand(executor);
		}
		catch(Exception e)
		{
			Logg.error("Command " + executor.getCommandName() + " failed to be added to the command map!");
		}
	}
	
	/**
	 * Collects local plugin command classes and initialises them
	 */
	public static void collectAndInitLocal()
	{
		Logg.title("Constructing commands...");
		
		try
		{
			ClasspathCollector collector = new ClasspathCollector();
			Set<String> cmdClasses = collector.getClasspathsWithAnnotation(CommandEx.class);
			CommandFactory.initCommand(cmdClasses);
		}
		catch (Exception e)
		{
			Logg.fatal("Local commands could not be initialised!",e);
		}
	}
	
	/**
	 * Refreshes the server and player clients as to
	 * what commands are available.
	 * 
	 * <p>This method should be called after modifying the
	 * command map
	 */
	public static void refreshCommands()
	{
		Logg.info("Syncing commands");
		CraftServer server = (CraftServer) Bukkit.getServer();
		server.syncCommands();
		
		Bukkit.getOnlinePlayers().forEach(v ->
		{
			v.updateCommands();
		});
	}
	
	/**
	 * Unregisters a set of commands
	 * @param commandNames Set of command names
	 */
	public static void unregisterCommands(Set<String> commandNames)
	{
		internalCommandMap.removeCommands(CommandFactory.COMMAND_PREFIX,commandNames);
	}
	
	/**
	 * Get internal command map
	 * @return Command Map
	 */
	public static Map<String,Command> getInternalCommandMap()
	{
		return internalCommandMap.getInternalCommandMap();
	}
	
	public static Map<String,AstralExecutor> getAstralCommandMap()
	{
		return commandMap;
	}
	
	/**
	 * Gets the group permission for a group name
	 * @param groupName Name of the group registered to commands
	 * @return Group permission
	 */
	public static String getGroupPermission(String groupName)
	{
		return CommandFactory.COMMAND_PREFIX + ".command.group." + groupName;
	}
	
	public static final String CFG_DEFAULT_GROUP_PERMISSION_NEEDED = "astral.default_group_permission_needed";
	public static final String CFG_FUZZY_SEARCH_SUGGESTION_RATIO = "astral.fuzzy_search_suggestion_ratio";

	@Override
	public Map<String,Object> getDefaults()
	{
		return Map.of(CFG_DEFAULT_GROUP_PERMISSION_NEEDED,false,
				  CFG_FUZZY_SEARCH_SUGGESTION_RATIO,70);
		
//		return Map.of(CFG_DEFAULT_GROUP_PERMISSION_NEEDED,false,
//					  CFG_FUZZY_SEARCH_SUGGESTION_RATIO,70);
	}
}
