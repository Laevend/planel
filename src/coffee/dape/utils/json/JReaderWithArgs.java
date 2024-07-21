package coffee.dape.utils.json;

/**
 * 
 * @author Laeven
 *
 */
public interface JReaderWithArgs<T>
{
	/**
	 * readJson with arguments. You are to override this method
	 * to provide implementation.
	 * @param args Object arguments
	 * @return T
	 * @throws Exception
	 */
	public T readJson(Object... args) throws Exception;
}
