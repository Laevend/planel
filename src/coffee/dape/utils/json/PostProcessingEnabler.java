package coffee.dape.utils.json;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * 
 * @author Laeven
 *
 */
public class PostProcessingEnabler implements TypeAdapterFactory
{
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type)
    {
        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

        return new TypeAdapter<T>()
        {
            public void write(JsonWriter out, T value) throws IOException
            {
            	T obj = value;
            	
            	if(obj instanceof PreSerialise)
                {
                    ((PreSerialise)obj).preSerialise();
                }
            	
            	delegate.write(out,obj);
            }

            public T read(JsonReader in) throws IOException
            {
                T obj = delegate.read(in);
                
                if (obj instanceof PostDeserialise)
                {
                    ((PostDeserialise)obj).postDeserialise();
                }
                
                return obj;
            }
        };
    }
}
