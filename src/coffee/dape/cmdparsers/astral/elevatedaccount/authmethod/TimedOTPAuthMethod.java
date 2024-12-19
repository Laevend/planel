package coffee.dape.cmdparsers.astral.elevatedaccount.authmethod;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.amdelamar.jotp.OTP;
import com.amdelamar.jotp.type.Type;

import coffee.dape.Dape;
import coffee.dape.cmdparsers.astral.elevatedaccount.ElevatedAccountCtrl.AuthMethod;
import coffee.dape.utils.ImageUtils;
import coffee.dape.utils.Logg;
import coffee.dape.utils.PlayerUtils;
import coffee.dape.utils.PrintUtils;
import coffee.dape.utils.StringUtils;
import coffee.dape.utils.security.SecureString;
import io.nayuki.fastqrcodegen.QrCode;
import io.nayuki.fastqrcodegen.QrCodeUtils;

public final class TimedOTPAuthMethod extends AuthenticationMethod
{
	private SecureString secret;
	
	public TimedOTPAuthMethod()
	{
		super(AuthMethod.TIMED_OTP);
		
		// Random secret Base32 with 20 bytes (160 bits) length
		// (Use this to setup 2FA for new accounts).
		this.secret = new SecureString(new StringBuilder(OTP.randomBase32(30)));
	}
	
	public TimedOTPAuthMethod(String secret)
	{
		super(AuthMethod.TIMED_OTP);
		
		this.secret = new SecureString(secret);
	}
	
	@Override
	public boolean setup(Player player)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getURL(UUID player)
	{
		return OTP.getURL(secret.asString(),6,Type.TOTP,StringUtils.capitaliseFirstLetter(Dape.getNamespaceName().toLowerCase()),PlayerUtils.getName(player));
	}
	
	public BufferedImage getQrCode(UUID player)
	{
		QrCode totpQr = QrCode.encodeText(getURL(player),QrCode.Ecc.LOW);
		BufferedImage qrcode = QrCodeUtils.toImage(totpQr,2,1);
		
		// Map is 128 x 128 pixels
		BufferedImage mapImage = ImageUtils.getBlank(Color.WHITE,128,128);
		
		// Calculate offset to draw qrcode in middle of map
		int xOffset = 64 - (qrcode.getWidth() / 2);
		int yOffset = 64 - (qrcode.getHeight() / 2);
		return ImageUtils.drawImageOntop(mapImage,qrcode,xOffset,yOffset);
	}

	@Override
	public boolean verifyMethod(final String value,final Player player)
	{
		if(value.length() > 6)
		{
			incrementAttempt();
			PrintUtils.error(player,"Authentication Failed, invalid passcode!");
			Logg.warn(player.getName() + " failed TOTP auth, value too long!");
			return false;
		}
		
		if(!value.matches("^\\d{6}$"))
		{
			incrementAttempt();
			PrintUtils.error(player,"Authentication Failed, invalid passcode!");
			Logg.warn(player.getName() + " failed TOTP auth, value is not exclusively digits!");
			return false;
		}
		
		try
		{
			// Generate a Time-based OTP from the secret, using Unix-time
			// rounded down to the nearest 30 seconds.
			String hexTime = OTP.timeInHex(System.currentTimeMillis(),30);
			String code = OTP.create(secret.asString(),hexTime,6,Type.TOTP);
			
			Logg.info("\nHexTime: " + hexTime
					+ "\nCode: " + code
					+ "\nSecret: " + secret.asString()
					+ "\nValue: " + value);
			
			if(!OTP.verify(secret.asString(),hexTime,value,6,Type.TOTP))
			{
				incrementAttempt();
				PrintUtils.error(player,"Authentication Failed, invalid passcode!");
				return false;
			}
			
			return true;
		}
		catch(Exception e)
		{
			Logg.error("Error occured verifying TOTP!",e);
			PrintUtils.error(player,"Authentication Failed, unable to verify!");
			return false;
		}
	}
	
	public SecureString getSecret()
	{
		return secret;
	}

	@Override
	public String getAuthMessage()
	{
		return "Enter your 2fa one time passcode from your authenticator app using '/auth <value>'";
	}
	
	@Override
	public int maxAttempts()
	{
		return 5;
	}

	@Override
	public void clear()
	{
		SecureRandom sr = new SecureRandom();
		this.secret = new SecureString(String.valueOf(sr.nextLong()));
	}
}
