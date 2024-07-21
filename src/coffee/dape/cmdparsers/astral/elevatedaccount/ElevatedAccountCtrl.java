package coffee.dape.cmdparsers.astral.elevatedaccount;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import coffee.dape.Dape;
import coffee.dape.cmdparsers.astral.elevatedaccount.authmethod.AuthenticationMethod;
import coffee.dape.cmdparsers.astral.elevatedaccount.authmethod.StaticPinAuthMethod;
import coffee.dape.cmdparsers.astral.elevatedaccount.authmethod.TimedOTPAuthMethod;
import coffee.dape.config.Configurable;
import coffee.dape.event.ChatInputEvent;
import coffee.dape.exception.IllegalMethodCallException;
import coffee.dape.utils.ChatBuilder;
import coffee.dape.utils.ChatUtils;
import coffee.dape.utils.ColourUtils;
import coffee.dape.utils.FileOpUtils;
import coffee.dape.utils.Logg;
import coffee.dape.utils.MapUtils;
import coffee.dape.utils.MathUtils;
import coffee.dape.utils.PlayerUtils;
import coffee.dape.utils.PrintUtils;
import coffee.dape.utils.security.HashingUtils;
import coffee.dape.utils.security.SecureString;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * 
 * @author Laeven
 * Controller for ElevatedAccounts
 */
public class ElevatedAccountCtrl implements Configurable, Listener
{
	private static final Map<UUID,ElevatedAccount> accounts;
	private static ConsoleAccount conAcc;
	private static final Path ELEVATED_ACCOUNTS_DIR = Dape.internalFilePath("elevated");
	protected static final int TRUE;
	protected static final int FALSE;
	private static ConsoleSetupPhase consoleSetupPhase = ConsoleSetupPhase.NONE;
	private static SecureString[] newPinTest = new SecureString[2];
	private static UUID inputSessionId = null;
	
	static
	{
		SecureRandom sr = new SecureRandom();
		TRUE = sr.nextInt(1,Integer.MAX_VALUE);
		FALSE = sr.nextInt(1,Integer.MAX_VALUE);
		
		accounts = new HashMap<>();
		
		if(TRUE == FALSE)
		{
			Logg.fatal("Elevated Account TRUE & FALSE definitions are the same!");
			Dape.forceShutdown();
		}
	}
	
	// Initialise
	public final static void init()
	{
		if(!ConsoleAccount.isSetup())
		{
			conAcc = new ConsoleAccount();
			return;
		}
		
		try
		{
			String data = new String(Files.readAllBytes(Dape.internalFilePath("elevated" + File.separator + "console")));
			String[] dataParts = data.split(",");
			conAcc = new ConsoleAccount(new SecureString(dataParts[0]),Base64.getDecoder().decode(dataParts[1]));
		}
		catch (IOException e)
		{
			Logg.error("Could not load console account pin/password!",e);
			conAcc = new ConsoleAccount();
			return;
		}
		
		loadAll();
	}
	
	@EventHandler
	public final static void onChatInput(ChatInputEvent e)
	{
		// Check input session id is matching that given by the request
		if(!e.getInputSessionId().equals(inputSessionId)) { return; }
		
		if(consoleSetupPhase == null || consoleSetupPhase == ConsoleSetupPhase.NONE)
		{
			PrintUtils.error(e.getPlayer(),"Error! Incorrect console setup phase! Restart server and try again!");
			return;
		}
		
		if(consoleSetupPhase == ConsoleSetupPhase.ASKING_FOR_PIN_OR_PASSWORD_1)
		{
			newPinTest[0] = new SecureString(e.getInput());
			setupConsoleAccount(e.getPlayer());
			return;
		}
		
		if(consoleSetupPhase == ConsoleSetupPhase.ASKING_FOR_PIN_OR_PASSWORD_2)
		{
			newPinTest[1] = new SecureString(e.getInput());
			setupConsoleAccount(e.getPlayer());
			return;
		}
	}
	
