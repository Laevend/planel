package coffee.dape.utils.security;

import java.util.UUID;

import com.amdelamar.jotp.OTP;
import com.amdelamar.jotp.type.Type;

import coffee.dape.Dape;
import coffee.dape.utils.PlayerUtils;
import coffee.dape.utils.StringUtils;

public class TotpUtils
{
	public static final String getTotpURL(SecureString secret,UUID player)
	{
		return OTP.getURL(
				secret.asString(),
				6,
				Type.TOTP,
				StringUtils.capitaliseFirstLetter(Dape.getNamespaceName().toLowerCase()),PlayerUtils.getName(player));
	}
	
	public static final String getTotpURL(SecureString secret,String name)
	{
		return OTP.getURL(
				secret.asString(),
				6,
				Type.TOTP,
				StringUtils.capitaliseFirstLetter(Dape.getNamespaceName().toLowerCase()),name);
	}
}
