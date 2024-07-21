package coffee.dape.cmdparsers.astral.elevatedaccount.authmethod;

import java.security.SecureRandom;

import org.bukkit.entity.Player;

import coffee.dape.cmdparsers.astral.elevatedaccount.ElevatedAccountCtrl.AuthMethod;
import coffee.dape.utils.PrintUtils;
import coffee.dape.utils.security.EncryptUtils;
import coffee.dape.utils.security.HashingUtils;
import coffee.dape.utils.security.SecureByteArray;
import coffee.dape.utils.security.SecureString;

public final class StaticPinAuthMethod extends AuthenticationMethod
{
	private SecureString hashedPin = null;
	private SecureByteArray salt = null;
	
	public StaticPinAuthMethod(final String hashedPin,final byte[] salt)
	{
		super(AuthMethod.STATIC_PIN);
		this.hashedPin = new SecureString(hashedPin);
		this.salt = new SecureByteArray(salt);
	}
	
	public StaticPinAuthMethod(final SecureString pin)
	{
		super(AuthMethod.STATIC_PIN);
		this.salt = new SecureByteArray(HashingUtils.generateSalt());
		this.hashedPin = new SecureString(EncryptUtils.toBase64(HashingUtils.hash(pin.asString(),this.salt.asByteArray())));
	}

	@Override
	public boolean verifyMethod(final String value,final Player player)
	{
		if(value.length() > 32)
		{
			incrementAttempt();
			PrintUtils.error(player,"Authentication Failed, invalid pin!");
			return false;
		}
		
		String hashedPin = EncryptUtils.toBase64(HashingUtils.hash(value,this.salt.asByteArray()));		
		
		if(!hashedPin.equals(hashedPin))
		{
			incrementAttempt();
			PrintUtils.error(player,"Authentication Failed, invalid pin!");
			return false;
		}
		
		return true;
	}
	
	@Override
	public String getAuthMessage()
	{
		return "Enter your pin using '/auth <value>'";
	}
	
	@Override
	public int maxAttempts()
	{
		return 5;
	}

	public SecureString getHashedPin()
	{
		return hashedPin;
	}
	
	/**
	 * Requests a change of the existing pin by comparing old pin to the pin currently held.
	 * <p>Pin is only changed if old pin matches what is currently held.
	 * @param oldPin The existing pin held in this account
	 * @param newPin The new pin the player who owns this account wishes to change their pin to
	 * @return
	 */
	public boolean changePin(SecureString oldPin,SecureString newPin)
	{
		String hashedPin = EncryptUtils.toBase64(HashingUtils.hash(oldPin.toString(),this.salt.asByteArray()));
		
		if(!hashedPin.equals(this.hashedPin.toString()))
		{
			incrementAttempt();
			return false;
		}
		
		setPin(newPin);
		return true;
	}
	
	public void setPin(SecureString pin)
	{
		this.salt = new SecureByteArray(HashingUtils.generateSalt());
		this.hashedPin = new SecureString(EncryptUtils.toBase64(HashingUtils.hash(pin.asString(),this.salt.asByteArray())));
	}

	public SecureByteArray getSalt()
	{
		return salt;
	}
	
	@Override
	public void clear()
	{
		SecureRandom sr = new SecureRandom();
		
		byte[] saltScrambled = new byte[sr.nextInt(16,32)];
		
		sr.nextBytes(saltScrambled);
		
		this.hashedPin = new SecureString(String.valueOf(sr.nextLong()));
		this.salt = new SecureByteArray(saltScrambled);
	}
}
