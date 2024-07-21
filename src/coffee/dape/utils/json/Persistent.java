package coffee.dape.utils.json;

/**
 * 
 * @author Laeven
 *
 */
public interface Persistent<T>
{
	JReader<T> getReader();
	
	JWriter<T> getWriter();
}
