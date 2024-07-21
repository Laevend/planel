package coffee.dape.cmdparsers.astral.elevatedaccount;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.gson.JsonObject;

import coffee.dape.cmdparsers.astral.elevatedaccount.authmethod.AuthenticationMethod;
import coffee.dape.cmdparsers.astral.elevatedaccount.authmethod.EmailOTPAuthMethod;
import coffee.dape.cmdparsers.astral.elevatedaccount.authmethod.StaticPinAuthMethod;
import coffee.dape.cmdparsers.astral.elevatedaccount.authmethod.TimedOTPAuthMethod;
import coffee.dape.cmdparsers.astral.elevatedaccount.authmethod.YubiKeyAuthMethod;
import coffee.dape.utils.json.JReader;
import coffee.dape.utils.json.JsonDataType;
import coffee.dape.utils.security.EncryptUtils;

/**
 * @author Laeven
 */
public class ElevatedAccountReader extends JReader<ElevatedAccount>
{
	private ElevatedAccount elevatedAccount;
	
	public static final String OWNER = "owner";
	public static final String CREATION_DATE = "creation_date";
	public static final String LOCKED = "locked";
	
	public static final String STATIC_PIN_AUTH = "static_pin_auth";
	public static final String PIN = "pin";
	public static final String SALT = "salt";
	
	public static final String TOTP_AUTH = "totp_auth";
	public static final String SECRET = "secret";
	
	public static final String EMAIL_AUTH = "email_auth";
	
	public static final String YUBIKEY_AUTH = "yubikey_auth";
	
	@Override
	public ElevatedAccount readJson() throws Exception
	{
		UUID owner = readOwner();
		long creationDate = readCreationDate();
		boolean locked = readLocked();
		
		List<AuthenticationMethod> methods = new ArrayList<>();
		
		if(has(STATIC_PIN_AUTH))
		{
			methods.add(new StaticPinAuthMethod(readPin(),readSalt()));
		}
		
		if(has(TOTP_AUTH))
		{
			methods.add(new TimedOTPAuthMethod(readSecret()));
		}
		
		// TODO Read email auth data
		if(has(EMAIL_AUTH))
		{
			methods.add(new EmailOTPAuthMethod(""));
		}
		
		// TODO Read yubikey auth data
		if(has(YUBIKEY_AUTH))
		{
			methods.add(new YubiKeyAuthMethod(""));
		}
		
		elevatedAccount = new ElevatedAccount(owner,creationDate,locked,methods);
		return elevatedAccount;
	}
	
	private UUID readOwner() throws Exception
	{
		assertHas(OWNER).assertType(OWNER,JsonDataType.STRING);
		return UUID.fromString(get(OWNER).getAsString());
	}
	
	private long readCreationDate() throws Exception
	{
		assertHas(CREATION_DATE).assertType(CREATION_DATE,JsonDataType.LONG);
		return get(CREATION_DATE).getAsLong();
	}
	
	private boolean readLocked() throws Exception
	{
		assertHas(LOCKED).assertType(LOCKED,JsonDataType.BOOLEAN);
		return get(LOCKED).getAsBoolean();
	}
	
	private String readPin() throws Exception
	{
		JsonObject obj = get(STATIC_PIN_AUTH).getAsJsonObject();
		assertHas(PIN,obj).assertType(PIN,obj,JsonDataType.STRING);
		return obj.get(PIN).getAsString();
	}
	
	private byte[] readSalt() throws Exception
	{
		JsonObject obj = get(STATIC_PIN_AUTH).getAsJsonObject();
		assertHas(SALT,obj).assertType(SALT,obj,JsonDataType.STRING);
		return EncryptUtils.fromBase64(obj.get(SALT).getAsString());
	}
	
	private String readSecret() throws Exception
	{
		JsonObject obj = get(TOTP_AUTH).getAsJsonObject();
		assertHas(SECRET,obj).assertType(SECRET,obj,JsonDataType.STRING);
		return new String(obj.get(SECRET).getAsString());
	}
}
