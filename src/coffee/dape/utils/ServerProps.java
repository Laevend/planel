package coffee.dape.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * @author Laeven
 * Acts as a read-only for the server.properties file
 */
public class ServerProps
{
	private static Properties props;
	
	static
	{
		Logg.fatal("READING FROM SERVER.PROPERTIES");
		try(FileReader fr = new FileReader("server.properties"); BufferedReader br = new BufferedReader(fr))
        {
			props = new Properties();
        	props.load(br);
        	
        	br.close();
        	fr.close();
        	Logg.fatal("DONE!");
        	
        	for(Entry<Object, Object> p : props.entrySet())
        	{
        		Logg.info("Key:" + p.getKey() + "Val:" + p.getValue());
        	}
        }
        catch(IOException e)
        {
        	Logg.error("Could not read server.properties from disk!",e);
        }
	}
	
	public static boolean accepts_transfers()
	{
		return getValue("accepts-transfers",false);
	}

	public static boolean allow_flight()
	{
		return getValue("allow-flight",false);
	}

	public static boolean allow_nether()
	{
		return getValue("allow-nether",true);
	}

	public static boolean broadcast_console_to_ops()
	{
		return getValue("broadcast-console-to-ops",true);
	}

	public static boolean broadcast_rcon_to_ops()
	{
		return getValue("broadcast-rcon-to-ops",true);
	}

	public static String difficulty()
	{
		return getValue("difficulty","easy");
	}

	public static boolean enable_command_block()
	{
		return getValue("enable-command-block",false);
	}

	public static boolean enable_jmx_monitoring()
	{
		return getValue("enable-jmx-monitoring",false);
	}

	public static boolean enable_query()
	{
		return getValue("enable-query",false);
	}

	public static boolean enable_rcon()
	{
		return getValue("enable-rcon",false);
	}

	public static boolean enable_status()
	{
		return getValue("enable-status",true);
	}

	public static boolean enforce_secure_profile()
	{
		return getValue("enforce-secure-profile",true);
	}

	public static boolean enforce_whitelist()
	{
		return getValue("enforce-whitelist",false);
	}

	public static int entity_broadcast_range_percentage()
	{
		return getValue("entity-broadcast-range-percentage",100);
	}

	public static boolean force_gamemode()
	{
		return getValue("force-gamemode",false);
	}

	public static int function_permission_level()
	{
		return getValue("function-permission-level",2);
	}

	public static String gamemode()
	{
		return getValue("gamemode","survival");
	}

	public static boolean generate_structures()
	{
		return getValue("generate-structures",true);
	}

	public static String generator_settings()
	{
		return getValue("generator-settings","{}");
	}

	public static boolean hardcore()
	{
		return getValue("hardcore",false);
	}

	public static boolean hide_online_players()
	{
		return getValue("hide-online-players",false);
	}

	public static String initial_disabled_packs()
	{
		return getValue("initial-disabled-packs","");
	}

	public static String initial_enabled_packs()
	{
		return getValue("initial-enabled-packs","vanilla");
	}

	public static String level_name()
	{
		return getValue("level-name","world");
	}

	public static String level_seed()
	{
		return getValue("level-seed","");
	}

	public static String level_type()
	{
		return getValue("level-type","minecraft:normal");
	}

	public static boolean log_ips()
	{
		return getValue("log-ips",true);
	}

	public static int max_chained_neighbor_updates()
	{
		return getValue("max-chained-neighbor-updates",1000000);
	}

	public static int max_players()
	{
		return getValue("max-players",20);
	}

	public static long max_tick_time()
	{
		return getValue("max-tick-time",60000L);
	}

	public static int max_world_size()
	{
		return getValue("max-world-size",29999984);
	}

	public static String motd()
	{
		return getValue("motd","A Minecraft Server");
	}

	public static int network_compression_threshold()
	{
		return getValue("network-compression-threshold",256);
	}

	public static boolean online_mode()
	{
		return getValue("online-mode",true);
	}

	public static int op_permission_level()
	{
		return getValue("op-permission-level",4);
	}

	public static int player_idle_timeout()
	{
		return getValue("player-idle-timeout",0);
	}

	public static boolean prevent_proxy_connections()
	{
		return getValue("prevent-proxy-connections",false);
	}

	public static boolean pvp()
	{
		return getValue("pvp",true);
	}

	public static int query_port()
	{
		return getValue("query.port",25565);
	}

	public static int rate_limit()
	{
		return getValue("rate-limit",0);
	}

	public static String rcon_password()
	{
		return getValue("rcon.password","");
	}

	public static int rcon_port()
	{
		return getValue("rcon.port",25575);
	}

	public static String region_file_compression()
	{
		return getValue("region-file-compression","deflate");
	}

	public static boolean require_resource_pack()
	{
		return getValue("require-resource-pack",false);
	}

	public static String resource_pack()
	{
		return getValue("resource-pack","");
	}

	public static String resource_pack_id()
	{
		return getValue("resource-pack-id","");
	}

	public static String resource_pack_prompt()
	{
		return getValue("resource-pack-prompt","");
	}

	public static String resource_pack_sha1()
	{
		return getValue("resource-pack-sha1","");
	}

	public static String server_ip()
	{
		return getValue("server-ip","");
	}

	public static int server_port()
	{
		return getValue("server-port",25565);
	}

	public static int simulation_distance()
	{
		return getValue("simulation-distance",10);
	}

	public static boolean spawn_animals()
	{
		return getValue("spawn-animals",true);
	}

	public static boolean spawn_monsters()
	{
		return getValue("spawn-monsters",true);
	}

	public static boolean spawn_npcs()
	{
		return getValue("spawn-npcs",true);
	}

	public static int spawn_protection()
	{
		return getValue("spawn-protection",16);
	}

	public static boolean sync_chunk_writes()
	{
		return getValue("sync-chunk-writes",true);
	}

	public static String text_filtering_config()
	{
		return getValue("text-filtering-config","");
	}

	public static boolean use_native_transport()
	{
		return getValue("use-native-transport",true);
	}

	public static int view_distance()
	{
		return getValue("view-distance",10);
	}

	public static boolean white_list()
	{
		return getValue("white-list",false);
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T getValue(String key,T defaultValue)
	{
		if(defaultValue.getClass() == Boolean.class)
		{
			Object obj = Integer.parseInt(props.getProperty(key));
			return (T) obj;
		} 
		else if(defaultValue.getClass() == Integer.class)
		{
			Object obj = Long.parseLong(props.getProperty(key));
			return (T) obj;
		}
		else if(defaultValue.getClass() == Long.class)
		{
			Object obj = Boolean.parseBoolean(props.getProperty(key));
			return (T) obj;
		}
		
		// String by default
		return (T) props.getProperty(key);
	}
}
