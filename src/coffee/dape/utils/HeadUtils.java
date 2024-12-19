package coffee.dape.utils;

import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import coffee.dape.utils.minecraftprofile.MinecraftProfileCtrl;
import coffee.dape.utils.minecraftprofile.data.MinecraftProfile;


/**
 * 
 * @author Laeven
 *
 */
public class HeadUtils
{
	/* Format for player head
	{
	   display:
	   {
	      Name:"Laeven_"
	   },
	   SkullOwner:
	   {
	      Id:"1b176422-182b-4018-a1bc-d6603bd76487",
	      Properties:
	      {
	         textures:
	         [
	            {
	               Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2Q4NTZhNWM0MGU4ZTcyMDY0ZmQ5YmVmOGVjOTBhODZlZTEzMGE0ZGE0YmVlYWJkYmRiNjM0YjcyMmJjYjBiYSJ9fX0="
	            }
	         ]
	      }
	   }
	}
	*/
	
	/* Custom texture url format
	 * {"textures":{"SKIN":{"url":"http://textures.minecraft.net/texture/cd856a5c40e8e72064fd9bef8ec90a86ee130a4da4beeabdbdb634b722bcb0ba"}}}
	 */
	
	private static final String textureHeader = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv";	
	private static URL defaultTexture = null;
	private static final String defaultTextureString = "4e2ce3372a3ac97fdda5638bef24b3bc49f4facf751fe9cad645f15a7fb8397c";
	
	static
	{
	    try
	    {
	    	defaultTexture = URI.create("https://textures.minecraft.net/texture/4e2ce3372a3ac97fdda5638bef24b3bc49f4facf751fe9cad645f15a7fb8397c").toURL();
	    }
	    catch (Exception e)
	    {
	    	Logg.fatal("An error occured trying to create the default fallback texture URL!",e);
	    }
	}
	
	/**
	 * Create a skull with a custom texture
	 * @param textureString 
	 * @return Skull
	 */
	public static ItemStack getSkull(String textureString)
	{
		Objects.requireNonNull(textureString);
		return getSkull(textureString,UUID.randomUUID());
	}
	
	/**
	 * Create a head that uses a specific UUID (to allow them to stack)
	 * @param textureString base64 encoded texture. Example: 18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
	 * @param uuid customUUID Uses a specific uuid to prevent skulls of the same texture from not stacking in a players inventory
	 * @return Skull
	 */
	public static ItemStack getSkull(String textureString,UUID uuid)
	{
		Objects.requireNonNull(textureString);
		Objects.requireNonNull(uuid);
		ItemStack skull = ItemBuilder.of(Material.PLAYER_HEAD,1).create();
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		skullMeta.setOwnerProfile(createProfile(textureString,uuid));
		skull.setItemMeta(skullMeta);
		return skull;
	}
	
	/**
	 * Gets a skull of a players head
	 * @param p Player to get skull of
	 * @return Skull of a players head
	 */
	public static ItemStack getSkull(Player p)
	{
		Objects.requireNonNull(p);
		return getSkull(p.getUniqueId());
	}
	
	/**
	 * Gets a skull of a players head
	 * @param p Player to get skull of
	 * @return Skull of a players head
	 */
	public static ItemStack getSkull(UUID playerUUID)
	{
		Objects.requireNonNull(playerUUID);
		ItemStack skull = ItemBuilder.of(Material.PLAYER_HEAD,1).create();
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		skullMeta.setOwnerProfile(createProfile(playerUUID));
		skull.setItemMeta(skullMeta);
		return skull;
	}
	
	/**
	 * Create a PlayerProfile with a custom skull texture
	 * @param textureString base64 encoded texture. Example: 18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
	 * @param uuid customUUID Uses a specific uuid to prevent skulls of the same texture from not stacking in a players inventory
	 * @return PlayerProfile
	 */
	private static PlayerProfile createProfile(String textureString,UUID uuid)
	{
		Objects.requireNonNull(textureString);
		Objects.requireNonNull(uuid);
	    PlayerProfile profile = Bukkit.createPlayerProfile(uuid);
	    PlayerTextures textures = profile.getTextures();
	    URL urlObject = null;
	    
	    try
	    {
	    	// The URL to the skin, for example: https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
	        urlObject = URI.create("https://textures.minecraft.net/texture/" + textureString).toURL();
	    }
	    catch (Exception e)
	    {
	    	Logg.error("An error occured trying to create the texture URL 'https://textures.minecraft.net/texture/" + textureString + "'!",e);
	    	textures.setSkin(defaultTexture);
		    profile.setTextures(textures);
		    return profile;
	    }
	    
	    textures.setSkin(urlObject);
	    profile.setTextures(textures);
	    return profile;
	}
	
	/**
	 * Create a PlayerProfile with a players skull texture
	 * @param playerUUID
	 * @return PlayerProfile
	 */
	private static PlayerProfile createProfile(UUID playerUUID)
	{
		Objects.requireNonNull(playerUUID);
		PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
	    PlayerTextures textures = profile.getTextures();
	    URL urlObject = getTextureOfPlayersSkull(playerUUID);
	    
	    if(urlObject == null) { return profile; }
	    
	    textures.setSkin(urlObject);
	    profile.setTextures(textures);
	    return profile;
	}
	
	/**
	 * Retrieves a players skull texture, regardless if they're online or have ever joined the server
	 * @param playerUUID UUID of player
	 * @return players URL skull texture
	 */
	private static URL getTextureOfPlayersSkull(UUID playerUUID)
	{
		Objects.requireNonNull(playerUUID);
		MinecraftProfile mcProfile = MinecraftProfileCtrl.getPlayerProfile(playerUUID,false);
		URL urlObject = null;
		
		try
	    {
	    	// The URL to the skin, for example: https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
	        urlObject = URI.create(mcProfile.getTexturesProperty().getSkinURL()).toURL();
	    }
	    catch (Exception e)
	    {
	    	Logg.error("An error occured trying to create the texture URL '" + mcProfile.getTexturesProperty().getSkinURL() + "'!",e);
		    return null;
	    }
		
		return urlObject;
	}
	
	/**
	 * Extracts a texture
	 * @param skull
	 * @return
	 */
	public static String extractTexture(ItemStack skull)
	{
		Objects.requireNonNull(skull);
		if(skull.getType() == Material.PLAYER_HEAD)
		{
			Logg.error("ItemStack is not of player head type!");
			return defaultTextureString;
		}
		
		SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
		PlayerProfile profile = skullMeta.getOwnerProfile();
		PlayerTextures textures = profile.getTextures();
		return textures.getSkin().toString().replace("https://textures.minecraft.net/texture/","");
	}
	
	public static ItemStack convertItemStackToHead(ItemStack skull,ItemStack stackToConvert)
	{
		Objects.requireNonNull(skull);
		Objects.requireNonNull(stackToConvert);
		stackToConvert.setType(Material.PLAYER_HEAD);
		SkullMeta skullMeta = (SkullMeta) stackToConvert.getItemMeta();
		skullMeta.setOwnerProfile(createProfile(extractTexture(skull),UUID.randomUUID()));
		stackToConvert.setItemMeta(skullMeta);
		return stackToConvert;
	}
	
	/**
	 * Gets the texture header
	 * @return
	 */
	public static String getHeader()
	{
		return textureHeader;
	}
	
	// Common Used Heads
	
	public static ItemStack SPEECH_BUBBLE = ItemBuilder.ofSkull("b02af3ca2d5a160ca1114048b7947594269afe2b1b5ec255ee72b683b60b99b9").create();
	public static ItemStack LEFT_ARROW = ItemBuilder.ofSkull("37aee9a75bf0df7897183015cca0b2a7d755c63388ff01752d5f4419fc645").create();
	public static ItemStack RIGHT_ARROW = ItemBuilder.ofSkull("682ad1b9cb4dd21259c0d75aa315ff389c3cef752be3949338164bac84a96e").create();	
	public static ItemStack DISCORD_LOGO = ItemBuilder.ofSkull("4d42337be0bdca2128097f1c5bb1109e5c633c17926af5fb6fc20000011aeb53").create();
	public static ItemStack RED_X = ItemBuilder.ofSkull("27548362a24c0fa8453e4d93e68c5969ddbde57bf6666c0319c1ed1e84d89065").create();
	public static ItemStack RED_X_OLD = ItemBuilder.ofSkull("beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7").create();
	public static ItemStack MAGNIFYING_GLASS = ItemBuilder.ofSkull("fc35e8684c7f776befedc4319d08148c54bea39321e1bd5def7a55b89fdaa099").create();
	public static ItemStack LIME_CHECK = ItemBuilder.ofSkull("a79a5c95ee17abfef45c8dc224189964944d560f19a44f19f8a46aef3fee4756").create();
	public static ItemStack LIME_CHECK_OLD = ItemBuilder.ofSkull("a92e31ffb59c90ab08fc9dc1fe26802035a3a47c42fee63423bcdb4262ecb9b6").create();
	public static ItemStack INFO_ICON = ItemBuilder.ofSkull("d01afe973c5482fdc71e6aa10698833c79c437f21308ea9a1a095746ec274a0f").create();
	public static ItemStack EXCLAMATION_MARK = ItemBuilder.ofSkull("b6e522d918252149e6ede2edf3fe0f2c2c58fee6ac11cb88c617207218ae4595").create();
	public static ItemStack QUESTION_MARK = ItemBuilder.ofSkull("2705fd94a0c431927fb4e639b0fcfb49717e412285a02b439e0112da22b2e2ec").create();
	public static ItemStack QUESTION_MARK_OLD = ItemBuilder.ofSkull("89a995928090d842d4afdb2296ffe24f2e944272205ceba848ee4046e01f3168").create();
	public static ItemStack SETTINGS_ICON = ItemBuilder.ofSkull("5949a18cb52c293fe7de7ba1014671340ed7ff8e5d705b2d60bf84d53148e04").create();
	public static ItemStack COLOUR_PICKER_ICON = ItemBuilder.ofSkull("c7ff1377754563ab41b8a0305dac03de63e02e5a39a6956afd6ccabf295a96d8").create();
	public static ItemStack BELL_ICON = ItemBuilder.ofSkull("e03780dfc2b1bbf1abf0f31d9ea2e5c78593118e85febe6eb9e90a0a281b00be").create();
	public static ItemStack INCOGNITO = ItemBuilder.ofSkull("a34f6a35071bb31dba7e965fedff0e8edd717600c5e14f3d6993c96b57a602fe").create();
	public static ItemStack REFRESH = ItemBuilder.ofSkull("11d720cd39df3be74b0cac75e3937f0085a37824743cad6330dc9f4666a4510d").create();
	public static ItemStack GLOBE = ItemBuilder.ofSkull("a0e555e42a1b2f85ef2d75668257676bc3b4d4f9ad88af4189f1f562cd39343").create();
	public static ItemStack CUBE_SPLIT_27 = ItemBuilder.ofSkull("9f13197c6a7cf52570fe564aab9944a9043bdf7d957479311ece0225e1d38df4").create();
	public static ItemStack CYAN_S = ItemBuilder.ofSkull("c92675b74daefd7ee86045fc6580fb6aa6c64f979a125b20ff6f8ea9496ce").create();
	public static ItemStack LIME_S = ItemBuilder.ofSkull("8d359aeb767e914521ff5b9d2c685db54871dc3f7e8f72f6eb8c1f2ca8c2d2").create();
	public static ItemStack GREY_V = ItemBuilder.ofSkull("9df53a18c59683fd63f6822cd4464d12c862d7818c68b2302857b719f55e4fe9").create();
	public static ItemStack BUG = ItemBuilder.ofSkull("d5bee915b88f1e4b9e826bedfd240480b8efe4aaeca200de1444426d9cf20056").create();
	public static ItemStack ARROW_DOWN = ItemBuilder.ofSkull("72431911f4178b4d2b413aa7f5c78ae4447fe9246943c31df31163c0e043e0d6").create();
	public static ItemStack TOKEN_BAG = ItemBuilder.ofSkull("693be1815b6c2a184644ba83bd587c778a50057cfc87d67de22f66441568e08c").create();
	public static ItemStack CLOCK = ItemBuilder.ofSkull("fcb8f06885d1daafd26cd95b3482cb525d881a67e0d247161b908d93d56d114f").create();
	
