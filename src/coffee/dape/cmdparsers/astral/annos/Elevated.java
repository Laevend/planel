package coffee.dape.cmdparsers.astral.annos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Elevated
{
	/**
	 * If authorisation should be required regardless of cool down since last
	 * authentication
	 * 
	 * @return
	 */
	public boolean force() default false;
}
