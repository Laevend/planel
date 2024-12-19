package coffee.dape.chaosui.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import coffee.dape.chaosui.ChaosFactory.InvTemplate;

/**
 * 
 * @author Laeven
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ChaosGUI
{
	/**
	 * Name of the GUI
	 * @return GUI name
	 */
	public String name();
	
	/**
	 * Holds the class of the handler of this GUI
	 * @return GuiHandler or Object.class aka This GUI has no handler!
	 */
	public Class<?> handler() default Object.class;
	
	/**
	 * Holds the inventory template used to create the this GUI
	 * @return Inventory template
	 */
	public InvTemplate template();
}