	public static ItemStack CHEST = ItemBuilder.ofSkull("cdbca4b69eaf8dcb7ac3728228de8a64440787013342ddaabc1b00eeb8eec1e2").create();
	public static ItemStack COPPER_CHEST = ItemBuilder.ofSkull("35e4cb4fc14ba69d49b2dd2b0418d6562dffdebd0ebf0165c4416a073e62ddf8").create();
	public static ItemStack IRON_CHEST = ItemBuilder.ofSkull("24b953b2c0e952574f1ed29c81e82e53bcdb1ba683259c20daeef7d554a2a798").create();
	public static ItemStack GOLD_CHEST = ItemBuilder.ofSkull("844498a0fe278956e3d04135ef4b1343d0548a7e208c61b1fb6f3b4dbc240da8").create();
	public static ItemStack DIAMOND_CHEST = ItemBuilder.ofSkull("31f7cdfea2d21cd5f6ebbf48481761c6cbdf36d00fe64083686e9aeaa3f1f217").create();
	public static ItemStack EMERALD_CHEST = ItemBuilder.ofSkull("4ba55671f97ff3bfc5be335ae92cd9749abd619e7afc2a6673597b80b755c741").create();
	public static ItemStack NETHERITE_CHEST = ItemBuilder.ofSkull("71a912e332fc0010bebd0f93a14d8e3ea65d1301100cea3fc5ae71990d985807").create();
	public static ItemStack COMMAND_BLOCK = ItemBuilder.ofSkull("5f4c21d17ad636387ea3c736bff6ade897317e1374cd5d9b1c15e6e8953432").create();
	
	public static ItemStack EMOTE_WINK = ItemBuilder.ofSkull("f4ea2d6f939fefeff5d122e63dd26fa8a427df90b2928bc1fa89a8252a7e").create();
	public static ItemStack EMOTE_SUGAR_RUSH = ItemBuilder.ofSkull("6990d9ef0cc5f35f45ade85021ba7e996e71f01dec8ee0740163b271269d8f55").create();
	public static ItemStack ONLINE = ItemBuilder.ofSkull("f5be49bbdd1db35def04ad11f06deaaf45c9666c05bc02bc8bf1444e99c7e").create();
	public static ItemStack BOMB = ItemBuilder.ofSkull("9b20ff173bd17b2c4f2eb21f3c4b43841a14b31dfbfd354a3bec8263af562b").create();
	public static ItemStack ALEX_FACE = ItemBuilder.ofSkull("6c85b43c0be7ba5d9b2ebd6034acc92960e5402962f8850cd042aab58c81cb0b").create();
	public static ItemStack JEWL = ItemBuilder.ofSkull("e80e2c206f06b4163dee678162b2d7a3ed6b20c6419fec44f91f6dd001a12a39").create();
	public static ItemStack DOWNLOAD_ICON = ItemBuilder.ofSkull("ce1f3cc63c73a6a1dde72fe09c6ac5569376d7b61231bb740764368788cbf1fa").create();
	public static ItemStack UPLOAD_ICON = ItemBuilder.ofSkull("a47d1dd4a7daff2aaf28e6a12a01f42d7e51593ef3dea762ef81847b1d4c5538").create();
	public static ItemStack PURPLE_TROPHY = ItemBuilder.ofSkull("55dfa284aa15324e5178561f803f5976228d95115583ab031266ae24ee1a99d1").create();
	public static ItemStack BOLD_ICON = ItemBuilder.ofSkull("242bed9ecfdbdba9d0064e3936168c0ce684cc346610d2097d42944ebf81ecc9").create();
	public static ItemStack ITALIC_ICON = ItemBuilder.ofSkull("bf3b8158e4c3d717c0f1061bc5a4ba34986bb1851c021e5b5070c62d312e2254").create();
	public static ItemStack UNDERLINE_ICON = ItemBuilder.ofSkull("7f328c3afe7f3e8c1bae8699e3dcace0bb63b43145941217551cfd6e65853f86").create();
	public static ItemStack STRIKETHROUGH_ICON = ItemBuilder.ofSkull("4122111ed2c1ac03799e4463ce5a86908372a219292129f43c36f5e77ccf0c5b").create();
	public static ItemStack RED_FLAG_ICON = ItemBuilder.ofSkull("48fd7126cd670c7971a285734edfdd802572a72a3f05ea41ccda4943ba373471").create();
	public static ItemStack LIKE_ICON = ItemBuilder.ofSkull("91c2f928c4abe31e342c80c71fef723e9905717f498dd746ebf94179889a75c3").create();
	public static ItemStack DISLIKE_ICON = ItemBuilder.ofSkull("c57e781151f4070fad7e544538bdd3492a1f548f2abbd3b390e600f589abe215").create();
	public static ItemStack TRASH_ICON = ItemBuilder.ofSkull("ca903f9026c785446fc452ed2d864389e7822d4adc087cf725e22e1d9f2990ea").create();
	public static ItemStack LOCK_OPEN = ItemBuilder.ofSkull("94d61ebc29c609724053e42f615bc742a16ef686961829a6d012704529b13085").create();
	public static ItemStack LOCK_CLOSED = ItemBuilder.ofSkull("2ecb62c63b2575ec8db771c57c8b560515bb504190238a961e6e243ef5602ed4").create();
	public static ItemStack HAYBALE = ItemBuilder.ofSkull("26459be09998e50abd2ccf4cd383e6b38ab5bc905facb66dce0e14e038ba1968").create();
	public static ItemStack GLOW_SQUID = ItemBuilder.ofSkull("e51ec3996fbcba0eb74c55a36a924ac0c0b15a75c8d82cd3e0e375b97e2b5fb5").create();

	public static ItemStack REDSTONE_RED = ItemBuilder.ofSkull("cef119f08851a72a5f10fbc3247d95e1c006360d2b4f412b23ce054092756b0c").create();
	public static ItemStack REDSTONE_GREEN = ItemBuilder.ofSkull("9b5871c72987266e15f1be49b1ec334ef6b618e9653fb78e918abd39563dbb93").create();
	
	public static ItemStack LEFT_GREEN_ARROW = ItemBuilder.ofSkull("afa4c82710837480df575ca0d64cef2fcdadaece7091b7076b923c67e5f4e849").create();
	public static ItemStack RIGHT_GREEN_ARROW = ItemBuilder.ofSkull("6395119dd5201a242b86b4866d6f04541b00b92ebdd57ce27919fb5f102a6ddd").create();
	public static ItemStack UP_GREEN_ARROW = ItemBuilder.ofSkull("f1d4fdd091840d9e7df0601681addec6051485a484ba7f536b35d4e05aa86ef9").create();
	public static ItemStack DOWN_GREEN_ARROW = ItemBuilder.ofSkull("1ab8865d995b609b3ffee0a3ed2e65440e35440d2c65514fedb2c5b2c687f85d").create();
	
	public static ItemStack CLAIM_CLUSTER = ItemBuilder.ofSkull("f1c9de1e420d65c21819e0a0c7ca5817c930052127b44a1334a246997f75e84f").create();
	public static ItemStack LOCAL_CLUSTER = ItemBuilder.ofSkull("6131a36e70ffaa7ca7e672ae6ac20b7fc1e457c43a8e1069e7b14ecdb8576").create();
	public static ItemStack LOCAL_CHUNK = ItemBuilder.ofSkull("6f32c92268dca04d723ff14d11f4b5273d08a8afe53a5256228f694ef882191c").create();
	
