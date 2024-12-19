package coffee.dape.cmdparsers.astral.elevatedaccount.authmethod;

import org.bukkit.entity.Player;

import coffee.dape.cmdparsers.astral.elevatedaccount.ElevatedAccountCtrl.AuthMethod;

/**
 * 
 * @author Laeven
 * TODO Placeholder for later
 */
public final class YubiKeyAuthMethod extends AuthenticationMethod
{
	public YubiKeyAuthMethod(final String secret)
	{
		super(AuthMethod.YUBI_KEY);
	}
	
	@Override
	public boolean setup(Player player)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyMethod(final String value,final Player player)
	{
		// TODO YubiKey verify logic
		
		return true;
	}
	
	@Override
	public String getAuthMessage()
	{
		return "You've been sent a one time passcode to your email. Enter it here.";
	}
	
	@Override
	public int maxAttempts()
	{
		return 5;
	}
	
	@Override
	public void clear()
	{
		
	}
}
