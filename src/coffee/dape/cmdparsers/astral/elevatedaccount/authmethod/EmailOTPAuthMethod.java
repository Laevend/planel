package coffee.dape.cmdparsers.astral.elevatedaccount.authmethod;

import org.bukkit.entity.Player;

import coffee.dape.cmdparsers.astral.elevatedaccount.ElevatedAccountCtrl.AuthMethod;
import coffee.dape.utils.PrintUtils;

/**
 * 
 * @author Laeven
 * TODO Placeholder for later
 */
public final class EmailOTPAuthMethod extends AuthenticationMethod
{
	public EmailOTPAuthMethod(final String email)
	{
		super(AuthMethod.EMAIL_OTP);
	}

	@Override
	public boolean verifyMethod(final String value,final Player player)
	{
		if(value.length() > 6)
		{
			incrementAttempt();
			PrintUtils.error(player,"Authentication Failed, invalid passcode!");
			return false;
		}
		
		if(!value.matches("^\\d{6}$"))
		{
			incrementAttempt();
			PrintUtils.error(player,"Authentication Failed, invalid passcode!");
			return false;
		}
		
		// TODO Email OTP verify logic
		
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