	public static ItemStack FLOATING_DIGITAL_CLOCK = ItemBuilder.ofSkull("2063dfa15c6d8da506a2d93414763cb1f819386d2cf6543c08e232f163fb2c1c").create();
	public static ItemStack MONITOR = ItemBuilder.ofSkull("15aecd80b53f1c13cd61547b092bf693f56fa4aa3c973893f7b8c0dbdc067952").create();
	public static ItemStack NIGHTCRAWLER = ItemBuilder.ofSkull("c6be5c115631946b7a798d212480efce334b13f09cec33621c3e4a30ca7a809a").create();
	public static ItemStack PRISMARINE_FLOWER_POT = ItemBuilder.ofSkull("95b105e6c22daa4ca6c109fdf8e9a48e3d566cafab194e808da83b07d9a1231d").create();
	public static ItemStack GAS_MASK = ItemBuilder.ofSkull("7b880372b3183e6c55408d2ce538500977baad1161197fa520106b5014a0e4ef").create();
	public static ItemStack MONITOR_BLACK = ItemBuilder.ofSkull("36a407070ea8bf2d7842bde6b6df5005e98df315ea66dffd171b0c75bc58630").create();
	public static ItemStack BEAR_WITH_HAT = ItemBuilder.ofSkull("b21cd656f52a9c53e23e4a8f85cff0f5dae84a3b5d2e564f1215b9841dc1fb05").create();
	public static ItemStack COMMAND_BLOCK_IMPULSE = ItemBuilder.ofSkull("3d8af980ff608867b6bc3736111c4c01b41f1ff6899a21e59858bce3d44dcf86").create();
	public static ItemStack COMMAND_BLOCK_REPEAT = ItemBuilder.ofSkull("b6edcaea89a41a94805af3421ca0df87a4b0ca675c04a2d117e8fe5a628d83f9").create();
	public static ItemStack COMMAND_BLOCK_CHAIN = ItemBuilder.ofSkull("4fe0e4f0609a2c5ad617a8c41d716e637d3d0572a2c007fe7d37ed50696b3ddb").create();
	public static ItemStack WHEAT_MONSTER = ItemBuilder.ofSkull("a933ceb9eb7dedd9aa4406f2029cf27d99af36babbd45950b1b23aa7268cec93").create();
	public static ItemStack CARROT_MONSTER = ItemBuilder.ofSkull("7d134aab5001e1e276b19762919a34c7e495ba9835132b19adf6f41a8cba6a72").create();
	public static ItemStack BEETROOT_MONSTER = ItemBuilder.ofSkull("a87e0d5c682d197b499ffc8780715daee196b91284a4f3498dee763ca1da47c0").create();
	public static ItemStack JACK_O_LANTERN = ItemBuilder.ofSkull("200c3aaff313d0c33ec7807920878f7e4a63b3e99d3c0e3410e1fec1f6784f3c").create();
	public static ItemStack CANDY_CORN = ItemBuilder.ofSkull("6c3a141f65d4101355e3ef0ed88da088e6d003ab6ec9106548ea13128911f334").create();
	public static ItemStack POTATO_MONSTER = ItemBuilder.ofSkull("c1c2f8fe61a61204bd36df86a21946c376288f6700c1e259b9115acce237fa55").create();
	public static ItemStack MELON_MONSTER = ItemBuilder.ofSkull("eeea5ff837d1acfbf5bac6239cf3cc9267d7c5f727281d956dd2e84a3e1c8c86").create();
	public static ItemStack BLACK_PLUS = ItemBuilder.ofSkull("9a2d891c6ae9f6baa040d736ab84d48344bb6b70d7f1a280dd12cbac4d777").create();
	public static ItemStack WHITE_PLUS = ItemBuilder.ofSkull("60b55f74681c68283a1c1ce51f1c83b52e2971c91ee34efcb598df3990a7e7").create();
	public static ItemStack WHITE_MINUS = ItemBuilder.ofSkull("c3e4b533e4ba2dff7c0fa90f67e8bef36428b6cb06c45262631b0b25db85b").create();
	public static ItemStack PLAYERS = ItemBuilder.ofSkull("257ed463876ed39ebf851ff6468cafce723421e80b1483302536b9556a0f7fe8").create();
	
	public static ItemStack BOOKS_GREY_STACKED_TOP = ItemBuilder.ofSkull("46ae1251d0d4eb6c8cc55cf65c92ecd59acf81e746c856ea64c7c742b06afe94").create();
	public static ItemStack BOOKS_GREY_STACKED_SIDEWAYS = ItemBuilder.ofSkull("8fb80dd4d2598a6fa0d643783e1aebf5de1945de8027afb48fef6107c69f3728").create();
	
	public static ItemStack LIME_P = ItemBuilder.ofSkull("c75a5dc31087f3a16f27a4ffcf2542ee8d522f25eb19d0895efc32cb9c2548").create();
	public static ItemStack RED_P = ItemBuilder.ofSkull("99081c38ea837e198fb7db2531507b82ca2624f9b5d7d856e57d992c58e9d238").create();
	
	// Map Icons for future travel thing? (Other worlds players can visit)
	public static ItemStack GRASS_BLOCK_DEEP_DARK = ItemBuilder.ofSkull("55f4a858eaed4552dceaffbe4d7fc0db3b6f81c0bc64f85f8b7ea76ef720e472").create();
	public static ItemStack SOUL_BLOCK = ItemBuilder.ofSkull("d19c65aca08741ef9d22941e5d4fec75f61d9c0e95baedabb7f7cb42f81e7abc").create();
	


	public static final class Mobs
	{
		public static final ItemStack ALLAY = ItemBuilder.ofSkull("cc0389177dbaa92f0d5ffdf848862c7f9b36df222fbfd737e2639dc3059e0cf3",UUID.fromString("35029c79-88e6-4e34-abee-d98eac0e8550")).name("&eAllay").create();

		public static class Axolotls
		{
			public static final ItemStack LUCY_AXOLOTL = ItemBuilder.ofSkull("667e15eab73064b6680d1db98ba445ed0914ba35a799997c0da2b03ffc3a8826",UUID.fromString("16ccf1cf-06d1-45f1-8c0d-e4af8352114e")).name("&eLucy Axolotl").create();
			public static final ItemStack WILD_AXOLOTL = ItemBuilder.ofSkull("47cf0274998bf5a7f38b37036e154f112fa2e28bad40d5a7c94765fe4f52211e",UUID.fromString("2207ba82-6578-47c8-ba12-4bdf6f093e06")).name("&eWild Axolotl").create();
			public static final ItemStack GOLD_AXOLOTL = ItemBuilder.ofSkull("e58560115faad11619b3d55de79ef2a053f478a67194bbe9247edea0bc98e834",UUID.fromString("15d137ec-d84a-47b1-88b0-75c3e129d97a")).name("&eGold Axolotl").create();
			public static final ItemStack CYAN_AXOLOTL = ItemBuilder.ofSkull("851196d43930659d717e1b6a046a08d1220fcb4e31c4856bc33e7551986ef1d",UUID.fromString("65e919f7-1ce4-42ae-a1ec-869db1287518")).name("&eCyan Axolotl").create();
			public static final ItemStack BLUE_AXOLOTL = ItemBuilder.ofSkull("68fd10b0fef4595960b1f64193bc8a1865a2d2ed48b2e2ce03d994563027df95",UUID.fromString("3f8f0a95-493e-4751-8ef3-4b4e6fbaa18e")).name("&eBlue Axolotl").create();
		}

		public static ItemStack BAT = ItemBuilder.ofSkull("8eb81c40b5b63f3d830340f8fcc4aab538d4e544e95eec9d70d61f786f6b6974",UUID.fromString("8fd466ea-839d-4f60-93e2-e3414f0417b1")).name("&eBat").create();

		public static class Bees
		{
			public static final ItemStack BEE = ItemBuilder.ofSkull("59ac16f296b461d05ea0785d477033e527358b4f30c266aa02f020157ffca736",UUID.fromString("cc3d0b4f-a880-4b75-877b-53df6c784ed1")).name("&eBee").create();
			public static final ItemStack POLLINATED_BEE = ItemBuilder.ofSkull("b727d0ab03f5cd022f8705d3f7f133ca4920eae8e1e47b5074433a137e691e4e",UUID.fromString("956f943d-28eb-427e-8844-2064997fe92c")).name("&ePollinated Bee").create();
			public static final ItemStack ANGRY_BEE = ItemBuilder.ofSkull("e400223f1fa54741d421d7e8046409d5f3e15c7f4364b1b739940208f3b686d4",UUID.fromString("fe7e4748-8135-448a-8c14-fe97ef478187")).name("&eAngry Bee").create();
			public static final ItemStack ANGRY_POLLINATED_BEE = ItemBuilder.ofSkull("e6b74e052b74288799ba6d9f35c5d0221cf8b04331547ec2f68d73597ae2c9b",UUID.fromString("ba6503f2-b82d-4b04-b449-662e23886c33")).name("&eAngry Pollinated Bee").create();
		}

		public static final ItemStack BLAZE = ItemBuilder.ofSkull("dee23dc7a10c6a87ef937454c0e94ed42c23aa641a91ed8470a3042d05c52c52",UUID.fromString("de69c686-41d3-48c5-bdfe-6be811e945fc")).name("&eBlaze").create();
		
		public static final ItemStack BREEZE = ItemBuilder.ofSkull("cd6e602f76f80c0657b5aed64e267eeea702b31e6dae86346c8506f2535ced02",UUID.fromString("5ec4a817-3117-4a9a-92dd-d0b813ad754d")).name("&eBreeze").create();
		
		public static final ItemStack CAMEL = ItemBuilder.ofSkull("e67d4597340166e1978a668a06bf5756c17b4cb5b40ab8ff244093b6b8bc75d3",UUID.fromString("8fbcad9a-ef9e-42f6-9d95-c6e766e3bce6")).name("&eCamel").create();

