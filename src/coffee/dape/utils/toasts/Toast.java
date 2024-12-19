package coffee.dape.utils.toasts;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import coffee.dape.Dape;
import coffee.dape.utils.DelayUtils;
import coffee.dape.utils.Logg;
import coffee.dape.utils.MaterialUtils;

public class Toast
{
	private NamespacedKey id;
    private Material icon;
    private String text;
    private Frame frame;
    private Collection<? extends Player> players;
    
	public Toast(String text,Material icon,Frame frame,Player player)
	{
		this(text,icon,frame,Arrays.asList(player));
	}
	
	public Toast(String text,Material icon,Frame frame,Collection<? extends Player> players)
	{
		this.id = new NamespacedKey(Dape.instance(),UUID.randomUUID().toString());
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
		this.players = players;
	}
	
	@SuppressWarnings("deprecation")
	public void send()
	{
		try
		{
			Advancement advancement = Bukkit.getAdvancement(id);
			
			// Advancements loaded to server are persistent with restarts.
			// Check this advancement was not left behind in a previous crash/restart without removal
			if(advancement == null)
			{
				Bukkit.getUnsafe().loadAdvancement(id,getToastJson());
			}
			
			Advancement finalAdvancement = Bukkit.getAdvancement(id);
			players.forEach(player -> player.getAdvancementProgress(finalAdvancement).getRemainingCriteria().forEach(criteria -> player.getAdvancementProgress(finalAdvancement).awardCriteria(criteria)));
		}
		catch (Exception e)
		{
			Logg.error("Error while loading advancement!",e);
		}
		
		// Needs a 1 tick delay otherwise the toast does not send
		DelayUtils.executeDelayedTask(() ->
		{
			Advancement advancement = Bukkit.getAdvancement(id);
			players.forEach(player -> player.getAdvancementProgress(advancement).getAwardedCriteria().forEach(criteria -> player.getAdvancementProgress(advancement).revokeCriteria(criteria)));
			Bukkit.getUnsafe().removeAdvancement(id);
		},1);
	}
	
	// Make sure JSON structure reflections how the wiki says it should be:
	// https://minecraft.wiki/w/Advancement_definition
	public String getToastJson()
	{
		JsonObject json = new JsonObject();

        JsonObject icon = new JsonObject();
        icon.addProperty("id",MaterialUtils.getMinecraftMaterial(this.icon));

        JsonObject display = new JsonObject();
        display.add("icon",icon);
        display.addProperty("title",this.text);
        display.addProperty("description","I am desc boi");
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
