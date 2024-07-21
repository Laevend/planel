package coffee.dape.utils.json;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import coffee.dape.utils.Logg;

/**
 * 
 * @author Laeven
 *
 */
public abstract class EmbeddedRootJsonObject extends EmbeddedJsonObject
{
	private static GsonBuilder builder;
	private static Gson gson;
	protected File file;
	protected String fileName;
    
    static
	{
		builder = new GsonBuilder();
		builder.setPrettyPrinting();
		gson = builder.create();
	}
	
	public EmbeddedRootJsonObject(File file)
	{
		this.file = file;
		this.fileName = file.getName();
	}
	
	/**
	 * Saves this embedded object to disk
	 * 
	 * @return true if data was successfully saved
	 */
	public boolean saveObject()
	{
		if(!JsonUtils.toJsonFile(file.toPath(),jo))
		{
			Logg.fatal("EbeddedJsonObject failed to save JSON data! Filename: " + this.fileName);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Discards data currently held in memory for this embedded object
	 * and loads from file on disk.
	 * 
	 * <p>In the event this embedded objects file on disk doesn't exist,
	 * a blank object will be used instead.
	 */
	public boolean loadObject()
	{
		this.jo = new JsonObject();
		
		if(!this.file.exists()) { return true; }
		
		jo = gson.toJsonTree(JsonUtils.fromJsonFile(this.file.getAbsolutePath())).getAsJsonObject();
		
		if(jo == null)
		{
			Logg.fatal("EbeddedJsonObject failed to load JSON data! Filename: " + this.fileName);
			return false;
		}
		
		return true;
	}
}