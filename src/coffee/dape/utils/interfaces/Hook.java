package coffee.dape.utils.interfaces;

/**
 * Interface for classes that hook into other classes.
 *
 * @author Khyonie
 */
public interface Hook<T>
{
	/**
	 * Hooks the target class.
	 *
	 * @param type Target class
	 *
	 * @return Whether or not the hooking was successful
	 */
	public boolean hookClass(Class<? extends T> type);
}
