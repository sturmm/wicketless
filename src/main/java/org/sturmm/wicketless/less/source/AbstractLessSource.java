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
package org.sturmm.wicketless.less.source;

import static org.mozilla.javascript.ScriptableObject.callMethod;
import static org.mozilla.javascript.ScriptableObject.putProperty;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.sturmm.wicketless.less.parser.LessParser;
import org.sturmm.wicketless.less.parser.ParsingError;

/**
 * This Class provides the basic functinality that is needed for using with
 * {@link LessParser}. Extend this if you want to provide source from anywhere
 * else than classpath (e.g. database, http...).
 * 
 * @author Martin Sturm
 */
public abstract class AbstractLessSource implements LessSource
{
	private Scriptable ast;
	private String css;

	private final void parse()
	{
		if (ast == null)
		{
			try
			{
				ast = LessParser.INSTANCE.parse(this);
			}
			catch (RuntimeException e)
			{
				throw e;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sturmm.wicketless.less.LessSource#getAST()
	 */
	public final Scriptable getAST()
	{
		parse();
		return ast;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sturmm.wicketless.less.LessSource#toCSS()
	 */
	public final String toCSS()
	{
		parse();
		if (css == null)
		{
			try
			{
				NativeObject options = new NativeObject();
				putProperty(options, "compress", isCompressed());
				css = (String)callMethod(ast, "toCSS", new Object[] { options });
			}
			catch (Exception e)
			{
				throw new ParsingError("Unable to generate CSS" + this.getFilename(), e);
			}
		}
		return css;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sturmm.wicketless.less.LessSource#isCompressed()
	 */
	public boolean isCompressed()
	{
		return false;
	}

}
