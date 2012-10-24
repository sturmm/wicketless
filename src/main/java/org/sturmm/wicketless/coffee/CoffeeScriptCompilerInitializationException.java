package org.sturmm.wicketless.coffee;

/**
 * 
 * Exception that will be throw if there occurs any problem during coffee
 * compiler initialization.
 * 
 * @author Martin Sturm
 * 
 */
public class CoffeeScriptCompilerInitializationException extends AbstractCoffeeScriptException
{

	public CoffeeScriptCompilerInitializationException(String msg, Throwable cause)
	{
		super(msg, cause);
	}

}
