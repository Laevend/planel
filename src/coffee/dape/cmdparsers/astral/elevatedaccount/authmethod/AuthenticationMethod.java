package coffee.dape.cmdparsers.astral.elevatedaccount.authmethod;

import org.bukkit.entity.Player;

import coffee.dape.cmdparsers.astral.elevatedaccount.ElevatedAccountCtrl.AuthMethod;
import coffee.dape.utils.security.ObfuscatedRandBaseEncoder;
import coffee.dape.utils.security.SecureByteArray;

public abstract class AuthenticationMethod
{
	private final AuthMethod authMethod;
	private SecureByteArray attempt = ObfuscatedRandBaseEncoder.encode(1);
	
	public AuthenticationMethod(final AuthMethod method)
	{
		this.authMethod = method;
	}
	
	public abstract boolean setup(final Player player);
	
	public abstract boolean verifyMethod(final String value,final Player player);
	
	public abstract String getAuthMessage();
	
	public abstract int maxAttempts();

	public final AuthMethod getAuthMethod()
	{
		return authMethod;
	}

	public final SecureByteArray getAttempt()
	{
		return attempt;
	}
	
	public final void incrementAttempt()
	{
		attempt = ObfuscatedRandBaseEncoder.encode(ObfuscatedRandBaseEncoder.decode(attempt) + 1);
	}
	
	public final void resetAttempt()
	{
		attempt = ObfuscatedRandBaseEncoder.encode(1);
	}
	
	public abstract void clear();
}