		public static class Cats
		{
			public static final ItemStack TABBY_CAT = ItemBuilder.ofSkull("de28d30db3f8c3fe50ca4f26f3075e36f003ae8028135a8cd692f24c9a98ae1b",UUID.fromString("d3ca8dbe-be1a-4272-a2c6-293749fa5f26")).name("&eTabby Cat").create();
			public static final ItemStack TUXEDO_CAT = ItemBuilder.ofSkull("4fd10c8e75f67398c47587d25fc146f311c053cc5d0aeab8790bce36ee88f5f8",UUID.fromString("d05449e2-5f02-498c-b784-9391b0b92b4e")).name("&eTuxedo Cat").create();
			public static final ItemStack GINGER_CAT = ItemBuilder.ofSkull("2113dbd3c6a078a17b4edb78ce07d836c38dace5027d4b0a83fd60e7ca7a0fcb",UUID.fromString("d26ce498-7d72-4bdd-9f45-a82ee5356a19")).name("&eGinger Cat").create();
			public static final ItemStack SIAMESE_CAT = ItemBuilder.ofSkull("d5b3f8ca4b3a555ccb3d194449808b4c9d783327197800d4d65974cc685af2ea",UUID.fromString("8d7f5cc7-9430-4d24-bb92-31a7968e228d")).name("&eSiamese Cat").create();
			public static final ItemStack BRITISH_SHORT_HAIR_CAT = ItemBuilder.ofSkull("5389e0d5d3e81f84b570e2978244b3a73e5a22bcdb6874b44ef5d0f66ca24eec",UUID.fromString("23566313-3df5-48c0-bb13-d2bef31a53cc")).name("&eBritish Shorthair Cat").create();
			public static final ItemStack CALICO_CAT = ItemBuilder.ofSkull("340097271bb680fe981e859e8ba93fea28b813b1042bd277ea3329bec493eef3",UUID.fromString("e94dfd1a-157e-4da5-88b5-a0aaaf425323")).name("&eCalico Cat").create();
			public static final ItemStack PERSIAN_CAT = ItemBuilder.ofSkull("ff40c746260ef91c96b27159795e87191ae7ce3d5f767bf8c74faad9689af25d",UUID.fromString("8c35757b-43bb-41e8-bd2f-1ac2046b37c9")).name("&ePersian Cat").create();
			public static final ItemStack RAGDOLL_CAT = ItemBuilder.ofSkull("dc7a45d25889e3fdf7797cb258e26d4e94f5bc13eef00795dafef2e83e0ab511",UUID.fromString("14b78351-1801-4401-8426-7cb9d1c77c09")).name("&eRagdoll Cat").create();
			public static final ItemStack WHITE_CAT = ItemBuilder.ofSkull("21d15ac9558e98b89aca89d3819503f1c5256c2197dd3c34df5aac4d72e7fbed",UUID.fromString("081bb38e-4307-47a5-858f-54d6c7dcfed9")).name("&eWhite Cat").create();
			public static final ItemStack JELLIE_CAT = ItemBuilder.ofSkull("a0db41376ca57df10fcb1539e86654eecfd36d3fe75e8176885e93185df280a5",UUID.fromString("f8d71965-821b-4488-8d8a-ceb23f1f870e")).name("&eJellie Cat").create();
			public static final ItemStack BLACK_CAT = ItemBuilder.ofSkull("22c1e81ff03e82a3e71e0cd5fbec607e11361089aa47f290d46c8a2c07460d92",UUID.fromString("f766a058-37eb-42bf-8c95-7f9ec4743554")).name("&eBlack Cat").create();
		}

		public static final ItemStack CAVE_SPIDER = ItemBuilder.ofSkull("a6a1c2599fc91203a65a03d479c8dc87f662deac3663c16c5e04d625b3978a25",UUID.fromString("eedf00bc-f65e-4828-941e-f2a9a14765cd")).name("&eCave Spider").create();

		public static final ItemStack CHICKEN = ItemBuilder.ofSkull("42af6e5847eea099e1b0ab8c20a9e5f3c7190158bda54e28133d9b271ec0cb4b",UUID.fromString("bf55c373-e046-4dc9-9800-2cb976d82a67")).name("&eChicken").create();

		public static final ItemStack COD = ItemBuilder.ofSkull("f246e19b32cf784549447e07b96072e1f656d78e93ccca5637485e6749734652",UUID.fromString("484f975d-25e5-4b1c-86c7-41115e6dc966")).name("&eCod").create();

		public static final ItemStack COW = ItemBuilder.ofSkull("63d621100fea5883922e78bb448056448c983e3f97841948a2da747d6b08b8ab",UUID.fromString("8e364048-f7d8-4c6e-9a75-3b04ff4a522a")).name("&eCow").create();

		public static final ItemStack CREEPER = ItemBuilder.ofSkull("ef5c8a0891e4393c805b57ab7de558def4b00c637610eb4d0a58e8a41ae21dee",UUID.fromString("0ca9d900-c97b-4ca0-a8f5-df9ff165ea5d")).name("&eCreeper").create();
		
		public static final ItemStack CHARGED_CREEPER = ItemBuilder.ofSkull("3511e4a3d5add6a54499abad10d799d06ce45cba9e520afd2008608a6288b7e7",UUID.fromString("9fa07688-2576-4b89-8a10-a017d47da9e3")).name("&eCharged Creeper").create();

		public static final ItemStack DOLPHIN = ItemBuilder.ofSkull("8e9688b950d880b55b7aa2cfcd76e5a0fa94aac6d16f78e833f7443ea29fed3",UUID.fromString("a0d246aa-8449-4111-aa60-06092a5f5277")).name("&eDolphin").create();

		public static final ItemStack DONKEY = ItemBuilder.ofSkull("4e25ee9273ad579d44bf406f6f6295586481ea198fd572076cd0c5882da7e6cc",UUID.fromString("da67f9ba-456b-4823-8420-977c076c908b")).name("&eDonkey").create();

		public static final ItemStack DROWNED = ItemBuilder.ofSkull("c3f7ccf61dbc3f9fe9a6333cde0c0e14399eb2eea71d34cf223b3ace22051",UUID.fromString("97458afa-dec1-495e-8c36-11812536633d")).name("&eDrowned").create();

		public static final ItemStack ELDER_GUARDIAN = ItemBuilder.ofSkull("4a2d64f4a00e9c85f67262edcacb84523581ae0f37bdab22dd704524f62e169f",UUID.fromString("ba72ebf0-2d68-4dc8-8155-03371be3e0f5")).name("&eElder Guardian").create();

		public static final ItemStack ENDERMAN = ItemBuilder.ofSkull("8977a94f02498cad0cfdb65ca7cb72e23111a914d8c670acccc7a65b347d7776",UUID.fromString("73c7e86b-65e2-43ee-824e-771c62c2e7b4")).name("&eEnderman").create();

		public static final ItemStack ENDERMITE = ItemBuilder.ofSkull("8c6b65c22b4465ba6793b2195cd5084ce83b88dca6e55eb9484540acd7352a50",UUID.fromString("54ec1e66-75df-4cf9-b65c-ac21e72aa76a")).name("&eEndermite").create();

		public static final ItemStack EVOKER = ItemBuilder.ofSkull("390fbd88f659d3963c68cbbcb7c7121d8195a8be65bbd2bf1257d1f69bccc0c7",UUID.fromString("8776f9d6-6e1a-43eb-ad3e-8bd02b6cc1a9")).name("&eEvoker").create();

		public static class Foxes
		{
			public static final ItemStack FOX = ItemBuilder.ofSkull("d7e0043111bc57090856259155571c7906e707046df041b8b572704c451fcd82",UUID.fromString("af220f49-ece8-48cb-8c5e-43cb4cb641dd")).name("&eFox").create();
			public static final ItemStack SNOW_FOX = ItemBuilder.ofSkull("41436377eb4c4b4e39fb0e1ed8899fb61ee1814a9169b8d08729ef01dc85d1ba",UUID.fromString("de21a9b3-f68a-4dc4-8eac-ea897cc39e90")).name("&eSnow Fox").create();
		}

		public static class Frogs
		{
			public static final ItemStack COLD_FROG = ItemBuilder.ofSkull("76877893e920ff5dfa4b5fbd14dabee2e6308a6f97c3a19b08e241a29eb9a5c3",UUID.fromString("00ee669a-e91c-43e3-8a64-c84c6690119f")).name("&eCold Frog").create();
			public static final ItemStack TEMPERATE_FROG = ItemBuilder.ofSkull("a50d1073d41f193405d95b1d941f9fe1a7ff080e38155d7bb780bbbd8e86f70d",UUID.fromString("46cf55a8-cc3d-4a32-bbb8-04c1319f232e")).name("&eTemperate Frog").create();
			public static final ItemStack WARM_FROG = ItemBuilder.ofSkull("d5b0da43975b83c3322788dda317506333843aebe5512787cb2e3d769ed2b382",UUID.fromString("b42c6957-8c77-4f10-aba9-66e2b0f672ce")).name("&eWarm Frog").create();
		}

		public static final ItemStack GHAST = ItemBuilder.ofSkull("c53de31a2d0041a6ef75bf7a6c8468464db1aaa6201ebb1a6013edb2245c7607",UUID.fromString("438a5436-8ba2-4523-a5a2-6bf8257fee32")).name("&eGhast").create();

		public static final ItemStack GLOW_SQUID = ItemBuilder.ofSkull("4b2e9b6581fed48a99e0323091ad5c3c326cda20073e28a902a0373f7382b59f",UUID.fromString("6965c98e-abf1-4766-bdd0-18875dd8a729")).name("&eGlow Squid").create();

		public static class Goats
		{
			public static final ItemStack GOAT = ItemBuilder.ofSkull("87473e055df6e7fd98664e9fdb63675f088106305d744024a41bb358918a142b",UUID.fromString("22fcc11b-6a0a-4dbf-8876-ff86acd1d782")).name("&eGoat").create();
			public static final ItemStack SCREAMING_GOAT = ItemBuilder.ofSkull("bda485ac23512420891a5ae1e8de989f091d848d15a9068da4720d316fc4330f",UUID.fromString("33678c43-18ff-45c7-bd76-9ee0ca8ccfc3")).name("&eScreaming Goat").create();
		}

		public static final ItemStack GUARDIAN = ItemBuilder.ofSkull("e2ba34416670454b1a20496f80b9398529f49003fc613eb930248ea9b5d1a391",UUID.fromString("731aa7fc-3107-4f41-bddd-84b055ab76a7")).name("&eGuardian").create();

		public static final ItemStack HOGLIN = ItemBuilder.ofSkull("bc4a7f57fc03b13aa2f9d83cdd4822b936793096daf51e78025bbd241ed6f68d",UUID.fromString("3a79efd3-ae03-4fd6-b9b9-52eb3129ef03")).name("&eHoglin").create();

