package coffee.dape.exception;

import coffee.dape.utils.json.JsonDataType;

/**
 * @author Laeven
 * @since 1.0.0
 */
public class InvalidJsonAttributeDataTypeException extends Exception
{
	private static final long serialVersionUID = 2473482744940397751L;
	
	public InvalidJsonAttributeDataTypeException(String reader,String jsonName,String attribute,JsonDataType type)
	{
        super("Json Reader '" + reader + "' attempting to parse '" + jsonName + "' contains attribute '" + attribute + "' with wrong data type! Expected " + type.toString().toLowerCase());
    }
	
	public InvalidJsonAttributeDataTypeException(String reader,String jsonName,String attribute,Class<?> type)
	{
        super("Json Reader '" + reader + "' attempting to parse '" + jsonName + "' contains attribute '" + attribute + "' with wrong enum data type! Expected " + type.getSimpleName());
    }
}
