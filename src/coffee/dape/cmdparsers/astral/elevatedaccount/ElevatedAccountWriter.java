package coffee.dape.cmdparsers.astral.elevatedaccount;

import com.google.gson.JsonObject;

import coffee.dape.cmdparsers.astral.elevatedaccount.authmethod.AuthenticationMethod;
import coffee.dape.cmdparsers.astral.elevatedaccount.authmethod.EmailOTPAuthMethod;
import coffee.dape.cmdparsers.astral.elevatedaccount.authmethod.StaticPinAuthMethod;
import coffee.dape.cmdparsers.astral.elevatedaccount.authmethod.TimedOTPAuthMethod;
import coffee.dape.cmdparsers.astral.elevatedaccount.authmethod.YubiKeyAuthMethod;
import coffee.dape.utils.Logg;
import coffee.dape.utils.json.JWriter;
import coffee.dape.utils.security.EncryptUtils;
import coffee.dape.utils.security.ObfuscatedRandBaseEncoder;

/**
 * @author Laeven
 */
public class ElevatedAccountWriter extends JWriter<ElevatedAccount>
{	
	@Override
	public void writeJson() throws Exception
	{
		writeOwner();
		writeCreationDate();
		writeLocked();
		
		for(AuthenticationMethod authMeth : this.object.getAuthMethods())
		{
			switch(authMeth.getAuthMethod())
			{
				case STATIC_PIN -> writeStaticPinAuthMethod((StaticPinAuthMethod) authMeth);
				case TIMED_OTP -> writeTimedOTPAuthMethod((TimedOTPAuthMethod) authMeth);
				case EMAIL_OTP -> writeEmailOTPAuthMethod((EmailOTPAuthMethod) authMeth);
				case YUBI_KEY -> writeYubiKeyAuthMethod((YubiKeyAuthMethod) authMeth);
				default -> Logg.warn("Unknown unsupported auth method: " + authMeth.getAuthMethod().toString());
			}
		}
	}
	
	private void writeOwner()
	{
		addProperty(ElevatedAccountReader.OWNER,this.object.getOwner().toString());
	}
	
	private void writeCreationDate()
	{
		addProperty(ElevatedAccountReader.CREATION_DATE,ObfuscatedRandBaseEncoder.decode(this.object.getCreationDate()));
	}
	
	private void writeLocked()
	{
		addProperty(ElevatedAccountReader.LOCKED,this.object.isLocked());
	}
	
	private void writeStaticPinAuthMethod(StaticPinAuthMethod auth)
	{
		JsonObject obj = new JsonObject();
		obj.addProperty(ElevatedAccountReader.PIN,auth.getHashedPin().asString());
		obj.addProperty(ElevatedAccountReader.SALT,EncryptUtils.toBase64(auth.getSalt().asByteArray()));
		add(ElevatedAccountReader.STATIC_PIN_AUTH,obj);
	}
	
	private void writeTimedOTPAuthMethod(TimedOTPAuthMethod auth)
	{
		JsonObject obj = new JsonObject();
		obj.addProperty(ElevatedAccountReader.SECRET,auth.getSecret().asString());
		add(ElevatedAccountReader.TOTP_AUTH,obj);
	}
	
	private void writeEmailOTPAuthMethod(EmailOTPAuthMethod auth)
	{
		// TODO Write email data
	}
	
	private void writeYubiKeyAuthMethod(YubiKeyAuthMethod auth)
	{
		// TODO Write yubikey data
	}
}