	/**
	 * Method for first time setup of console auth
	 * @param p Player setting up console account
	 */
	public final static void setupConsoleAccount(Player p)
	{
		Objects.requireNonNull(p,"Player cannot be null!");
		
		// Prevents re-setting up
		if(ConsoleAccount.isSetup()) { return; }
		
		if(consoleSetupPhase == null || consoleSetupPhase == ConsoleSetupPhase.NONE)
		{
			consoleSetupPhase = ConsoleSetupPhase.ASKING_FOR_PIN_OR_PASSWORD_1;
			inputSessionId = ChatUtils.requestInput(p,"Enter a pin/password that will be used to authorise any elevated commands via the console");
			return;
		}
		
		if(consoleSetupPhase == ConsoleSetupPhase.ASKING_FOR_PIN_OR_PASSWORD_1)
		{
			consoleSetupPhase = ConsoleSetupPhase.ASKING_FOR_PIN_OR_PASSWORD_2;
			inputSessionId = ChatUtils.requestInput(p,"To confirm, enter the same pin/password again");
			return;
		}
		
		if(consoleSetupPhase != ConsoleSetupPhase.ASKING_FOR_PIN_OR_PASSWORD_2) { return; }
		consoleSetupPhase = ConsoleSetupPhase.NONE;
		
		String pin = newPinTest[0].asString();
		
		PrintUtils.warn(p,"Pin '" + pin + "' ");
		
		if(pin.isEmpty()) { PrintUtils.error(p,"Pin/Password cannot be empty!"); return; }
		if(pin.isBlank()) { PrintUtils.error(p,"Pin/Password cannot be blank!"); return; }
		if(pin.length() < 9) { PrintUtils.error(p,"Pin/Password cannot be less than 9 characters!"); return; }
		if(pin.matches("\\s+")) { PrintUtils.error(p,"Pin/Password cannot contain space characters!"); return; }
		
		byte[] tempSalt = HashingUtils.generateSalt();
		
		PrintUtils.warn(p,"Pin '" + pin + "' ");
		
		if(!HashingUtils.hashToString(newPinTest[0].asString(),tempSalt).equals(HashingUtils.hashToString(newPinTest[1].asString(),tempSalt)))
		{
			PrintUtils.error(p,"Pins/Passwords entered do not match!");
			return;
		}
		
		try
		{
			conAcc.setPin(new SecureString(pin));
		}
		catch (IllegalMethodCallException e)
		{
			e.printStackTrace();
		}
		
		// overwrite memory
		newPinTest[0] = new SecureString("null");
		newPinTest[1] = new SecureString("null");
		newPinTest = null;
		
		try
		{
			Files.write(Dape.internalFilePath("elevated" + File.separator + "console"),new String(conAcc.getHashedPin().asString() + "," + Base64.getEncoder().encodeToString(conAcc.getSalt().asByteArray())).getBytes());
		}
		catch (IOException e)
		{
			Logg.error("Could not save console account pin/password!",e);
			return;
		}
		
		PrintUtils.success(p,"Console setup complete!");
	}
	
	public static void createElevatedAccount(Player p,SecureString pin)
	{
		if(accounts.containsKey(p.getUniqueId())) { return; }
		if(Files.exists(Paths.get(ELEVATED_ACCOUNTS_DIR + File.separator + p.getUniqueId().toString() + ".json"))) { return; }
		
		ElevatedAccount newAcc = new ElevatedAccount(p.getUniqueId(),getMethods());
		new ElevatedAccountWriter().toJson(newAcc,Paths.get(ELEVATED_ACCOUNTS_DIR + File.separator + p.getUniqueId().toString() + ".json"));
	}
	
	public static void createElevatedAccount(UUID uuid)
	{
		if(accounts.containsKey(uuid)) { return; }
		if(Files.exists(Paths.get(ELEVATED_ACCOUNTS_DIR + File.separator + uuid.toString() + ".json"))) { return; }
		
		ElevatedAccount newAcc = new ElevatedAccount(uuid,getMethods());
		new ElevatedAccountWriter().toJson(newAcc,Paths.get(ELEVATED_ACCOUNTS_DIR + File.separator + uuid.toString() + ".json"));
	}
	
