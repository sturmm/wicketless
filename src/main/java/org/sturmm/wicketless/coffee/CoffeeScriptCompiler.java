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
package org.sturmm.wicketless.coffee;

import java.io.InputStreamReader;
import java.io.Reader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sturmm.wicketless.less.parser.LessParserInitializationException;

/**
 * Adapter to coffee-script.js parser/compiler using Mozilla's Rhino engine.
 * 
 * @author Martin Sturm
 */
public final class CoffeeScriptCompiler
{

	private static Logger LOG = LoggerFactory.getLogger(CoffeeScriptCompiler.class);

	/**
	 * Singleton instance of {@link CoffeeScriptCompiler}.
	 */
	public static CoffeeScriptCompiler INSTANCE = new CoffeeScriptCompiler();

	private Scriptable jsScope;
	private Function compileFunction;

	/**
	 * Privat Constructor of {@link CoffeeScriptCompiler} which initiates a
	 * Rhino javascript context with less.js environment and needed funtions.
	 * 
	 * @throws LessParserInitializationException
	 *             is parser could'nt be initialized.
	 */
	private CoffeeScriptCompiler()
	{
		try
		{
			LOG.debug("Initializing CoffeeCompiler ...");
			Context ctx = Context.enter();

			Reader lesscss = new InputStreamReader(
					CoffeeScriptCompiler.class.getResourceAsStream("coffee-script.js"));

			ctx.setOptimizationLevel(1);

			Global global = new Global();
			global.init(ctx);

			jsScope = ctx.initStandardObjects(global);

			Object $logger = Context.javaToJS(LOG, jsScope);
			ScriptableObject.putProperty(jsScope, "log", $logger);

			ctx.evaluateReader(jsScope, lesscss, "coffee-script.js", 1, null);
			NativeObject coffeeScript = (NativeObject)jsScope.get("CoffeeScript", jsScope);
			compileFunction = (Function)coffeeScript.get("compile", jsScope);

			LOG.debug("... compiler initialization successfull!");
		}
		catch (Throwable t)
		{
			throw new CoffeeScriptCompilerInitializationException(
					"Unable to initialize CoffeeCompiler scope.", t);
		}
		finally
		{
			try
			{
				Context.exit();
			}
			catch (Exception e)
			{
				LOG.warn("Exception raised while exiting context.", e);
			}
		}
	}

	public final String compile(String source, boolean bare)
	{

		try
		{
			Scriptable newObject = new NativeObject();
			newObject.put("bare", newObject, bare);

			return (String)Context.call(null, compileFunction, jsScope, jsScope, new Object[] {
					source, newObject });
		}
		catch (Exception e)
		{
			throw new CoffeeScriptParsingError("Unable to compile CoffeeScript.", e);
		}
	}

	/**
	 * Returns a singleton instance of {@link CoffeeScriptCompiler}. Might throw
	 * {@link LessParserInitializationException} if parser could not be
	 * initialized.
	 * 
	 * @return parser as singleton
	 */
	public static CoffeeScriptCompiler getInstance()
	{
		return INSTANCE;
	}

}
