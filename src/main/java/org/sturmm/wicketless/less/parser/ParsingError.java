package org.sturmm.wicketless.less.parser;

import static org.mozilla.javascript.ScriptableObject.getTypedProperty;

import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.json.JsonParser;
import org.sturmm.wicketless.less.AbstractLessException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/**
 * 
 * 
 * @author Martin Sturm
 */
public class ParsingError extends AbstractLessException
{
	private static final long serialVersionUID = 26016767918781830L;

	public ParsingError()
	{
		super();
	}

	public ParsingError(String msg, Throwable cause)
	{
		super(msg, cause);
	}

	public ParsingError(String msg)
	{
		super(msg);
	}

	public ParsingError(Throwable cause)
	{
		super(cause);
	}

	@Override
	public String getMessage()
	{
		Throwable root = this.getCause();
		if (root != null && root instanceof JavaScriptException)
		{
			String error = "{ ";

			Scriptable value = (Scriptable)((JavaScriptException)root).getValue();
			
			error += "\n\tfile: '" + defaultIfNull(getTypedProperty(value, "filename", String.class), "n/a") + "', ";

			final int line = defaultIfNull(getTypedProperty(value, "line", Integer.class), -1);
			final int column = defaultIfNull(getTypedProperty(value, "column", Integer.class), 0);
			if (line != -1)
			{
				error += "\n\terror: { line:" + line + ", column: " + column + "}, \n\tsource:{ ";
				
				final String[] extract = defaultIfNull(getTypedProperty(value, "extract", String[].class),
						new String[0]);
				
				int startingLineNumber = extract.length >= 2 ? line - 1 : line;
				
				for (int i = 0; i < extract.length; i++)
				{
					if (!"undefined".equals(extract[i]))
					{
						error += "\n\t\t";
						error += startingLineNumber + i + (startingLineNumber + i == line ? "*" : " ") + "| "
								+ extract[i];
					}
				}
				error += "\n\t},";
			}
			error += "\n\tmessage: " + defaultIfNull(getTypedProperty(value, "message", String.class), "n/a").replaceAll("\\n", "\n\t");
			error += "\n}";
			return error;
		}
		else
		{
			return super.getMessage();
		}
	}

	private static <T> T defaultIfNull(T value, T _default)
	{
		if (value == null)
		{
			return _default;
		}
		else
		{
			return value;
		}
	}

}