	public static boolean hasElevatedAccount(Player p)
	{
		return accounts.containsKey(p.getUniqueId()) || Files.exists(Paths.get(ELEVATED_ACCOUNTS_DIR + File.separator + p.getUniqueId().toString() + ".json"));
	}
	
	public static boolean hasElevatedAccount(UUID uuid)
	{
		return accounts.containsKey(uuid) || Files.exists(Paths.get(ELEVATED_ACCOUNTS_DIR + File.separator + uuid.toString() + ".json"));
	}
	
	public static boolean isAccountLoaded(Player p)
	{
		return accounts.containsKey(p.getUniqueId());
	}
	
	public static boolean isAccountLoaded(UUID uuid)
	{
		return accounts.containsKey(uuid);
	}
	
	public static boolean hasElevatedAccountLoaded(Player p)
	{
		return accounts.containsKey(p.getUniqueId());
	}
	
	public static boolean hasElevatedAccountLoaded(UUID uuid)
	{
		return accounts.containsKey(uuid);
	}
	
	public static void removeElevatedAccount(UUID uuid)
	{
		if(accounts.containsKey(uuid))
		{
			// Clear sooner rather than waiting for GC
			accounts.get(uuid).clear();
			accounts.get(uuid).markAsDeleted(true);
		}

		FileOpUtils.delete(Paths.get(ELEVATED_ACCOUNTS_DIR + File.separator + uuid.toString() + ".json"));
	}
	
	public static ElevatedAccount getAccount(Player p)
	{
		if(!accounts.containsKey(p.getUniqueId())) { return null; }
		return accounts.get(p.getUniqueId());
	}
	
	public static Map<UUID,ElevatedAccount> getAccounts()
	{
		return accounts;
	}
	
	public static ElevatedAccount getAccount(UUID uuid)
	{
		if(!accounts.containsKey(uuid)) { return null; }
		return accounts.get(uuid);
	}

	public static ConsoleAccount getConsoleAccount()
	{
		return conAcc;
	}

	public static void save(UUID uuid)
	{
		System.out.println(Paths.get(ELEVATED_ACCOUNTS_DIR + File.separator + uuid.toString() + ".json"));
		
		if(!accounts.containsKey(uuid)) { return; }
		
		new ElevatedAccountWriter().toJson(getAccount(uuid),Paths.get(ELEVATED_ACCOUNTS_DIR + File.separator + uuid.toString() + ".json"));
	}
	
	public static void saveAll()
	{
		for(UUID uuid : accounts.keySet())
		{
			new ElevatedAccountWriter().toJson(getAccount(uuid),Paths.get(ELEVATED_ACCOUNTS_DIR + File.separator + uuid.toString() + ".json"));
		}
	}
	
	/**
	 * Loads all accounts upon server start.
	 * New accounts cannot be added until server restart
	 * @return
	 */
	private static final void loadAll()
	{
		FileOpUtils.createDirectories(ELEVATED_ACCOUNTS_DIR);
		
		if(ELEVATED_ACCOUNTS_DIR.toFile().listFiles() == null) { Collections.unmodifiableMap(accounts); return; }
		if(ELEVATED_ACCOUNTS_DIR.toFile().listFiles().length == 0) { Collections.unmodifiableMap(accounts); return; }
		
		for(File file : ELEVATED_ACCOUNTS_DIR.toFile().listFiles())
		{
			if(!file.getName().endsWith(".json")) { continue; }
			ElevatedAccount elevatedAccount = new ElevatedAccountReader().fromJson(file);
			
			if(elevatedAccount == null) { continue; }
			
			Logg.info("Loaded elevated account for " + PlayerUtils.getName(elevatedAccount.getOwner()));
			accounts.put(elevatedAccount.getOwner(),elevatedAccount);
		}
		
		Collections.unmodifiableMap(accounts);
	}
	
