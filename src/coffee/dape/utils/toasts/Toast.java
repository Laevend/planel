package coffee.dape.utils.toasts;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import coffee.dape.Dape;
import coffee.dape.utils.Logg;
import coffee.dape.utils.MaterialUtils;

public class Toast
{
	private NamespacedKey id;
    private Material icon;
    private String text;
    private Frame frame;
    
	public Toast(String toastName,String text,Material icon,Frame frame)
	{
		this.id = new NamespacedKey(Dape.instance(),toastName);
		this.text = text;
		
		if(!MaterialUtils.canRenderAsItemStack(icon))
		{
			this.icon = Material.GRASS_BLOCK;
		}
		else
		{
			this.icon = icon;
		}
		
		this.frame = frame;
	}
	
	@SuppressWarnings("deprecation")
	public void add()
	{
		try
		{
			Bukkit.getUnsafe().loadAdvancement(id,getJson());
		}
		catch (Exception e)
		{
			Logg.error("Error while saving, Advancement " + id + " seems to already exist");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void remove()
	{
		Bukkit.getUnsafe().removeAdvancement(id);
	}
	
	public void grant(Collection<? extends Player> players)
	{
		Advancement advancement = Bukkit.getAdvancement(id);
		AdvancementProgress progress;
		
		for(Player player : players)
		{
			progress = player.getAdvancementProgress(advancement);
			
			if(!progress.isDone())
			{
				for(String criteria : progress.getRemainingCriteria())
				{
					progress.awardCriteria(criteria);
				}
			}
		}
    }
	
	public void revoke(Collection<? extends Player> players)
	{
		Advancement advancement = Bukkit.getAdvancement(id);
		AdvancementProgress progress;
		
		for(Player player : players)
		{
			progress = player.getAdvancementProgress(advancement);
			
			if(progress.isDone())
			{
				for(String criteria : progress.getAwardedCriteria())
				{
					progress.revokeCriteria(criteria);
				}
			}
		}
	}
	
	public String getJson()
	{
		JsonObject json = new JsonObject();

        JsonObject icon = new JsonObject();
        icon.addProperty("item",MaterialUtils.getMinecraftMaterial(this.icon));

        JsonObject display = new JsonObject();
        display.add("icon",icon);
        display.addProperty("title",this.text);
        display.addProperty("description","N/A");
        display.addProperty("background","minecraft:textures/gui/advancements/backgrounds/adventure.png");
        display.addProperty("frame",this.frame.toString().toLowerCase());
        display.addProperty("announce_to_chat",false);
        display.addProperty("show_toast",true);
        display.addProperty("hidden",true);

        JsonObject criteria = new JsonObject();
        JsonObject trigger = new JsonObject();

        trigger.addProperty("trigger","minecraft:impossible");
        criteria.add("impossible",trigger);

        json.add("criteria", criteria);
        json.add("display", display);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(json);
	}

	public NamespacedKey getId()
	{
		return id;
	}

	public Material getIcon()
	{
		return icon;
	}

	public String getText()
	{
		return text;
	}

	public Frame getFrame()
	{
		return frame;
	}
	
	public enum Frame
	{
		TASK,
		CHALLENGE,
		GOAL;
	}
}
