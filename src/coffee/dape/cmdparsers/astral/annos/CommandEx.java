package coffee.dape.cmdparsers.astral.annos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Laeven
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandEx
{
	/**
	 * Name of the command and what you type to execute it
	 * @return Command name
	 */
	public String name();
	
	/**
	 * Alias of the command
	 * @return Command alias
	 */
	public String[] alias() default "";
	
	/**
	 * Description of the command
	 * @return Command description
	 */
	public String description();
	
	/**
	 * Parent permission of the command required to use any of the sub commands
	 * 
	 * <p>If base permission is blank ("") then command permissions becomes
	 * <\plugin / module name\>.command.<\command name\>
	 * 
	 * <p>Example dape.command.help
	 * @return Main permission of this command
	 */
	public String permission() default "";
	
	/**
	 * The group that this commands belongs to.
	 * 
	 * <p>If left blank, the group is 'default'
	 * <p>Commands in the group 'default' do not require any permissions to execute!
	 * @return Group this command belongs to
	 */
	public String group() default "default";
}