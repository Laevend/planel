package coffee.dape.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

/**
 * 
 * @author Laeven
 *
 */
public class WebUtils
{
	public static String getWebpage(String url)
	{
		try
		{
			URL webpage = URI.create(url).toURL();
			BufferedReader in = new BufferedReader(new InputStreamReader(webpage.openStream()));

			StringBuilder sb = new StringBuilder();
			String inputLine;
			
			while((inputLine = in.readLine()) != null)
			{
				sb.append(inputLine);
			}
			            
			in.close();
			return sb.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
