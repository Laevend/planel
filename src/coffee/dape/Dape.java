package coffee.dape;

import java.io.File;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import coffee.dape.cmdparsers.astral.elevatedaccount.ElevatedAccountCtrl;
import coffee.dape.cmdparsers.astral.elevatedaccount.ElevatedAccountCtrl.SecretViewWarning;
import coffee.dape.cmdparsers.astral.parser.CommandFactory;
import coffee.dape.config.Configurable;
import coffee.dape.config.DapeConfig;
import coffee.dape.config.YamlConfig;
import coffee.dape.listeners.TestLis;
import coffee.dape.utils.ColourUtils;
import coffee.dape.utils.Logg;
import coffee.dape.utils.MapUtils.ImageMapper;
import coffee.dape.utils.data.DataUtils;
import coffee.dape.utils.data.DataUtils.DType;
import coffee.dape.utils.tools.ClasspathCollector;

public final class Dape extends JavaPlugin
{
	private static Dape INSTANCE;
	private static Path DAPE_PLUGIN_PATH;
	private static final String NAMESPACE_NAME = "DAPE";
	private static final String DT_PLUGIN_MANAGED_ENTITY = "plugin_managed_entity";
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
		
		configureLogger();
		ElevatedAccountCtrl.init();
		CommandFactory.collectAndInitLocal();
		ImageMapper.load();
		
		initEndTime = System.currentTimeMillis();
		
		Logg.info("&f&lDape Pre-Bukkit load initialised in " + ((initEndTime - initStartTime) / 1000F) + " seconds");
		
		// For the time being until I built the auto listener register, we manually init
		Bukkit.getPluginManager().registerEvents(new TestLis(),this);
		Bukkit.getPluginManager().registerEvents(new ElevatedAccountCtrl(),this);
		Bukkit.getPluginManager().registerEvents(new ImageMapper(),this);
		Bukkit.getPluginManager().registerEvents(new SecretViewWarning(),this);
	}
	
	@Override
	public void onDisable()
	{
		ImageMapper.save();
	}
	
	private void createConfig()
	{
		Map<String,Object> defaults = new HashMap<>();
		
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
		
		//config = new TomlConfig(internalFilePath("config.toml"),defaults,"Dape config file");
		
		config = new YamlConfig(internalFilePath("config.yml"),defaults,"Dape config file");
	}
	
	private void configureLogger()
	{
		Logg.setHideVerbose(config.getBoolean("logger.hide_verbose.all"));
		Logg.setHideWarnings(config.getBoolean("logger.hide_warnings"));
		Logg.setHideErrors(config.getBoolean("logger.hide_errors"));
		Logg.setHideFatals(config.getBoolean("logger.hide_fatals"));
		Logg.setSilenceExceptions(config.getBoolean("logger.hide_exceptions"));
		Logg.setWriteExceptions(config.getBoolean("logger.write_warnings"));
		
		List<String> enabledVerboseClasses = (List<String>) config.getStringList("logger.hide_verbose.class");
		if(enabledVerboseClasses == null) { return; }
		
		Logg.setEnabledVerboseClasses(enabledVerboseClasses);
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
	 * Used to shutdown the server in times when the server is left in a state that cannot be recovered
	 * Shutting down the server prevents further data degradation and unpredictable server states
	 */
	public static void forceShutdown(String reason)
	{
		Logg.error("The server is being forcefully shutdown. Reason: " + reason);
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
	
	public static Path featureFilePath(String path)
	{
		Path p = Paths.get(Dape.instance().getDataFolder().getPath() + File.separator + "feature" + File.separator + path);		
		return p; 
	}
	
	/**
	 * Returns an internal file path for dapes plugin data folder with an appended directory path
	 * @param path Appended directory path starting from ./plugins/Dape/
	 * @return Path of directory or file internal to dapes plugin data directory
	 */
	public static Path internalFilePath(String path)
	{
		Path p = Paths.get(Dape.instance().getDataFolder().getPath() + File.separator + path);		
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
	
	public static NamespacedKey getNamespacedKey()
	{
		return new NamespacedKey(Dape.instance(),getNamespaceName());
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
	
	public static DapeConfig getConfigFile()
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
	
	/**
	 * Signs an entity making it identify as plugin managed
	 * @param e Entity
	 */
	public static void setAsPluginManaged(Entity e)
	{
		DataUtils.set(DT_PLUGIN_MANAGED_ENTITY,DType.INTEGER,1,e);
	}
	
	/**
	 * Checks if this entity is managed by this plugin by looking for a data tag
	 * @param e Entity to check
	 * @return True if this a plugin managed entity, false otherwise
	 */
	public static boolean isPluginManaged(Entity e)
	{
		return DataUtils.has(NAMESPACE_NAME + ":" + DT_PLUGIN_MANAGED_ENTITY,e);
	}

	private final String[] logo = new String[]
	{
		"          &c                 ++++++-+++++++++-                 &r\r\n",
		"          &c             --+##++-++###########++--             &r\r\n",
		"          &c          +-++###     ++########## +---+-          &r\r\n",
		"          &c        -++######++-   -+#########+   +#+++        &r\r\n",
		"          &c      -++##########++-  ++########-   -#####+      &r\r\n",
		"          &c     -+##############+++ ++#######-   +### #+-               &8[ &cD A P E &8]&r\r\n",
		"          &c   -+++---++###########++ ########-  -+######++-   &r\r\n",
		"          &c  -++-     +----++######++ #######-  +#########+-            &9Version &8> &e" + getDescription().getVersion() + "&r\r\n",
		"          &c -+#+            ++++############+  -+#######++++-           &9Message Prefix &8> &8[&cD&8]&r\r\n",
		"          &c +##++----------++###############+ ++#######+- -++           &9Spigot API &8> &e" + getDescription().getAPIVersion() + "&r\r\n",
		"          &c.+############################### ++#######+-   ++.          &9Contributors &8> &8[&e" + String.join(",",getDescription().getAuthors()) + "&8]&r\r\n",
		"          &c-+##+############++++#######+#+###+#####++--    ++-          &9Bukkit Ver &8> &e" + Bukkit.getBukkitVersion() + "&r\r\n",
		"          &c-+#++######+###+####+#+++++++##++++###++--   .-+#+-&r\r\n",
		"          &c-######+--++#  +##############+####++++#   --+####-&r\r\n",
		"          &c-+#+++-.   +###+#############+#++##+  #+--+####+#+-&r\r\n",
		"          &c-++---   .-++++++++++++++++++####+#+++++++++++++#+-&r\r\n",
		"          &c-++    .-+++++++++#+###+###+++++++++++++++++++++++.&r\r\n",
		"          &c.++   -++++++++++++++++++++++++++++++++++++++++ ++.&r\r\n",
		"          &c -++#-+++++++++- ++++++#+++++++###+-.........--++- &r\r\n",
		"          &c .++++++++++++. .+++++++++++++++++            +++. &r\r\n",
		"          &c  --+++++++++-  .+++++++ -+++++++++-....     .+--  &r\r\n",
		"          &c   .-++++++++.  .++++++++ --+++++++++++++...---.   &r\r\n",
		"          &c     .++ +++-   .++++++++. +-+++++++++++++ ++.     &r\r\n",
		"          &c      .-++#+.   .++++++++++  .-+++++++++#++-.      &r\r\n",
		"          &c        .--+-   -+++++++++-.   ..--+++++--.        &r\r\n",
		"          &c          ...... ++++++++++-.      ++--..          &r\r\n",
		"          &c             ...-+++++++++++--........             &r\r\n",
		"          &c                 .................                 &r\r\n",
	};	
}