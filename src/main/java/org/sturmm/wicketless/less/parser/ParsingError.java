/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sturmm.wicketless.less.parser;

import static org.mozilla.javascript.ScriptableObject.getTypedProperty;

import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.sturmm.wicketless.less.AbstractLessException;

/**
 * 
 * This exception will be thrown for any error which raises due the parsing a
 * resource or create css from it's AST.
 * 
 * @author Martin Sturm
 * 
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
	/*
	 * if the root cause is an JavaScriptException we assume that it's an
	 * parsing error and try to interpret it
	 */
	public String getMessage()
	{
		Throwable root = this.getCause();

		if (root != null && root instanceof JavaScriptException)
		{
			String error = "{ ";

			Scriptable value = (Scriptable)((JavaScriptException)root).getValue();

			error += "\n\tfile: '";
			error += defaultIfNull(getTypedProperty(value, "filename", String.class), "n/a");
			error += "', ";

			final int line = defaultIfNull(getTypedProperty(value, "line", Integer.class), -1);
			final int column = defaultIfNull(getTypedProperty(value, "column", Integer.class), 0);

			// extract source only if the line number indicates a source error.
			// otherwise it might be confusing.
			if (line != -1)
			{
				error += "\n\terror: { line:" + line + ", column: " + column + "}, \n\tsource:{ ";

				final String[] extract = defaultIfNull(
						getTypedProperty(value, "extract", String[].class), new String[0]);

				int startingLineNumber = extract.length >= 2 ? line - 1 : line;

				for (int i = 0; i < extract.length; i++)
				{
					if (!"undefined".equals(extract[i]))
					{
						error += "\n\t\t";
						error += startingLineNumber + i
								+ (startingLineNumber + i == line ? "*" : " ") + "| " + extract[i];
					}
				}
				error += "\n\t},";
			}

			// adding the message and replacing newlines with newline + tab for
			// having an indent
			error += "\n\tmessage: "
					+ defaultIfNull(getTypedProperty(value, "message", String.class), "n/a")
							.replaceAll("\\n", "\n\t");
			error += "\n}";
			return error;
		}
		else
		{
			return super.getMessage();
		}
	}

	/*
	 * Helper method for extracting values from scriptable
	 */
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
