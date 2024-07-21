package coffee.dape.utils.security;

import java.lang.reflect.Method;

import coffee.dape.exception.IllegalMethodCallException;
import coffee.dape.utils.Logg;

/**
 * @author Laeven
 * Acts as a way to block method calls from classes/objects that shouldn't be calling a method
 */
public final class Bouncer
{
	/**
	 * Halts a method call unless the class & method calling this method is of the arguments passed
	 * @param clazz Class to allow a method call from
	 * @param meth Method to allow a method call from
	 * @throws IllegalMethodCallException Thrown when The class and method calling a method does not match arguments provided
	 */
	public static final void haltAllBut(final Class<?> clazz,final Method meth) throws IllegalMethodCallException
	{
		StackTraceElement ele = Thread.currentThread().getStackTrace()[3];
		if(ele.getClassName().equals(clazz.getName()) && ele.getMethodName().equals(meth.getName())) { return; }
		
		throw new IllegalMethodCallException(ele.getClassName(),ele.getMethodName(),ele.getLineNumber());
	}
	
	/**
	 * Halts a method call unless the class calling this method is one of the arguments passed
	 * @param clazzes Class to allow a method call from
	 * @throws IllegalMethodCallException Thrown when The class calling a method does not match any class arguments provided
	 */
	public static final void haltAllBut(final Class<?>... clazzes) throws IllegalMethodCallException
	{
		StackTraceElement ele = Thread.currentThread().getStackTrace()[3];
		
		for(Class<?> clazz : clazzes)
		{
			if(ele.getClassName().equals(clazz.getName())) { return; }
		}
		
		throw new IllegalMethodCallException(ele.getClassName(),ele.getMethodName(),ele.getLineNumber());
	}
	
	/**
	 * Probes the stack trace where {@link Bouncer#probe()} is called showing what where the last call was from
	 */
	public static final void probe()
	{
		StackTraceElement ele = Thread.currentThread().getStackTrace()[3];
		Logg.info("Bouncer Prob -> \n" + 
				  "    Class: " + ele.getClassName() + "\n" +
				  "    Method: " + ele.getMethodName() + "\n" +
				  "    Line: " + ele.getLineNumber());
	}
}
