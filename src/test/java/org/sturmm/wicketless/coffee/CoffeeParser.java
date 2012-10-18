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

import org.apache.wicket.util.io.IOUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sturmm.wicketless.less.parser.ParserInitializationException;
import org.sturmm.wicketless.less.parser.ParsingError;
import org.sturmm.wicketless.less.source.ClasspathLessSource;
import org.sturmm.wicketless.less.source.LessSource;

/**
 * Adapter to less.js parser/compiler using Mozilla's Rhino engine.
 * 
 * @author Martin Sturm
 */
public final class CoffeeParser
{

	private static Logger LOG = LoggerFactory.getLogger(CoffeeParser.class);

	/**
	 * Singleton instance of {@link CoffeeParser}.
	 */
	public static CoffeeParser INSTANCE = new CoffeeParser();

	private Scriptable jsScope;
	private Function parserJs;

	/**
	 * Privat Constructor of {@link CoffeeParser} which initiates a Rhino
	 * javascript context with less.js environment and needed funtions.
	 * 
	 * @throws ParserInitializationException
	 *             is parser could'nt be initialized.
	 */
	private CoffeeParser()
	{
		try
		{
			LOG.debug("Initializing LessParser ...");
			Context ctx = Context.enter();

			Reader lesscss = new InputStreamReader(
					CoffeeParser.class.getResourceAsStream("coffee-script.js"));

			ctx.setOptimizationLevel(1);

			Global global = new Global();
			global.init(ctx);

			jsScope = ctx.initStandardObjects(global);

			Object $logger = Context.javaToJS(LOG, jsScope);
			ScriptableObject.putProperty(jsScope, "log", $logger);

			ctx.evaluateReader(jsScope, lesscss, "coffee-script.js", 1, null);
			NativeObject coffee = (NativeObject)jsScope.get("CoffeeScript", jsScope);
			parserJs = (Function)coffee.get("compile", jsScope);

			Scriptable newObject = ctx.newObject(jsScope);
			newObject.put("bare", newObject, true);

			String script = IOUtils.toString(CoffeeParser.class
					.getResourceAsStream("CoffeeTest.cs"));

			String js = (String)parserJs.call(ctx, jsScope, jsScope, new Object[] {
					"foo = ->\n  bar : 10, baz: 100", newObject });
			System.out.println(js);
			LOG.debug("... parser initialization successfull!");
		}
		catch (Throwable t)
		{
			throw new ParserInitializationException("Unable to initialize LessCompiler scope.", t);
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

	/**
	 * Parses a wrapped .less file and returns the {@link Scriptable} object,
	 * which is the AST of the source.
	 * 
	 * @param file
	 *            , wrapper for .less file
	 * @return the AST of {@link ClasspathLessSource} wrapped in
	 *         {@link Scriptable}
	 * @throws ParsingError
	 *             if parsing of source file fails
	 */
	public final Scriptable parse(LessSource file)
	{
		try
		{
			return (Scriptable)Context
					.call(null, parserJs, jsScope, jsScope, new Object[] { file });
		}
		catch (Exception e)
		{
			throw new ParsingError("Unable to compile less file " + file.getFilename(), e);
		}
	}

	/**
	 * Returns a singleton instance of {@link CoffeeParser}. Might throw
	 * {@link ParserInitializationException} if parser could not be initialized.
	 * 
	 * @return parser as singleton
	 */
	public static CoffeeParser getInstance()
	{
		return INSTANCE;
	}

	public static void main(String[] args)
	{
		getInstance();
	}

}