		public static class Horses
		{
			public static final ItemStack WHITE_HORSE = ItemBuilder.ofSkull("c7bc61609730f2cb010268fab0821bd47352699750a150599f21c3fc4e92591a",UUID.fromString("e1ae854b-9e23-465c-9149-343455e041f2")).name("&eWhite Horse").create();
			public static final ItemStack CREAMY_HORSE = ItemBuilder.ofSkull("42a0d54cc071267d6bfd5f523f8c89dcfdc5e805fabbb76010cb3befa465aa94",UUID.fromString("fe5aaf3e-51a2-4c03-9367-27d249d7bcc2")).name("&eCreamy Horse").create();
			public static final ItemStack CHESTNUT_HORSE = ItemBuilder.ofSkull("6c8720d1f552693b40a9a33afa41cef06afd142833bed9fa5b887e88f05f49fa",UUID.fromString("2f355d8d-9fd7-4cf9-8fdf-4a3e0ff4f024")).name("&eChestnut Horse").create();
			public static final ItemStack BROWN_HORSE = ItemBuilder.ofSkull("6771800770cb4e814a3d91186fcd795ec82e06102ff7c1ee4e5c380102a0c70f",UUID.fromString("ba946393-2d18-4301-acca-a67bcb08644a")).name("&eBrown Horse").create();
			public static final ItemStack BLACK_HORSE = ItemBuilder.ofSkull("6723fa5be6ac2292a72230f5fd7ab663493bd8f7e64816424dc5bf24f133890c",UUID.fromString("b9d4d200-89ae-46f2-8517-4b5401033858")).name("&eBlack Horse").create();
			public static final ItemStack GRAY_HORSE = ItemBuilder.ofSkull("c25986102181083fb317bc5712f7104daa5a3e889264dfebb9159f6e08bac90c",UUID.fromString("560a9f85-2662-4439-9bc5-d15df1f481b0")).name("&eGray Horse").create();
			public static final ItemStack DARK_BROWN_HORSE = ItemBuilder.ofSkull("7f2341aaa0c82c22bbc207063e319291097c539adad9aa913eb8001b11aa59da",UUID.fromString("4c81de1a-f5ab-4a07-afb6-c1edebac7d1c")).name("&eDark Brown Horse").create();
		}

		public static final ItemStack HUSK = ItemBuilder.ofSkull("7338318bc91a36cd5ab6aa885c9a4ee2bdacdaa5c66b2a99dfb0a560983f2480",UUID.fromString("bbb39df2-2357-4531-a6b2-7eab3ff20edb")).name("&eHusk").create();

		public static final ItemStack ILLUSIONER = ItemBuilder.ofSkull("d382701c67d6c54c907558891dc176225112518771e061c5d8bd918479e6bdd8",UUID.fromString("13ff7f9d-e3b7-4f25-a1cf-7e70cea06071")).name("&eIllusioner").create();

		public static final ItemStack IRONGOLEM = ItemBuilder.ofSkull("fe7c0719fabe116dce605199bcadc69a538860864ef15706983ff662822d9fe3",UUID.fromString("1d3c7cba-d655-4855-a256-450f55c79da2")).name("&eIron Golem").create();

		public static class Llamas
		{
			public static final ItemStack CREAMY_LLAMA = ItemBuilder.ofSkull("4d67fd4bff293269cb908974dca83c33485e435ed5a8e1dbd6521c61687140",UUID.fromString("0e8bf33b-ca3a-4031-a6e6-53fee9eb28c5")).name("&eCremay Llama").create();
			public static final ItemStack WHITE_LLAMA = ItemBuilder.ofSkull("80277e6b3d9f7819efc7da4b42745f7ab9a63ba8f36d6b84a7a250c6d1a358eb",UUID.fromString("c68e55d0-6d01-468c-9ed9-82bedabec0a8")).name("&eWhite Llama").create();
			public static final ItemStack BROWN_LLAMA = ItemBuilder.ofSkull("c2b1ecff77ffe3b503c30a548eb23a1a08fa26fd67cdff389855d74921368",UUID.fromString("c39b5090-5def-47e2-a118-4c31fc8d6129")).name("&eBrown Llama").create();
			public static final ItemStack GRAY_LLAMA = ItemBuilder.ofSkull("cf24e56fd9ffd7133da6d1f3e2f455952b1da462686f753c597ee82299a",UUID.fromString("a5baa81d-ba88-4995-8393-eea1da69fa2f")).name("&eGray Llama").create();
		}

		public static final ItemStack MAGMACUBE = ItemBuilder.ofSkull("b81718d4984847a4ad3ec081a4ebffd183743239aecab60322138a72609812c3",UUID.fromString("6971e3f6-0af0-4ca1-91bb-d230748c96c4")).name("&eMagma Cube").create();

		public static final ItemStack MULE = ItemBuilder.ofSkull("41c224a1031be434d25ae1885bf4ff400c9894c69bfef56a49354c5625c0c09c",UUID.fromString("d85ec6c9-37a6-4560-b98b-9340407b367e")).name("&eMule").create();

		public static class Mooshrooms
		{
			public static final ItemStack RED_MOOSHROOM = ItemBuilder.ofSkull("da80606e82c642f141587733e3180ae57f646442c9fffd4e5997457e34311a29",UUID.fromString("e7b17db7-bbe1-4615-8183-11fa7d49977f")).name("&eRed Mooshroom").create();
			public static final ItemStack BROWN_MOOSHROOM = ItemBuilder.ofSkull("7e6466302a5ab4398b4e477349808e5d9402ea3ad8fc42e2446e4bed0a5ed5e",UUID.fromString("dc4ca787-8533-411d-99b7-85819de5b90b")).name("&eBrown Mooshroom").create();
		}

		public static final ItemStack OCELOT = ItemBuilder.ofSkull("9175cc43ea8ae20168a1f170810b4da4d9b4ebd3c9976e9fc22e9f995c3cbc3c",UUID.fromString("78ddf0ac-e2ec-496c-b108-00c52e7ccbd0")).name("&eOcelot").create();

		public static class Pandas
		{
			public static final ItemStack AGGRESSIVE_PANDA = ItemBuilder.ofSkull("e546e436d166b17f0521bd8538ea13cd6ee3b5df102eb32e3e425cb285d44063",UUID.fromString("8d267866-3062-4fda-97f5-87618249204e")).name("&eAggressive Panda").create();
			public static final ItemStack LAZY_PANDA = ItemBuilder.ofSkull("587f1f5db2e24df4daaed4685d6aee5deb7cdd029630f0079c1f8e1f9741acfd",UUID.fromString("c74525ce-237a-4a53-8097-4db1faecb02e")).name("&eLazy Panda").create();
			public static final ItemStack PLAYFUL_PANDA = ItemBuilder.ofSkull("8cadd4bf3c4cace916680e1fef90b5d16ad6643951725668ba6b4996b69ca140",UUID.fromString("b6545ae0-80a7-4ba7-a9f3-211ba6697d79")).name("&ePlayful Panda").create();
			public static final ItemStack WORRIED_PANDA = ItemBuilder.ofSkull("fb86fd1bf8cbce23bc08fb90691717611addc85ab823b7714aec98a5660eff15",UUID.fromString("3c4b5777-e55c-4b72-bda4-236b8f8f631f")).name("&eWorried Panda").create();
			public static final ItemStack BROWN_PANDA = ItemBuilder.ofSkull("1d5f6d6126728671b44c1c775f99617424e33611b5d31ad2acff2804eb96eb06",UUID.fromString("90c753a9-ad74-49a0-90d0-26a5fe8d2d3c")).name("&eBrown Panda").create();
			public static final ItemStack WEAK_PANDA = ItemBuilder.ofSkull("cc56a355fbe0e2fbd28e85c4d815ffa5d1f9d5f8798dbc259ff88c4addb202ae",UUID.fromString("23e74140-e0df-4707-8f71-74aaf1c6441a")).name("&eWeak Panda").create();
			public static final ItemStack PANDA = ItemBuilder.ofSkull("59df47e015d5c1c68d72be11bb656380fc6db533aab38941a91b1d3d5e396497",UUID.fromString("63264026-de0a-4fb0-bc06-4b9a0d42b307")).name("&ePanda").create();
		}

		public static class Parrots
		{
			public static final ItemStack RED_PARROT = ItemBuilder.ofSkull("40a3d47f54e71a58bf8f57c5253fb2d213f4f55bb7934a19104bfb94edc76eaa",UUID.fromString("59b21a34-b5b3-4f9d-92b5-751d46b4578c")).name("&eRed Parrot").create();
			public static final ItemStack BLUE_PARROT = ItemBuilder.ofSkull("b94bd3fcf4d46354ede8fef73126dbcab52b301a1c8c23b6cdfc12d612b61bea",UUID.fromString("bec72db6-b605-406e-951f-815b4449f709")).name("&eBlue Parrot").create();
			public static final ItemStack GREEN_PARROT = ItemBuilder.ofSkull("6a1dc33115232f800825cac9e3d9ed03fc18ae553c25b8059513000c59e354fe",UUID.fromString("93e53d3c-f631-4e3d-80d4-4cddc21423c6")).name("&eGreen Parrot").create();
			public static final ItemStack LIGHT_BLUE_PARROT = ItemBuilder.ofSkull("7268ce37be8507ed67e3d40b617e2d72f66f9d20b106efb08e6ba041f9b9ef10",UUID.fromString("cc3532ac-afdf-4ba2-8f8b-393030f9b2fa")).name("&eLight Blue Parrot").create();
			public static final ItemStack GRAY_PARROT = ItemBuilder.ofSkull("71be723aa17393d99daddc119c98b2c79c54b35debe05c7138edeb8d0256dc46",UUID.fromString("d60327b2-dd28-4f45-b559-15a38999c33d")).name("&eGray Parrot").create();
		}

		public static final ItemStack PHANTOM = ItemBuilder.ofSkull("7e95153ec23284b283f00d19d29756f244313a061b70ac03b97d236ee57bd982",UUID.fromString("d71fb2e4-f9f6-4e81-95f4-cd9283ea4f74")).name("&ePhantom").create();

		public static final ItemStack PIG = ItemBuilder.ofSkull("41ee7681adf00067f04bf42611c97641075a44ae2b1c0381d5ac6b3246211bfe",UUID.fromString("f5a86cd5-0ec5-4ed7-9edb-84eb35dad5c4")).name("&ePig").create();
		
		public static final ItemStack PIGLIN = ItemBuilder.ofSkull("a792b6997d739f535beed3ab1d4aeadfa76777bf8e38a666f54f82ff9f858186",UUID.fromString("1bafd9a1-82cb-4247-91e7-84f496181c02")).name("&ePiglin").create();

