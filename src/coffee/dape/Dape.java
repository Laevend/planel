package coffee.dape;

import java.io.File;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import coffee.dape.config.Configurable;
import coffee.dape.config.DapeConfig;
import coffee.dape.config.TomlConfig;
import coffee.dape.listeners.TestLis;
import coffee.dape.utils.ColourUtils;
import coffee.dape.utils.FileOpUtils;
import coffee.dape.utils.Logg;
import coffee.dape.utils.tools.ClasspathCollector;
import coffee.khyonieheart.lilac.value.TomlObject;

public final class Dape extends JavaPlugin
{
	private static Dape INSTANCE;
	private static Path DAPE_PLUGIN_PATH;
	private static final String NAMESPACE_NAME = "DAPE";
	private static DapeConfig config = null;
	
	private static long initStartTime = 0;
	private static long initEndTime = 0;
	
	/* =================== *
	 *   Pre-Bukkit Load
	 * =================== */
	
	@Override
	public void onEnable()
	{
		initStartTime = System.currentTimeMillis();
		INSTANCE = this;
		DAPE_PLUGIN_PATH = Path.of(getFile().getAbsolutePath());
		createConfig();
		printHeader();
		
		Logg.verb("Plugin running @ " + DAPE_PLUGIN_PATH.toString());
		
		initEndTime = System.currentTimeMillis();
		
		Logg.info("&eDape Pre-Bukkit load initialised in " + ((initEndTime - initStartTime) / 1000F) + " seconds");
		
		Logg.info("This an info message");
		Logg.warn("This is a warning message");
		Logg.error("This is an error message");
		Logg.error("This is an error message with exception",new NullPointerException("Oops"));
		Logg.fatal("This is a fatal message");
		Logg.fatal("This is an fatal message with exception",new NullPointerException("Oops"));
		
		Bukkit.getPluginManager().registerEvents(new TestLis(),this);
	}
	
	private void createConfig()
	{
		Map<String,TomlObject<?>> defaults = new HashMap<>();
		
		Logg.title("Collecting Default Configuration Values...");
		
		try
		{
			ClasspathCollector collector = new ClasspathCollector(DAPE_PLUGIN_PATH,Dape.class.getClassLoader());
			Set<String> configurableClasses = collector.getClasspathsAssignableFrom(Configurable.class);
			
			for(String clazz : configurableClasses)
			{			
				Class<?> configurableClass = Class.forName(clazz,false,Dape.class.getClassLoader());
				
				try
				{
					Constructor<?> configurableConst = configurableClass.getDeclaredConstructor();
					Configurable conf = (Configurable) configurableConst.newInstance();
					defaults.putAll(conf.getDefaults());
					Logg.Common.printOk(Logg.Common.Component.CONFIG,"Collecting",configurableClass.getSimpleName());
				}
				catch (Exception e)
				{
					Logg.Common.printFail(Logg.Common.Component.CONFIG,"Collecting",configurableClass.getSimpleName());
				}
			}
		}
		catch (Exception e)
		{
			Logg.fatal("Local configurables could not be initialised!",e);
		}
		
		/**
		 * If swapping to YAML, comment out TomlConfig initialiser and uncomment YamlConfig
		 */
		
		config = new TomlConfig(internalFilePath("config.toml"),defaults,"Dape config file");
		
		//config = new YamlConfig(internalFilePath("config.yml"),defaults,"Dape config file");
	}
	
	/**
	 * Used to shutdown the server in times when the server is left in a state that cannot be recovered
	 * Shutting down the server prevents further data degradation and unpredictable server states
	 */
	public static void forceShutdown()
	{
		Logg.error("A fatal error has occured. The server will be forcefully shutdown to prevent further damage.");
		Bukkit.getServer().shutdown();
	}
	
	/**
	 * Prints header for Dape
	 */
	private final void printHeader()
	{
		// String builder necessary to create a single string otherwise the logger prints the time for each line
		StringBuilder sb = new StringBuilder();
		
		sb.append("\r" + String.format("%" + 400 + "s", "") + "\n\n\n");
		
		for(String s : logo)
		{
			sb.append(s);
		}
		
		sb.append("\n\n\n");
		
		Logg.raw(ColourUtils.transCol(sb.toString()));
	}
	
	public static final Dape instance()
	{
		return INSTANCE;
	}
	
	/**
	 * Returns an internal file path for dapes plugin data folder with an appended directory path
	 * @param path Appended directory path starting from ./plugins/Dape/
	 * @return Path of directory or file internal to dapes plugin data directory
	 */
	public static Path internalFilePath(String path)
	{
		Path p = Paths.get(Dape.instance().getDataFolder().getPath() + File.separator + path);
		
		// Make sure directories are created or that they exist before returning path
		if(Files.isRegularFile(p))
		{
			FileOpUtils.createDirectoriesForFile(p);
		}
		else
		{
			FileOpUtils.createDirectories(p);
		}
		
		return p; 
	}
	
