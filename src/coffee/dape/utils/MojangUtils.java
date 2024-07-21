package coffee.dape.utils;

import java.util.UUID;

import com.google.gson.JsonObject;

import coffee.dape.utils.json.JsonUtils;

/**
 * 
 * @author Laeven
 * Utility methods for calling the Mojang Public API
 */
public class MojangUtils
{
	/**
	 * Checks if Mojang Public API is 'OK'
	 * @return True if API is ok and working, false otherwise
	 */
	public static boolean isApiOK()
	{
		return getApiStatus().equals("OK");
	}
	
	/**
	 * Gets the status of the Mojang Public API
	 * @return Mojang Public API status
	 */
	public static String getApiStatus()
	{
		JsonObject obj = getApiDetails();
		if(!obj.has("Status")) { Logg.error("No status found!"); return "null"; }
		return obj.get("Status").getAsString();
	}
	
	/**
	 * Gets the Mojang Public API details
	 * @return
	 */
	public static JsonObject getApiDetails()
	{
		String json = WebUtils.getWebpage("https://api.mojang.com/");
		
		if(json == null) { Logg.error("Too many API requests!"); return null; }
	
		return JsonUtils.fromJsonString(json);
	}
	
	/**
	 * Requests a new profile from the Mojang API
	 * @param uuid UUID of player
	 * @param withSignature If also requested the signature of the player to be returned in the API call
	 * @return JsonObject of a players profile, null otherwise
	 */
	public static JsonObject requestProfileFromAPI(UUID uuid,boolean withSignature)
	{
		String json = withSignature ?
				WebUtils.getWebpage("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString() + "?unsigned=false") :
					WebUtils.getWebpage("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString());
		
		if(json == null) { Logg.error("UUID not found or too many API requests!"); return null; }
		
		return JsonUtils.fromJsonString(json);
	}
	
	/**
	 * Requests a new UUID lookup from the Mojang API
	 * @param playerName Exact name of the player
	 * @return JsonObject of UUID lookup, null otherwise
	 */
	public static JsonObject requestUUIDFromAPI(String playerName)
	{
		String json = WebUtils.getWebpage("https://api.mojang.com/users/profiles/minecraft/" + playerName);
		
		if(json == null) { Logg.error("Too many API requests!"); return null; }
		
		return JsonUtils.fromJsonString(json);
	}
}