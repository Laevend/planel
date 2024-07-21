package coffee.dape.utils;

import java.net.HttpURLConnection;
import java.net.URI;

public class SecurityUtils
{
	public static boolean urlExists(String url)
	{
		try
		{
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection) URI.create(url).toURL().openConnection();
			con.setRequestMethod("HEAD");
			
			if(con.getResponseCode() == HttpURLConnection.HTTP_OK) { return true; }
		}
		catch(Exception e)
		{
			Logg.error("Error checking URL " + url,e);
			return false;
		}
		
		return false;
	}
}