		public static final ItemStack PIGLINBRUTE = ItemBuilder.ofSkull("6488799c83ecb29452ceba89c3c0099219274ce5b2bfb8ad0b3ea4c65fac4630",UUID.fromString("04ca0b95-056c-49ae-8ad3-52ebfdd94234")).name("&ePiglin Brute").create();

		public static final ItemStack PILLAGER = ItemBuilder.ofSkull("c225f0b49c5295048a409c9c601cca79aa8eb52aff5e2033ebb865f4367ef43e",UUID.fromString("63e41015-2f8d-4ebd-838b-15dc45e90c96")).name("&ePillager").create();

		public static final ItemStack POLARBEAR = ItemBuilder.ofSkull("cd8702911e616c0d32fbe778d195f21ecce9025bcbd09151e3d97af3192aa7ec",UUID.fromString("b578a141-1521-4c3c-8401-11d93df964c6")).name("&ePolar Bear").create();

		public static final ItemStack PUFFERFISH = ItemBuilder.ofSkull("e2733d5da59c82eaf310b382aff40bd513c44354dbbabfe14b066a556810a7f9",UUID.fromString("9a082418-cc8f-4461-9eea-2cac2f7fff97")).name("&ePuffer Fish").create();

		public static class Rabbits
		{
			public static final ItemStack TOAST = ItemBuilder.ofSkull("51a57c3d0a9b10e13f66df74200cb8a6d484c672226812d74e25f6c027410616",UUID.fromString("31499336-1cca-494a-a276-7be83ff2bdf1")).name("&eToast").create();
			public static final ItemStack BROWN_RABBIT = ItemBuilder.ofSkull("cfd4f86cf7473fbae93b1e090489b64c0be126c7bb16ffc88c002447d5c72795",UUID.fromString("daf4dc5b-a9ef-44aa-8aaa-6a80ed8739f3")).name("&eBrown Rabbit").create();
			public static final ItemStack WHITE_RABBIT = ItemBuilder.ofSkull("9542d7160987148a5d8e20e469bd9b3c2a3946c7fb5923f55b9beae99185f",UUID.fromString("70f36365-11ed-44f2-bbc5-b6a5bbbc61a2")).name("&eWhite Rabbit").create();
			public static final ItemStack BLACK_RABBIT = ItemBuilder.ofSkull("b2b425ff2a236ab19cc9397195db40f8f185b191c40bf44b26e95eac9fb5efa3",UUID.fromString("f928798a-faa7-484c-8aa0-c50df41a0c9e")).name("&eBlack Rabbit").create();
			public static final ItemStack BLACK_AND_WHITE_RABBIT = ItemBuilder.ofSkull("35f72a2195ebf4117c5056cfe2b7357ec5bf832ede1856a7773ee42a0d0fb3f0",UUID.fromString("8442179c-46dc-4905-9f8f-bda23b65d1e2")).name("&eBlack and White Rabbit").create();
			public static final ItemStack GOLD_RABBIT = ItemBuilder.ofSkull("767b722656fdeec39974d3395c5e18b47c5e237bce5bbced9b7553aa14b54587",UUID.fromString("7548569c-9c19-42e8-b614-d33c43437388")).name("&eGold Rabbit").create();
			public static final ItemStack SALT_AND_PEPPER_RABBIT = ItemBuilder.ofSkull("9238519ff39815b16c4062823e43161ffaac96894fe088b018e6a24c26e181ec",UUID.fromString("a5eb4214-1e05-44f9-bd8b-b89aab3fcd4a")).name("&eSalt and Pepper Rabbit").create();
			public static final ItemStack THE_KILLER_BUNNY = ItemBuilder.ofSkull("71dd767929ef2fd2d43e86e8744c4b0d810853471201f2dfa18f96a67de56e2f",UUID.fromString("8935ea4b-0b33-468b-aaa6-78f19eb1dee1")).name("&eThe Killer Bunny").create();
		}

		public static final ItemStack RAVAGER = ItemBuilder.ofSkull("eb4db2986140e251e32e70ed08c8a081720313ce257632be1ef94a0737394db",UUID.fromString("4c2e79f1-5b07-4d10-81fe-7f35fe5b7086")).name("&eRavager").create();

		public static final ItemStack SALMON = ItemBuilder.ofSkull("791d9e69b795da4eaacfcf7350dfe8ae367fed833556706e040339dd7fe0240a",UUID.fromString("602918d8-3cd4-4e39-bcf5-430a8c2c5622")).name("&eSalmon").create();

		public static class Sheeps
		{
			public static final ItemStack BLACK_SHEEP = ItemBuilder.ofSkull("13335e8065c7b5dfea58d3df7474f396af4fa0a2ba52a3c9b7fba68319271c91",UUID.fromString("14577890-5c4b-47cb-b10d-7f6bd7b12d91")).name("&eBlack Sheep").create();
			public static final ItemStack BLUE_SHEEP = ItemBuilder.ofSkull("740e277da6c398b749a32f9d080f1cf4c4ef3f1f20dd9e5f422509e7ff593c0",UUID.fromString("fca18ae7-4750-4fac-be24-ad0a0f276b3b")).name("&eBlue Sheep").create();
			public static final ItemStack BROWN_SHEEP = ItemBuilder.ofSkull("3128d086bc81669fc2255bb22cadc66a0f5ed70885e84c32d37c1b484db35901",UUID.fromString("81356516-afb6-4474-8da5-553f06393f65")).name("&eBrown Sheep").create();
			public static final ItemStack CYAN_SHEEP = ItemBuilder.ofSkull("5d42fcbcaf9d48f73ffb0c3c36f34b4643295f6daa6cc74ab9d242ed5aa5636",UUID.fromString("c45b1b20-94ac-4cd1-869e-f6276d142e98")).name("&eCyan Sheep").create();
			public static final ItemStack GRAY_SHEEP = ItemBuilder.ofSkull("3fafecf0603b2dcd7984d252586069895db9aa78e1841bd554b19508dcf967a1",UUID.fromString("6a33acf0-f1ab-445f-944a-3007b4467a27")).name("&eGray Sheep").create();
			public static final ItemStack GREEN_SHEEP = ItemBuilder.ofSkull("9ea887eae4b07636e9e2f906609b00ab8d9b86b74728b819ff6f376583ea139",UUID.fromString("e9c78f34-e940-4479-ab2a-34b0cb1e4d4c")).name("&eGreen SHeep").create();
			public static final ItemStack JEB_SHEEP = ItemBuilder.ofSkull("233326765a190ebf90d5486d71f20e2597e4bee2a391fecbbd80debfe1f82d78",UUID.fromString("8576baf8-6974-400d-9eda-6c9407c11cef")).name("&eJeb Sheep").create();
			public static final ItemStack LIGHT_BLUE_SHEEP = ItemBuilder.ofSkull("ebf23af8719c437b3ee84019ba3c9e69ca854d3a8afd5cba6d9696c053b48614",UUID.fromString("a00b1ce9-1da9-45f8-b690-cf8b1d916f93")).name("&eLight Blue Sheep").create();
			public static final ItemStack LIGHT_GRAY_SHEEP = ItemBuilder.ofSkull("1d2e2e93a142bfd43f240d37de8f9b0976e76e65b22651908259e46db770e",UUID.fromString("1fa43907-66e7-41aa-a18a-a2d8d361658e")).name("&eLight Gray Sheep").create();
			public static final ItemStack LIME_SHEEP = ItemBuilder.ofSkull("6bead0342ae89b8dfd3d711a60add65e2c2bfea8d0bd274a7587deed7a31892e",UUID.fromString("7b69ea3f-cbee-41b7-bfc2-225f35a0a200")).name("&eLime Sheep").create();
			public static final ItemStack MAGENTA_SHEEP = ItemBuilder.ofSkull("a8e1f05f0dacca63a731874f90a693ffe21ff832e2b1e1d07b65c8764526f089",UUID.fromString("62e6e8d5-be4c-4caf-a39d-6516fa33c351")).name("&eMagenta Sheep").create();
			public static final ItemStack ORANGE_SHEEP = ItemBuilder.ofSkull("f684d04fa80aa59da14535dead3883d097fbba400625659f5259964806ba66f0",UUID.fromString("e4dbf656-00a5-455f-816a-e7f534df95b4")).name("&eOrange Sheep").create();
			public static final ItemStack PINK_SHEEP = ItemBuilder.ofSkull("6363e8a93d287a84e640309ae83ca1de0a0b257505a20ec55b3349d40a44854",UUID.fromString("b37d11eb-008c-4eed-b216-e5a2d237face")).name("&ePink Sheep").create();
			public static final ItemStack PURPLE_SHEEP = ItemBuilder.ofSkull("3449d08291dae45a24673619602f435b57f4cd4e9e98d2e0fbec4f18144781d3",UUID.fromString("4d6ba948-deb2-49c0-a6d6-6547cf48286f")).name("&ePurple Sheep").create();
			public static final ItemStack RED_SHEEP = ItemBuilder.ofSkull("5478e057158de6f45e2541cd17788e640ccb59723de59c254e82ab5711f3fc27",UUID.fromString("8d93fddb-20f1-40a8-95b8-e600db441b2a")).name("&eRed Sheep").create();
			public static final ItemStack WHITE_SHEEP = ItemBuilder.ofSkull("6dfe7cc46d749b153261c1dc11abbf2a3108ea1ba0b2650280eed1592dcfc75b",UUID.fromString("3955f04b-0958-431d-9222-2c5ab80b248c")).name("&eWhite Sheep").create();
			public static final ItemStack YELLOW_SHEEP = ItemBuilder.ofSkull("94b28f035735906f82ffc4dba99c9f0b55240e426cd1c525a9aa77180eec4934",UUID.fromString("fbf8e35c-9691-4f9d-a766-4cff585b139f")).name("&eYellow Sheep").create();
		}

		public static final ItemStack SHULKER = ItemBuilder.ofSkull("fb9e6af6b819f3d90e67ce2e7059fbef31da2aa953d35e3454f1021fa912efde",UUID.fromString("351e3e64-c736-4d8c-836d-259b7dc10b2c")).name("&eShulker").create();

