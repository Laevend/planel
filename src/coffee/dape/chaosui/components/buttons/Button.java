package coffee.dape.chaosui.components.buttons;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import coffee.dape.chaosui.components.ChaosComponent;
import coffee.dape.chaosui.listeners.ChaosActionListener;

/**
 * 
 * @author Laeven
 *
 */
public class Button extends ChaosComponent
{
	private static final Sound defaultButtonSound = Sound.BLOCK_LODESTONE_BREAK;
	private static final float defaultButtonPitch = 2.0f;
	
	public Button(int slot,ItemStack stack)
	{
		super(slot,stack,Type.BUTTON);
		this.setSound(defaultButtonSound,defaultButtonPitch);
	}
	
	public Button(int slot,ItemStack stack,ChaosActionListener listener)
	{
		super(slot,stack,Type.BUTTON);
		this.setSound(defaultButtonSound,defaultButtonPitch);
		this.addActionListener(listener);
	}
	
	public Button(ItemStack stack)
	{
		super(stack,Type.BUTTON);
		this.setSound(defaultButtonSound,defaultButtonPitch);
	}
	
	public Button(ItemStack stack,ChaosActionListener listener)
	{
		super(stack,Type.BUTTON);
		this.setSound(defaultButtonSound,defaultButtonPitch);
		this.addActionListener(listener);
	}

	@Override
	public ItemStack getStack()
	{
		return getAppearance();
	}
	
	@Override
	public boolean isItemComponentType()
	{
		return true;
	}

	@Override
	public ChaosComponent getComponent()
	{
		return this;
	}
}