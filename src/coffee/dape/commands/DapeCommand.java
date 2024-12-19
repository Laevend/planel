package coffee.dape.commands;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import coffee.dape.Dape;
import coffee.dape.cmdparsers.astral.annos.CommandEx;
import coffee.dape.cmdparsers.astral.annos.Path;
import coffee.dape.cmdparsers.astral.parser.AstralExecutor;
import coffee.dape.cmdparsers.astral.parser.CommandParser.CmdSender;
import coffee.dape.exception.MissingAnnotationException;
import coffee.dape.utils.ColourUtils;
import coffee.dape.utils.GradientUtils;
import coffee.dape.utils.ItemBuilder;
import coffee.dape.utils.PrintUtils;
import coffee.dape.utils.StringUtils;
import net.minecraft.core.component.DataComponentMap;


/**
 * @author Laeven
 */
@CommandEx(name = "dape",description = "A command for viewing dape version")
public final class DapeCommand extends AstralExecutor
{
	public DapeCommand() throws MissingAnnotationException
	{
		super(DapeCommand.class);
		
		addPath("version",CmdSender.ANY);
	}
	
	@Path(name = "version",description = "Displays the version of Dape",syntax = "/dape",usage = "/dape")
	public final void version(CommandSender sender)
	{
		PrintUtils.info(sender,ColourUtils.applyColour(Dape.instance().getDescription().getName(),ColourUtils.RUSTY_RED));
		PrintUtils.info(sender,"Version: " + ColourUtils.applyColour(Dape.instance().getDescription().getVersion(),ColourUtils.DARK_WHITE));
		PrintUtils.info(sender,"Developed by " + ColourUtils.applyColour(StringUtils.arrayToCSV(Dape.instance().getDescription().getAuthors()),ColourUtils.RUSTY_RED));
		
		ItemStack stack = ItemBuilder.of(Material.MACE,1)
			.lore("You what up")
			//.wrap(GradientUtils.applyGradient("This is very long",GradientUtils.ENSOULED))
			.wrap(GradientUtils.applyGradient("This is a very long string to show how text can be written in one line and then be wrapped around to another line as if by magic!",GradientUtils.ENSOULED))
			.append(GradientUtils.applyGradient("How many & symbols can I throw in here? &v &n \\&aOooh how is this not working? it has \\&a??",GradientUtils.ENSOULED))
			.commit()
			.name("This is uBER mAcE windooo",GradientUtils.SAND_TO_BLUE)
			.enchant(Enchantment.DENSITY,10)
			.enchant(Enchantment.BREACH,10)
			.enchant(Enchantment.WIND_BURST,10)
			.flag(ItemFlag.HIDE_DESTROYS)
			.unbreakable(true)
			.attmod(Attribute.GENERIC_GRAVITY,EquipmentSlotGroup.OFFHAND,Operation.ADD_NUMBER,100)
			.create();
		
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(stack);
		DataComponentMap nbtItem = nmsItem.a();
		//PatchedDataComponentMap nbtItem = nmsItem.
		String nbt = nbtItem.toString();
		
		System.out.println(nbt);
		
		Player p = (Player) sender;
		p.getWorld().dropItem(p.getLocation(),stack);
	}
}