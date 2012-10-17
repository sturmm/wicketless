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

import java.io.Serializable;

import org.mozilla.javascript.Scriptable;
import org.sturmm.wicketless.less.parser.LessParser;

/**
 * Wrapper interface for .less files that provides all methods needed by
 * {@link LessParser} for parsing the source and for creation of CSS. The most
 * important concept is, that the wrapper itself knows how to resolve it's
 * imports. So you are able to load and import sources from wherever you want
 * (e.g. from classpath resource, database, http...).<br>
 * <br>
 * <b>Note: It is not recommended to implement this interface. Extend
 * {@link AbstractLessSource} instead.</b>
 * 
 * @author Martin Sturm
 */
public interface LessSource extends Serializable
{
	/**
	 * Resolves Less import (e.g. '@import "foo";') to the according LessSource.
	 * 
	 * @param path
	 *            for the file to import.
	 * @return LessSource wrapper pointing to the imported file
	 * @throws ResourceException
	 *             if import couldn't be resolved.
	 */
	public LessSource resolveImport(String path) throws ResourceException;

	/**
	 * Returns the filename which you'll see e.g. in parsing error messages.
	 * 
	 * @return filename as String
	 */
	public String getFilename();

	/**
	 * Returns the source of the less file as String.
	 * 
	 * @return source as String
	 */
	public String getSource();

	/**
	 * Returns syntax tree created by less.js compiler.
	 * 
	 * @return AST as {@link Scriptable}
	 */
	public Scriptable getAST();

	/**
	 * Returns the CSS generated from less source.
	 * 
	 * @return generated CSS as String
	 */
	public String toCSS();

	/**
	 * Returns true if less js should generate minified CSS, else false.
	 * 
	 * @return true if generated css should be minified
	 */
	public boolean isCompressed();

}