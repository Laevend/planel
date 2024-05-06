package coffee.dape.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class Logg
{
	public final static String DAPE_PREFIX = "&8[&cD&8]";
	
	private final static String verbosePrefix = "&8[&bVERB&8] ";
	private final static String infoPrefix = "&8[&9INFO&8] ";
	private final static String warningPrefix = "&8[&6WARN&8] ";
	private final static String errorPrefix = "&8[&cERROR&8] ";
	private final static String fatalPrefix = "&8[&4FATAL&8] ";
	
	private static boolean hideVerbose = false;
	private static boolean hideWarnings = false;
	private static boolean hideErrors = false;
	private static boolean hideFatals = false;
	private static boolean silenceExceptions = false;
	private static boolean writeExceptions = true;
	
	private static boolean verboseExcludingMode = false;
	private static List<String> enabledVerboseClasses = new ArrayList<>();
	
	public static synchronized void emptyDivider(int emptyLines)
	{
		raw("\r" + String.format("%" + 400 + "s", ""));
		
		for(int i = 1; i < emptyLines; i++)
		{
			raw("\n");
		}
	}
	
	/**
	 * Gets current time in hrs, minutes, and seconds
	 * @return Formatted current time
	 */
	private static synchronized String getTime()
	{
		return DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
	}
	
	/**
	 * Prints a message to the console with no sub-prefix
	 * @param s Message
	 */
	public static synchronized void raw(String s)
	{
		Bukkit.getServer().getConsoleSender().sendMessage(s);
	}
	
	/**
	 * Prints a message to the console
	 * @param s String to print
	 */
	public static void print(String s)
	{
		// Why the large gap? Well it's to print empty space over the '[00:00:00 INFO]:' print header
		Bukkit.getServer().getConsoleSender().sendMessage("\r                \r[" + getTime() + "] " + ColourUtils.transCol(DAPE_PREFIX + " " + s));
	}
	
	/**
	 * Prints a message to the console without the time or plugin message prefix
	 * @param s String to print
	 */
	public static void printFromBlank(String s)
	{
		// Why the large gap? Well it's to print empty space over the '[00:00:00 INFO]:' print header
		Bukkit.getServer().getConsoleSender().sendMessage("\r                \r" + ColourUtils.transCol(s));
	}
	
	/**
	 * Prints an verbose message
	 * Use this for showing variables and other messages. Useful when debugging
	 * @param type The class this debug message is called from
	 * @param s The message
	 */
	public static synchronized void printStacktrace()
	{
		print(verbosePrefix + getClassAndMethod(Thread.currentThread().getStackTrace()[2]) + "&2StackTrace");
		
		for(StackTraceElement stackTraceEle : Thread.currentThread().getStackTrace())
		{
			printFromBlank("    &a" + stackTraceEle.toString());
		}
	}
	
	/**
	 * Prints an verbose message
	 * Use this for showing variables and other messages. Useful when debugging
	 * @param type The class this debug message is called from
	 * @param s The message
	 */
	public static synchronized void callFrom()
	{
		print(verbosePrefix + getClassAndMethod(Thread.currentThread().getStackTrace()[2]) + "&2" + "CalledFrom " + getClassAndMethod(Thread.currentThread().getStackTrace()[4]));
	}
	
	/**
	 * Prints an verbose message
	 * Use this for showing variables and other messages. Useful when debugging
	 * @param type The class this debug message is called from
	 * @param s The message
	 */
	public static synchronized void verb(String s)
	{		
		if(hideVerbose) { return; }		
		if(enabledVerboseClasses.contains(Thread.currentThread().getStackTrace()[2].getClassName())) { return; }
		
		print(verbosePrefix + getClassAndMethod(Thread.currentThread().getStackTrace()[2]) + "&b" + s);
	}
	
	/**
	 * Prints an info message
	 * 
	 * <p>Presents information to the console</p>
	 * @param s The message
	 */
	public static synchronized void info(String s)
	{
		print(infoPrefix + getClass(Thread.currentThread().getStackTrace()[2]) + "&9" + s);
	}
	
	/**
	 * Prints an info message
	 * 
	 * <p>Presents information to the console about an action that completed successfully</p>
	 * @param s The message
	 */
	public static synchronized void success(String s)
	{
		print(infoPrefix + getClass(Thread.currentThread().getStackTrace()[2]) + "&a" + s);
	}
	
	/**
	 * Prints a warning message
	 * 
	 * <p>Presents information to the console about an action that failed but is not mandatory to succeed to continue operating</p>
	 * @param s The message
	 */
	public static synchronized void warn(String s)
	{
		if(hideWarnings) { return; }
		
		print(warningPrefix + getClassAndMethod(Thread.currentThread().getStackTrace()[2]) + "&6" + s);
	}
	
	/**
	 * Prints an error message
	 * 
	 * <p>Presents information to the console about an action that failed. Does not need immediate attention</p>
	 * @param s The message
	 */
	public static synchronized void error(String s)
	{
		if(hideErrors) { return; }
		
		print(errorPrefix + getClassAndMethod(Thread.currentThread().getStackTrace()[2]) + "&c" + s);
	}
	
	/**
	 * Prints an error message with an exception
	 * <p>Presents information to the console about an action that failed. Does not need immediate attention</p>
	 * @param s The message
	 * @param e The exception
	 */
	public static synchronized void error(String s,Exception e)
	{
		if(!hideErrors)
		{
			print(errorPrefix + getClassAndMethod(Thread.currentThread().getStackTrace()[2]) + "&c" + s);
		}
		
		if(!silenceExceptions)
		{
			e.printStackTrace();
		}
		
		if(writeExceptions)
		{
			writeExceptionToDiscord(e);
		}
	}
	
	/**
	 * Prints an fatal message
	 * 
	 * <p>Presents information to the console about an important action that failed. Requires immediate attention of operators</p>
	 * @param s The message
	 */
	public static synchronized void fatal(String s)
	{
		if(hideFatals) { return; }
		
		// This message is designed to get operators attention in the console
		print("&4! ! ! ! ! ! ! ! ! !");
		print(fatalPrefix + getClassAndMethod(Thread.currentThread().getStackTrace()[2]) + "&4" + s);
		print("&4^ ^ ^ ^ ^ ^ ^ ^ ^ ^");
	}
	
	/**
	 * Prints an fatal message with an exception
	 * 
	 * <p>Presents information to the console about an important action that failed. Requires immediate attention of operators</p>
	 * @param s The message
	 */
	public static synchronized void fatal(String s,Exception e)
	{
		if(!hideFatals)
		{
			// This message is designed to get operators attention in the console
			print("&4! ! ! ! ! ! ! ! ! !");
			print(fatalPrefix + getClassAndMethod(Thread.currentThread().getStackTrace()[2]) + "&4" + s);
			print("&4^ ^ ^ ^ ^ ^ ^ ^ ^ ^");
		}
		
		if(!silenceExceptions)
		{
			e.printStackTrace();
		}
		
		if(writeExceptions)
		{
			writeExceptionToDiscord(e);
		}
	}
	
	/**
	 * Prints a title in the console (useful for important messages)
	 * @param s The title
	 */
	public static synchronized void title(String s)
	{
		Bukkit.getServer().getConsoleSender().sendMessage("");
		print(s);
		Bukkit.getServer().getConsoleSender().sendMessage("");
	}
	
	/**
	 * TODO Rename and refactor
	 * Sends an error/fatal message to discord with its exception
	 * @param s Error message
	 * @param e Exception
	 */
	public static synchronized void writeExceptionToDiscord(Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		//String sStackTrace = sw.toString(); // stack trace as a string
		
		//CSVUtils.writeCSV(DataPaths.ARC_EXCEPTIONS.getPath() + File.separator + StringUtils.getDateAsString() + ".txt",sStackTrace);
	}
	
	/**
	 * Grabs the class name the logged message was printed from
	 * @param stackTrace stack trace
	 * @return Class name
	 */
	private static synchronized String getClass(StackTraceElement stackTrace)
	{		
		String[] classpath  = stackTrace.getClassName().replace(".","-").split("-");
		return "&6" + classpath[classpath.length - 1] + "&8 - &f";
	}
	
	/**
	 * Grabs the class name, line number, and method the logged message was printed from
	 * @param stackTrace stack trace
	 * @return Class name, line number and method name
	 */
	private static synchronized String getClassAndMethod(StackTraceElement stackTrace)
	{		
		String[] classpath  = stackTrace.getClassName().replace(".","-").split("-");
		return "&6" + classpath[classpath.length - 1] + " &8> &e" + stackTrace.getMethodName() + "&8 - &f";
	}

	public static synchronized boolean isHideVerbose()
	{
		return hideVerbose;
	}

	public static synchronized void setHideVerbose(boolean hideVerbose)
	{
		Logg.hideVerbose = hideVerbose;
	}

	public static synchronized boolean isHideWarnings()
	{
		return hideWarnings;
	}

	public static synchronized void setHideWarnings(boolean hideWarnings)
	{
		Logg.hideWarnings = hideWarnings;
	}

	public static synchronized boolean isHideErrors()
	{
		return hideErrors;
	}

	public static synchronized void setHideErrors(boolean hideErrors)
	{
		Logg.hideErrors = hideErrors;
	}

	public static synchronized boolean isHideFatals()
	{
		return hideFatals;
	}

	public static synchronized void setHideFatals(boolean hideFatals)
	{
		Logg.hideFatals = hideFatals;
	}

	public static synchronized boolean isSilenceExceptions()
	{
		return silenceExceptions;
	}

	public static synchronized void setSilenceExceptions(boolean silenceExceptions)
	{
		Logg.silenceExceptions = silenceExceptions;
	}

	public static synchronized boolean isWriteExceptions()
	{
		return writeExceptions;
	}

	public static synchronized void setWriteExceptions(boolean writeExceptions)
	{
		Logg.writeExceptions = writeExceptions;
	}
	
	public static synchronized List<String> getEnabledVerboseClasses()
	{
		return enabledVerboseClasses;
	}

	public static synchronized void setEnabledVerboseClasses(List<String> enabledVerboseClasses)
	{
		Logg.enabledVerboseClasses = enabledVerboseClasses;
	}

	public static class Common
	{
		private static String PREFIX_OK = "&8[&a OK &8] &a";
		private static String PREFIX_FAIL = "&8[&cFAIL&8] &c";
		
		public enum Component
		{
			CONFIG("CFG"),
			LISTENER("LIS"),
			COMMAND("CMD"),
			GUI("GUI"),
			LIBRARY("LIB"),
			MODULE("MOD");
			
			public String acronym;
			
			Component(String acronym)
			{
				this.acronym = acronym;
			}
		}
		
		/**
		 * Prints a success message showing that this component initialised successfully
		 * @param com Component type
		 * @param action The action (registering/building/constructing/initialising)
		 * @param componentIdentifier The string to identify what succeeded (The name or class name of the component)
		 */
		public static synchronized void printOk(Component com,String action,String componentIdentifier)
		{
			Bukkit.getServer().getConsoleSender().sendMessage(ColourUtils.transCol(PREFIX_OK + " &3" + com.acronym + " &8> &6" + action + " &8> &r" + componentIdentifier));
		}
		
		/**
		 * Prints a failed message showing that this component failed to initialised correctly
		 * @param com Component type
		 * @param action The action (registering/building/constructing/initialising)
		 * @param componentIdentifier The string to identify what succeeded (The name or class name of the component)
		 */
		public static synchronized void printFail(Component com,String action,String componentIdentifier)
		{
			Bukkit.getServer().getConsoleSender().sendMessage(ColourUtils.transCol(PREFIX_FAIL + " &3" + com.acronym + " &8> &6" + action + " &8> &c" + componentIdentifier));
		}
	}

//	@Override
//	public Map<String,Object> getDefaults()
//	{
//		return Map.of("logger.hide_verbose.all",false,
//					  "logger.hide_warnings",false,
//					  "logger.hide_errors",false,
//					  "logger.hide_fatals",false,
//					  "logger.hide_exceptions",false,
//					  "logger.write_exceptions",false);
//	}
}