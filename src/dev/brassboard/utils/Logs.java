package dev.brassboard.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class Logs
{
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
	private static List<String> disabledVerboseClasses = new ArrayList<>();
	
	/**
	 * Prints an verbose message
	 * Use this for showing variables and other messages. Useful when debugging
	 * @param type The class this debug message is called from
	 * @param s The message
	 */
	public static synchronized void verb(String s)
	{		
		if(hideVerbose) { return; }		
		if(disabledVerboseClasses.contains(Thread.currentThread().getStackTrace()[2].getClassName())) { return; }
		
		PrintUtils.println(PrintUtils.toColour(verbosePrefix + getClassAndMethod(Thread.currentThread().getStackTrace()[2]) + "&b" + s));
	}
	
	/**
	 * Prints an info message
	 * Use this to print information about an action that occured
	 * @param s The message
	 */
	public static synchronized void info(String s)
	{
		PrintUtils.println(PrintUtils.toColour(infoPrefix + getClass(Thread.currentThread().getStackTrace()[2]) + "&9" + s));
	}
	
	/**
	 * Prints a warning message
	 * Use this for telling the operator something went wrong but the server can recover from it
	 * @param s The message
	 */
	public static synchronized void warn(String s)
	{
		if(hideWarnings) { return; }
		
		PrintUtils.println(PrintUtils.toColour(warningPrefix + getClassAndMethod(Thread.currentThread().getStackTrace()[2]) + "&6" + s));
	}
	
	/**
	 * Prints an error message
	 * Error messages are notifications that something has gone wrong but its not
	 * serious enough to warrant immediate attention
	 * @param s The message
	 */
	public static synchronized void error(String s)
	{
		if(hideErrors) { return; }
		
		PrintUtils.println(PrintUtils.toColour(errorPrefix + getClassAndMethod(Thread.currentThread().getStackTrace()[2]) + "&c" + s));
	}
	
	/**
	 * Prints an error message
	 * Error messages are notifications that something has gone wrong but its not
	 * serious enough to warrant immediate attention
	 * @param s The message
	 */
	public static synchronized void error(String s,Exception e)
	{
		if(!hideErrors)
		{
			PrintUtils.println(PrintUtils.toColour(errorPrefix + getClassAndMethod(Thread.currentThread().getStackTrace()[2]) + "&c" + s));
		}
		
		if(!silenceExceptions)
		{
			e.printStackTrace();
		}
		
		if(writeExceptions)
		{
			writeException(e);
		}
	}
	
	/**
	 * Prints an fatal message
	 * Fatal messages are uncorrectable errors and should alert operators immediately
	 * @param s The message
	 */
	public static synchronized void fatal(String s)
	{
		if(hideFatals) { return; }
		
		PrintUtils.println("");
		PrintUtils.println(PrintUtils.toColour(fatalPrefix + getClassAndMethod(Thread.currentThread().getStackTrace()[2]) + "&4" + s));
		PrintUtils.println("&4^ ^ ^ ^ ^ ^ ^ ^ ^ ^");
	}
	
	/**
	 * Prints an fatal message
	 * Fatal messages are uncorrectable errors and should alert operators immediately
	 * @param s The message
	 */
	public static synchronized void fatal(String s,Exception e)
	{
		if(!hideFatals)
		{
			PrintUtils.println("");
			PrintUtils.println(PrintUtils.toColour(fatalPrefix + getClassAndMethod(Thread.currentThread().getStackTrace()[2]) + "&4" + s));
			PrintUtils.println("&4^ ^ ^ ^ ^ ^ ^ ^ ^ ^");
		}
		
		if(!silenceExceptions)
		{
			e.printStackTrace();
		}
		
		if(writeExceptions)
		{
			writeException(e);
		}
	}
	
	/**
	 * Prints a title in the console (useful for important messages)
	 * @param s The title
	 */
	public static synchronized void title(String s)
	{
		Bukkit.getServer().getConsoleSender().sendMessage("");
		PrintUtils.println(PrintUtils.toColour(s));
		Bukkit.getServer().getConsoleSender().sendMessage("");
	}
	
	/**
	 * Writes an exception to disk
	 * @param e
	 */
	public static synchronized void writeException(Exception e)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String sStackTrace = sw.toString(); // stack trace as a string
		
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
		Logs.hideVerbose = hideVerbose;
	}

	public static synchronized boolean isHideWarnings()
	{
		return hideWarnings;
	}

	public static synchronized void setHideWarnings(boolean hideWarnings)
	{
		Logs.hideWarnings = hideWarnings;
	}

	public static synchronized boolean isHideErrors()
	{
		return hideErrors;
	}

	public static synchronized void setHideErrors(boolean hideErrors)
	{
		Logs.hideErrors = hideErrors;
	}

	public static synchronized boolean isHideFatals()
	{
		return hideFatals;
	}

	public static synchronized void setHideFatals(boolean hideFatals)
	{
		Logs.hideFatals = hideFatals;
	}

	public static synchronized boolean isSilenceExceptions()
	{
		return silenceExceptions;
	}

	public static synchronized void setSilenceExceptions(boolean silenceExceptions)
	{
		Logs.silenceExceptions = silenceExceptions;
	}

	public static synchronized boolean isWriteExceptions()
	{
		return writeExceptions;
	}

	public static synchronized void setWriteExceptions(boolean writeExceptions)
	{
		Logs.writeExceptions = writeExceptions;
	}
	
	public static synchronized List<String> getDisabledVerboseClasses()
	{
		return disabledVerboseClasses;
	}

	public static synchronized void setDisabledVerboseClasses(List<String> disabledVerboseClasses)
	{
		Logs.disabledVerboseClasses = disabledVerboseClasses;
	}

	public static class Common
	{
		private static String prefixOk = "&8[ &aPASS &8] &a";
		private static String prefixFail = "&8[ &cFAIL &8] &c";
		
		public static synchronized void listenerRegisterSuccessful(String listener)
		{
			PrintUtils.println(prefixOk + "&3LIS &8> &6Register &8> &r" + listener);
		}
		
		public static synchronized void listenerRegisterUnsuccessful(String listener)
		{
			PrintUtils.println(prefixFail + "&3LIS &8> &6Register &8> &r" + listener);
		}
		
		public static synchronized void listenerUnregisterSuccessful(String listener)
		{
			PrintUtils.println(prefixOk + "&3LIS &8> &dUnregister &8> &r" + listener);
		}
		
		public static synchronized void listenerUnregisterUnsuccessful(String listener)
		{
			PrintUtils.println(prefixFail + "&3LIS &8> &dUnregister &8> &r" + listener);
		}
		
		public static synchronized void commandRegisterSuccessful(String command)
		{
			PrintUtils.println(prefixOk + "&3CMD &8> &6Register &8> &r" + command);
		}
		
		public static synchronized void commandRegisterUnsuccessful(String command)
		{
			PrintUtils.println(prefixFail + "&3CMD &8> &6Register &8> &r" + command);
		}
		
		public static synchronized void commandUnregisterSuccessful(String command)
		{
			PrintUtils.println(prefixOk + "&3CMD &8> &dUnregister &8> &r" + command);
		}
		
		public static synchronized void commandUnregisterUnsuccessful(String command)
		{
			PrintUtils.println(prefixFail + "&3CMD &8> &dUnregister &8> &r" + command);
		}
		
		public static synchronized void guiRegisterSuccessful(String gui)
		{
			PrintUtils.println(prefixOk + "&3GUI &8> &6Register &8> &r" + gui);
		}
		
		public static synchronized void guiRegisterUnsuccessful(String gui)
		{
			PrintUtils.println(prefixFail + "&3GUI &8> &6Register &8> &r" + gui);
		}
		
		public static synchronized void guiUnregisterSuccessful(String gui)
		{
			PrintUtils.println(prefixOk + "&3GUI &8> &dUnregister &8> &r" + gui);
		}
		
		public static synchronized void guiUnregisterUnsuccessful(String gui)
		{
			PrintUtils.println(prefixFail + "&3GUI &8> &dUnregister &8> &r" + gui);
		}
		
		public static synchronized void libraryRegisterSuccessful(String library)
		{
			PrintUtils.println(prefixOk + "&3LIB &8> &6Load &8> &r" + library);
		}
		
		public static synchronized void libraryRegisterUnsuccessful(String library)
		{
			PrintUtils.println(prefixFail + "&3LIB &8> &6Load &8> &r" + library);
		}
		
		public static synchronized void libraryUnregisterSuccessful(String library)
		{
			PrintUtils.println(prefixOk + "&3LIB &8> &dUnload &8> &r" + library);
		}
		
		public static synchronized void libraryUnregisterUnsuccessful(String library)
		{
			PrintUtils.println(prefixFail + "&3LIB &8> &dUnload &8> &r" + library);
		}
		
		public static synchronized void moduleRegisterSuccessful(String module)
		{
			PrintUtils.println(prefixOk + "&3MOD &8> &6Load &8> &r" + module);
		}
		
		public static synchronized void moduleRegisterUnsuccessful(String module)
		{
			PrintUtils.println(prefixFail + "&3MOD &8> &6Load &8> &r" + module);
		}
		
		public static synchronized void moduleUnregisterSuccessful(String module)
		{
			PrintUtils.println(prefixOk + "&3MOD &8> &dUnload &8> &r" + module);
		}
		
		public static synchronized void moduleUnregisterUnsuccessful(String module)
		{
			PrintUtils.println(prefixFail + "&3MOD &8> &dUnload &8> &r" + module);
		}
	}
}