		public static final ItemStack SILVERFISH = ItemBuilder.ofSkull("f25e9fae371664de1a800c84d025124abb8f15111807c8bc1ab9126aacbd4f95",UUID.fromString("aee58e8c-7f2b-44a1-bc8e-c58e7a167579")).name("&eSilverfish").create();
		
		public static final ItemStack SKELETON = ItemBuilder.ofSkull("d5a5839f179798cd3e7b09c371057665fa26f9369e267ffd471f0e78d4a65624",UUID.fromString("9b8eaa82-db29-4874-9b60-6e2a4be9f32b")).name("&eSkeleton").create();

		public static final ItemStack SKELETON_HORSE = ItemBuilder.ofSkull("6e226705bd2a9e7bb8d6b0f4daa969b9e12d4ae5c66da693bb5f4a4a1e6aa296",UUID.fromString("0a6275f9-fbd9-4c56-8cef-60f27eb157ae")).name("&eSkeleton Horse").create();

		public static final ItemStack SLIME = ItemBuilder.ofSkull("c06424ec7a196b15f9ad5733a36a6d1f2e6a0d42ffce1e1508f90f312ac4caed",UUID.fromString("6f528030-afc6-43ee-a4d6-1817667c8729")).name("&eSlime").create();

		public static final ItemStack SNIFFER = ItemBuilder.ofSkull("c84a7e7fe197b7e7419b51d46cc233551b9ec899de1afe7f653e4f8fb26a686e",UUID.fromString("26938565-4039-4801-816d-09ee113d2593")).name("&eSniffer").create();

		public static final ItemStack SNOWGOLEM = ItemBuilder.ofSkull("caa3e17ef1b29a4b87fa43dee1db12c41fd39aa387fa13af2a079b5b378fde8b",UUID.fromString("d452e937-fc8d-4c5a-9d6f-7da23db228a3")).name("&eSnow Golem").create();

		public static final ItemStack SPIDER = ItemBuilder.ofSkull("de28e6629b6ed1da94d4a818761612c36fb3a6813c4b63fb9fea5076415f3f0c",UUID.fromString("9c141461-5a94-4a48-8ca5-863313226904")).name("&eSpider").create();

		public static final ItemStack SQUID = ItemBuilder.ofSkull("8351b7d9a4f36cfe31fd59d8c900e419a135144105e7a981caa5a168dcff325b",UUID.fromString("a325cf1a-5bf3-468c-a688-4e488c4a2356")).name("&eSquid").create();

		public static final ItemStack STRAY = ItemBuilder.ofSkull("592b5597085e35db53d9bda008cae72b2f00cd7d4cd8dc69ff174a55b689e6e",UUID.fromString("0e647835-fb82-43d9-a343-012f53b63a5e")).name("&eStray").create();

		public static class Striders
		{
			public static final ItemStack STRIDER = ItemBuilder.ofSkull("9c40fad1c11de9e6422b405426e9b97907f35bce345e3758604d3e7be7df884",UUID.fromString("18ffddf9-d10b-45ba-b3e7-4981616ed289")).name("&eStrider").create();
			public static final ItemStack COLD_STRIDER = ItemBuilder.ofSkull("2713085a57527e45459c38faa7bb91cabb381df31cf2bf79d67a07156b6c2309",UUID.fromString("ca0170c3-b4a9-4392-a4de-a3f97e3b92d0")).name("&eCold Strider").create();
		}

		public static final ItemStack TADPOLE = ItemBuilder.ofSkull("3daf1653b5f59b5ec5a3f79609cb4233579fef07e693b61749e0900149edf563",UUID.fromString("c3bc0791-9900-4bdd-b5cd-1ae3cc44a04d")).name("&eTadpole").create();

		public static class TraderLlamas
		{
			public static final ItemStack CREAMY_TRADER_LLAMA = ItemBuilder.ofSkull("e89a2eb17705fe7154ab041e5c76a08d41546a31ba20ea3060e3ec8edc10412c",UUID.fromString("73a21d8e-fc9a-4b13-bdcc-05bbc9b790e5")).name("&eCreamy Trader Llama").create();
			public static final ItemStack WHITE_TRADER_LLAMA = ItemBuilder.ofSkull("7087a556d4ffa95ecd2844f350dc43e254e5d535fa596f540d7e77fa67df4696",UUID.fromString("90a94f70-a26f-4d13-8af7-210c9dd2edcf")).name("&eWhite Trader Llama").create();
			public static final ItemStack BROWN_TRADER_LLAMA = ItemBuilder.ofSkull("8424780b3c5c5351cf49fb5bf41fcb289491df6c430683c84d7846188db4f84d",UUID.fromString("b8b5696d-b180-40d6-83d2-27a589826c2e")).name("&eBrown Trader Llama").create();
			public static final ItemStack GRAY_TRADER_LLAMA = ItemBuilder.ofSkull("be4d8a0bc15f239921efd8be3480ba77a98ee7d9ce00728c0d733f0a2d614d16",UUID.fromString("9553491c-8383-461b-9d8e-057032ee4b61")).name("&eGray Trader Llama").create();
		}

		public static final ItemStack TROPICAL_FISH = ItemBuilder.ofSkull("34a0c84dc3c090df7bafc4367a9fc6c8520da2f73efffb80e934d1189eadac41",UUID.fromString("98d7c345-5dcf-4c7c-b0d7-d7817d613ec8")).name("&eTropical Fish").create();

		public static final ItemStack TURTLE = ItemBuilder.ofSkull("304931200ad460b650a190e8d41227c3999fbeb933b51ca49fd9e5920d1f8e7d",UUID.fromString("e18625fa-9a82-40cd-a043-7bbc5d2f6370")).name("&eTurtle").create();

		public static class Vexs
		{
			public static final ItemStack VEX = ItemBuilder.ofSkull("b9538f2830c4dea6996ed744785504e32e0e20d8663edab6b0222f2c022077bd",UUID.fromString("0e095edd-fed7-4e92-bd58-f00765bf0b08")).name("&eVex").create();
			public static final ItemStack CHARGING_VEX = ItemBuilder.ofSkull("4a4e518e16e4b5c114acbd9c61cd18292da9ef60550a4fcae27d39ae293e477a",UUID.fromString("e13024bf-4207-49cb-82b3-f82201fcba25")).name("&eCharging Vex").create();
		}

		public static class Villagers
		{
			public static final ItemStack ARMORER_VILLAGER = ItemBuilder.ofSkull("1ef627f566ac0a7828bad93e9e4b9643d99a928a13d5f977bf441e40db1336bf",UUID.fromString("41d6cc23-b54d-4964-997d-6fcd3492b165")).name("&eArmorer Villager").create();
			public static final ItemStack BUTCHER_VILLAGER = ItemBuilder.ofSkull("a1bad64185e04bf1dafe3da84933d02545ea4a63221a10d0f07759179112bdc2",UUID.fromString("41b63db1-d9d3-4883-bfa7-0cc657b96fcd")).name("&eButcher Villager").create();
			public static final ItemStack CARTOGRAPHER_VILLAGER = ItemBuilder.ofSkull("e3aecfbe801cf32b5d1b0b1f6680049666158678c53f4a651fc83e0df9d3738b",UUID.fromString("152e7a3a-b70e-4477-8760-7b1a673b3157")).name("&eCartographer Villager").create();
			public static final ItemStack CLERIC_VILLAGER = ItemBuilder.ofSkull("5b9e582e2f9b89d556e79c4697f706b1dd4929ecae3c07ee90bf1d5be319bf6f",UUID.fromString("cba624ab-b9a4-4cf2-b407-88a42162d4ce")).name("&eCleric Villager").create();
			public static final ItemStack FARMER_VILLAGER = ItemBuilder.ofSkull("d9272d03cda6290e4d925a7e850a745e711fe5760f6f06f93d92b8f8c739db07",UUID.fromString("8bcdb394-a442-47ca-b6a0-967d0d2543e1")).name("&eFarmer Villager").create();
			public static final ItemStack FISHERMAN_VILLAGER = ItemBuilder.ofSkull("d189fb4acd15d73ff2a58a88df0466ad9f4c154a2008e5c6265d5c2f07d39376",UUID.fromString("be3ee97f-dc7a-4773-b4c7-076af3f10fdb")).name("&eFisherman Villager").create();
			public static final ItemStack FLETCHER_VILLAGER = ItemBuilder.ofSkull("bf611f12e18ce44a57238eef1cae03cd9f730a7a45e0ec248f14ce84e9c48056",UUID.fromString("dd6014eb-068a-4808-b4fa-867f87a9a11d")).name("&eFletcher Villager").create();
			public static final ItemStack LEATHERWORKER_VILLAGER = ItemBuilder.ofSkull("ae0e9591e11aaef4c2c51d9ac69514e340485defcc2c12c38cd12386c2ec6b78",UUID.fromString("3143a8e3-afa2-4b3c-9d38-5e190b886ae0")).name("&eLeatherworker Villager").create();
			public static final ItemStack LIBRARIAN_VILLAGER = ItemBuilder.ofSkull("cdcaa574babb40ee0fa83f2fd5ea20cff31ffa272fe113588ceee469682128e7",UUID.fromString("2164b6f3-46a7-4213-bcbe-4cac658c2636")).name("&eLibrarian Villager").create();
			public static final ItemStack MASON_VILLAGER = ItemBuilder.ofSkull("ae0e9591e11aaef4c2c51d9ac69514e340485defcc2c12c38cd12386c2ec6b78",UUID.fromString("67b2134e-07e7-4637-870a-be171a68a5b7")).name("&eMason Villager").create();
			public static final ItemStack NITWIT_VILLAGER = ItemBuilder.ofSkull("ae0e9591e11aaef4c2c51d9ac69514e340485defcc2c12c38cd12386c2ec6b78",UUID.fromString("67a87372-aa68-40c6-b8ae-f8581ee6cad7")).name("&eNitwit Villager").create();
			public static final ItemStack VILLAGER = ItemBuilder.ofSkull("ae0e9591e11aaef4c2c51d9ac69514e340485defcc2c12c38cd12386c2ec6b78",UUID.fromString("23de197d-6e99-484f-9131-9c5dd7d974b5")).name("&eVillager").create();
			public static final ItemStack SHEPHERD_VILLAGER = ItemBuilder.ofSkull("2abf4e9154ac9271941c733eacc62dc9fc0a6dc1b5d67c78ca98afb5cb1be9b2",UUID.fromString("deb97032-ea8a-4935-95f3-34315168c4e1")).name("&eShepherd Villager").create();
			public static final ItemStack TOOLSMITH_VILLAGER = ItemBuilder.ofSkull("ae0e9591e11aaef4c2c51d9ac69514e340485defcc2c12c38cd12386c2ec6b78",UUID.fromString("fe006929-3eee-45a9-bad4-04f6fc893d70")).name("&eToolsmith Villager").create();
			public static final ItemStack WEAPONSMITH_VILLAGER = ItemBuilder.ofSkull("8476ffa410bbe7fa70909965a125f4a4e9a4fb1ce1b8b3c34bfb73aaffd4ce43",UUID.fromString("c4277918-4f73-4de3-8af8-f95b53298ba2")).name("&eWeaponsmith Villager").create();
		}