	public static final Path getPluginPath()
	{
		return DAPE_PLUGIN_PATH;
	}
	
	public static String getNamespaceName()
	{
		return NAMESPACE_NAME;
	}

	public static int getMajorVersion()
	{
		String major = INSTANCE.getDescription().getVersion().split("[.]")[0];
		return major.length() == 0 ? 0 : Integer.parseInt(major);
	}

	public static int getMinorVersion()
	{
		String minor = INSTANCE.getDescription().getVersion().split("[.]")[1];
		return minor.length() == 0 ? 0 : Integer.parseInt(minor);
	}

	public static int getRevision()
	{
		String patch = INSTANCE.getDescription().getVersion().split("[.]")[2];
		return patch.length() == 0 ? 0 : Integer.parseInt(patch);
	}
	
	public static int getHotfix()
	{
		String hotfix = INSTANCE.getDescription().getVersion().split("[.]")[3];
		return hotfix.length() == 0 ? 0 : Integer.parseInt(hotfix);
	}

	public static String getVersion()
	{
		return INSTANCE.getDescription().getVersion();
	}
	
	public DapeConfig getConfigFile()
	{
		return config;
	}
	
	@Override
	public void saveConfig()
	{
		config.saveConfig();
	}
	
	public void reloadConfig()
	{
		config.reloadConfig();
	}
	
	@Override
	public void saveDefaultConfig()
	{
		throw new UnsupportedOperationException("The default configuration is not supported!");
	}
	
	@Override
	public FileConfiguration getConfig()
	{
		throw new UnsupportedOperationException("The default configuration is not supported! Please use 'Dape.instance().getConfigFile()");
	}

	private final String[] logo = new String[]
	{
		"          &c                 ++++++-+++++++++-                 \r\n",
		"          &c             --+##++-++###########++--             \r\n",
		"          &c          +-++###     ++########## +---+-          \r\n",
		"          &c        -++######++-   -+#########+   +#+++        \r\n",
		"          &c      -++##########++-  ++########-   -#####+      \r\n",
		"          &c     -+##############+++ ++#######-   +### #+-               &8[ &cD A P E &8]\r\n",
		"          &c   -+++---++###########++ ########-  -+######++-   \r\n",
		"          &c  -++-     +----++######++ #######-  +#########+-            &9Version &8> &e" + getDescription().getVersion() + "\r\n",
		"          &c -+#+            ++++############+  -+#######++++-           &9Message Prefix &8> &8[&cD&8]\r\n",
		"          &c +##++----------++###############+ ++#######+- -++           &9Spigot API &8> &e" + getDescription().getAPIVersion() + "\r\n",
		"          &c.+############################### ++#######+-   ++.          &9Contributors &8> &8[&e" + String.join(",",getDescription().getAuthors()) + "&8]\r\n",
		"          &c-+##+############++++#######+#+###+#####++--    ++-          &9Bukkit Ver &8> &e" + Bukkit.getBukkitVersion() + "\r\n",
		"          &c-+#++######+###+####+#+++++++##++++###++--   .-+#+-\r\n",
		"          &c-######+--++#  +##############+####++++#   --+####-\r\n",
		"          &c-+#+++-.   +###+#############+#++##+  #+--+####+#+-\r\n",
		"          &c-++---   .-++++++++++++++++++####+#+++++++++++++#+-\r\n",
		"          &c-++    .-+++++++++#+###+###+++++++++++++++++++++++.\r\n",
		"          &c.++   -++++++++++++++++++++++++++++++++++++++++ ++.\r\n",
		"          &c -++#-+++++++++- ++++++#+++++++###+-.........--++- \r\n",
		"          &c .++++++++++++. .+++++++++++++++++            +++. \r\n",
		"          &c  --+++++++++-  .+++++++ -+++++++++-....     .+--  \r\n",
		"          &c   .-++++++++.  .++++++++ --+++++++++++++...---.   \r\n",
		"          &c     .++ +++-   .++++++++. +-+++++++++++++ ++.     \r\n",
		"          &c      .-++#+.   .++++++++++  .-+++++++++#++-.      \r\n",
		"          &c        .--+-   -+++++++++-.   ..--+++++--.        \r\n",
		"          &c          ...... ++++++++++-.      ++--..          \r\n",
		"          &c             ...-+++++++++++--........             \r\n",
		"          &c                 .................                 \r\n",
	};	
}