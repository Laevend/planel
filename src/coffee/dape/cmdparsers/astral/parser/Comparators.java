package coffee.dape.cmdparsers.astral.parser;

import java.util.Comparator;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import coffee.dape.utils.MaterialUtils;

public class Comparators
{
	public static Comparator<String> ALPHABETICALLY = (s1,s2) -> s1.compareTo(s2);
	public static Comparator<Integer> NUMERICALLY = (i1,i2) -> i1.compareTo(i2);
	public static Comparator<String> NUMERICALLY_BY_STRING = new Comparator<String>()
	{
		@Override
		public int compare(String s1, String s2)
		{
			Integer num1 = Integer.valueOf(s1);
			Integer num2 = Integer.valueOf(s2);
			
			return num1.compareTo(num2);
		}
	};
	public static Comparator<ItemStack> ITEMSTACK_BY_NAME = new Comparator<ItemStack>()
	{
		@Override
		public int compare(ItemStack o1, ItemStack o2)
		{
			String stackName1 = o1.getItemMeta().hasDisplayName() ? ChatColor.stripColor(o1.getItemMeta().getDisplayName()) : MaterialUtils.getNameFromMaterial(o1.getType());
			String stackName2 = o2.getItemMeta().hasDisplayName() ? ChatColor.stripColor(o2.getItemMeta().getDisplayName()) : MaterialUtils.getNameFromMaterial(o2.getType());
			
			return stackName1.compareTo(stackName2);
		}
	};
}