	private static List<AuthenticationMethod> getMethods()
	{
		List<AuthenticationMethod> methods = new ArrayList<>();
		
		if(Dape.getConfigFile().getBoolean(ConfigKey.STATIC_PIN))
		{
			methods.add(new StaticPinAuthMethod(new SecureString(new StringBuilder(MathUtils.getSecureRandomIntString(6)))));
		}
		
		if(Dape.getConfigFile().getBoolean(ConfigKey.TOTP))
		{
			methods.add(new TimedOTPAuthMethod());
		}
		
		if(Dape.getConfigFile().getBoolean(ConfigKey.EMAIL_OTP))
		{
			// Not implemented yet
		}
		
		if(Dape.getConfigFile().getBoolean(ConfigKey.YUBI_KEY))
		{
			// Not implemented yet
		}
		
		return methods;
	}
	
	public enum AuthMethod
	{
		STATIC_PIN,
		TIMED_OTP,
		EMAIL_OTP,
		YUBI_KEY
	}
	
	private enum ConsoleSetupPhase
	{
		NONE,
		ASKING_FOR_PIN_OR_PASSWORD_1,
		ASKING_FOR_PIN_OR_PASSWORD_2
	}

	@Override
	public Map<String, Object> getDefaults()
	{
		return Map.of(
				ConfigKey.STATIC_PIN,true,
				ConfigKey.TOTP,true,
				ConfigKey.EMAIL_OTP,true,	// Not implemented yet
				ConfigKey.YUBI_KEY,false,	// Not implemented yet (requires local server)
				ConfigKey.AUTH_TIME,300_000L); // 5 minutes
	}
	
	public static class ConfigKey
	{
		public static final String STATIC_PIN = "elevated_accounts.auth_method.static_pin";
		public static final String TOTP = "elevated_accounts.auth_method.totp";
		public static final String EMAIL_OTP = "elevated_accounts.auth_method.email_otp";
		public static final String YUBI_KEY = "elevated_accounts.auth_method.yubi_key";
		
		public static final String AUTH_TIME = "elevated_accounts.auth_time";
	}
	
	public static class SecretViewWarning implements Listener
	{
		@EventHandler
		public final static void onChatInput(ChatInputEvent e)
		{
			// Check input session id is matching that given by the request
			if(!e.getPlayer().getUniqueId().equals(e.getSessionOwner())) { return; }
			if(!e.getInput().equalsIgnoreCase("y") && !e.getInput().equalsIgnoreCase("yes"))
			{
				Logg.info("Cancelled totp view.");
				return;
			}
			
			ElevatedAccount acc = ElevatedAccountCtrl.getAccount(e.getPlayer());
			
			for(AuthenticationMethod meth : acc.getAuthMethods())
			{
				if(meth.getAuthMethod() != AuthMethod.TIMED_OTP) { continue; }
				
				TimedOTPAuthMethod totpAuthMethod = (TimedOTPAuthMethod) meth;
				BufferedImage qrCode = totpAuthMethod.getQrCode(e.getPlayer().getUniqueId());
				ItemStack stack = MapUtils.personalImageToMap(qrCode,e.getPlayer().getUniqueId());
				
				e.getPlayer().getInventory().addItem(stack);
				
				PrintUtils.error(e.getPlayer(),"Only you can see the QR code on this map.");
				PrintUtils.error(e.getPlayer(),"DO NOT SHARE THIS QRCODE OR YOUR TOTP SECRET WITH ANYONE!");
				BaseComponent[] totpSecret = new ChatBuilder()
					.setMessage("&5Totp secret&8:&d" + totpAuthMethod.getSecret().asString().substring(0,7) + "...")
					.setHoverShowTextEvent(ColourUtils.transCol("&eClick me to copy your secret to your clipboard"))
					.setClickCopyToClipboardEvent(totpAuthMethod.getSecret().asString()).getResult();
				PrintUtils.sendComp(e.getPlayer(),totpSecret);
				return;
			}
		}
	}
}