		public static final ItemStack VINDICATOR = ItemBuilder.ofSkull("2dabafde27ee12b09865047aff6f183fdb64e04dae1c00ccbde04ad93dcc6c95",UUID.fromString("bd49f975-18cd-4174-8a1f-664554da3640")).name("&eVindicator").create();

		public static final ItemStack WANDERING_TRADER = ItemBuilder.ofSkull("5f1379a82290d7abe1efaabbc70710ff2ec02dd34ade386bc00c930c461cf932",UUID.fromString("d0bf9b83-dff8-4d85-88b6-b129332fee40")).name("&eWandering Trader").create();

		public static final ItemStack WARDEN = ItemBuilder.ofSkull("b2f3879b737127485eb35ddee748d06cf914b193d97753ae34e92230842831fb",UUID.fromString("8c9b775b-3060-4793-ad0e-7c500a1516cc")).name("&eWarden").create();

		public static final ItemStack WITCH = ItemBuilder.ofSkull("a520f12c63c7912186c4be4e30c33c5acaec0db0b6abd836d517d74a62275d4b",UUID.fromString("c61af030-bbaf-4942-8b5d-db00ffe35345")).name("&eWitch").create();

		public static class WitherHeads
		{
			public static final ItemStack WITHER = ItemBuilder.ofSkull("eda10828f63b7ecdefd767b3245fbdaa13c3ec0c6b13774f1ee8d307c034c383",UUID.fromString("5dc14150-8d93-4cde-be52-ec744588fbc5")).name("&eWither").create();
			public static final ItemStack WITHER_PROJECTILE = ItemBuilder.ofSkull("b37c5814a92f8ec0f6ae9933abe9542e165190768e760478543aebeed4027c27",UUID.fromString("a9daa368-aee6-4673-881e-643fade0ebd6")).name("&eWither Projectile").create();
			public static final ItemStack BLUE_WITHER_PROJECTILE = ItemBuilder.ofSkull("d3682b06203b9de4c28541071a26cdc340dd25d4c372b7023ec2f412021d62f7",UUID.fromString("fbef139b-c146-469a-91ca-45af0d46ad50")).name("&eBlue Wither Projectile").create();
		}
		
		public static final ItemStack WITHER_SKELETON = ItemBuilder.ofSkull("3a5775eabbbf81f8a9e3fb140fb7dcb0e68c5b2d02e10b1043564125494f1a2f",UUID.fromString("4aa6fd68-7902-46d2-9941-58e597b2bfc2")).name("&eWither Skeleton").create();

		public static class Wolfs
		{
			public static final ItemStack WOLF = ItemBuilder.ofSkull("b6439a43e5687008815a2dd1ff4a134c12221b782336678b979ad13dce39665e",UUID.fromString("ff1e72e5-d413-4a6a-8981-14fa31cd0e86")).name("&eWolf").create();
			public static final ItemStack ANGRY_WOLF = ItemBuilder.ofSkull("8d1aa7e3b9564b3846f1dea14f1b1ccbf399bbb23b952dbd7eec41802a289c96",UUID.fromString("5fa9fa50-2a12-40a5-a118-f31a797fb1e0")).name("&eAngry Wolf").create();
		}

		public static final ItemStack ZOGLIN = ItemBuilder.ofSkull("2e3493a956bfd7588ed1a8ea858759667659d58100cbecd6d96ccc0ca9b36923",UUID.fromString("abb27a90-a71c-4288-a726-75ada852afb2")).name("&eZoglin").create();
		
		public static final ItemStack ZOMBIE = ItemBuilder.ofSkull("eda8302d830ff9a3148ce6e7b52d5c45a33c892e2e4951ce1d90ae49bd8c57c8",UUID.fromString("5f49c440-fe2e-4729-9d38-870026792afd")).name("&eZombie").create();

		public static final ItemStack ZOMBIE_HORSE = ItemBuilder.ofSkull("6618ffbe1cfa2058fe80a065f70c128c225a1e0bc9deaf8b38b0395443f40909",UUID.fromString("f2b3519a-5f51-417e-9a5b-f913e8e944ee")).name("&eZombie Horse").create();

		public static class ZombieVillagers
		{
			public static final ItemStack ZOMBIE_ARMORER = ItemBuilder.ofSkull("c8679e034767d518660d9416dc5eaf319d697682ac40c886e3c2bc8dfa1de1d",UUID.fromString("3ee5cfea-1fcc-4f42-a90d-035a505c6fb6")).name("&eZombie Armorer").create();
			public static final ItemStack ZOMBIE_BUTCHER = ItemBuilder.ofSkull("9cce8d6ce4124cec3e84a852e70f50293f244ddc9ee8578f7d6d8929e16bad69",UUID.fromString("983b396c-beb7-40b6-9412-b7ec9b3f22d5")).name("&eZombie Butcher").create();
			public static final ItemStack ZOMBIE_CARTOGRAPHER = ItemBuilder.ofSkull("e60800b01012e963e7c20c8ba14b70a0264d146a850deffbca7bfe512f4cb23d",UUID.fromString("946559cd-3550-4b9b-9bd5-f104a86fbcb8")).name("&eZombie Cartographer").create();
			public static final ItemStack ZOMBIE_CLERIC = ItemBuilder.ofSkull("2958578be0e12172734a78242dab14964abc85ab9b596361f7c5daf8f14a0feb",UUID.fromString("3100a2c2-f9d2-4b18-b91d-3892f6839e17")).name("&eZombie Cleric").create();
			public static final ItemStack ZOMBIE_FARMER = ItemBuilder.ofSkull("f77d415f9baa4fa4b5e058f5b81bf7f003b0a2c90a4831e53a7dbc09841c5511",UUID.fromString("3986eadd-fb78-4ba2-b48c-fb1838f837f6")).name("&eZombie Farmer").create();
			public static final ItemStack ZOMBIE_FISHERMAN = ItemBuilder.ofSkull("6905d53fe4faeb0b315a6878c9ab81b4be52c31cd478c027f0d7ece9f6da8914",UUID.fromString("ef250301-d298-45cc-86b4-24394979e5ea")).name("&eZombie Fisherman").create();
			public static final ItemStack ZOMBIE_FLETCHER = ItemBuilder.ofSkull("2ea26ac0e25498adada4ecea58bb4e76da32d5ca2de307efe5e4218fb7c5ef89",UUID.fromString("72d3780b-97de-4d65-905a-63dac1482935")).name("&eZombie Fletcher").create();
			public static final ItemStack ZOMBIE_LEATHERWORKER = ItemBuilder.ofSkull("fb552c90f212e855d12255d5cd62ed38b9cd7e30e73f0ea779d1764330e69264",UUID.fromString("1f2147c3-5e3e-4310-ab84-b7a067db13d9")).name("&eZombie Leatherworker").create();
			public static final ItemStack ZOMBIE_LIBRARIAN = ItemBuilder.ofSkull("62211a1f409cca4249c70d20ca80399fa4844ea417458be988cc21eb4797375e",UUID.fromString("b5d0d032-e5b0-49f9-a0ce-335ea562cd01")).name("&eZombie Librarian").create();
			public static final ItemStack ZOMBIE_MASON = ItemBuilder.ofSkull("fb552c90f212e855d12255d5cd62ed38b9cd7e30e73f0ea779d1764330e69264",UUID.fromString("da39c3c8-922a-4f95-a64a-19bd47aa497b")).name("&eZombie Mason").create();
			public static final ItemStack ZOMBIE_NITWIT = ItemBuilder.ofSkull("fb552c90f212e855d12255d5cd62ed38b9cd7e30e73f0ea779d1764330e69264",UUID.fromString("d2440816-30d9-42e9-ab34-555d81ce0c16")).name("&eZombie Nitwit").create();
			public static final ItemStack ZOMBIE_VILLAGER = ItemBuilder.ofSkull("fb552c90f212e855d12255d5cd62ed38b9cd7e30e73f0ea779d1764330e69264",UUID.fromString("802af25f-b543-46e5-a7a2-7d73483c098f")).name("&eZombie Villager").create();
			public static final ItemStack ZOMBIE_SHEPHERD = ItemBuilder.ofSkull("691391bef3a46ef267d3b7171086ba4c8d17f2a6b0f83fa2ac30efe914b7c249",UUID.fromString("41184709-fd08-434c-879f-bc3d513c1e88")).name("&eZombie Shepherd").create();
			public static final ItemStack ZOMBIE_TOOLSMITH = ItemBuilder.ofSkull("fb552c90f212e855d12255d5cd62ed38b9cd7e30e73f0ea779d1764330e69264",UUID.fromString("6e3c3474-dde9-4632-8e15-56ec1b833633")).name("&eZombie Toolsmith").create();
			public static final ItemStack ZOMBIE_WEAPONSMITH = ItemBuilder.ofSkull("4370894b5cc305d87aa08c3b4b08587db68ff29e7a3ef354cad6abca50e5528b",UUID.fromString("fdc72639-2c71-49f8-912d-5ac8918e8d61")).name("&eZombie Weaponsmith").create();
		}

		public static final ItemStack ZOMBIFIED_PIGLIN = ItemBuilder.ofSkull("2df03128b002a70708d6825ed6cf54ddf694b3766d78d5649030b1cb8b34c6fa",UUID.fromString("c16faa19-7048-4954-b05a-b4d4128c340c")).name("&eZombified Piglin").create();
	}
}