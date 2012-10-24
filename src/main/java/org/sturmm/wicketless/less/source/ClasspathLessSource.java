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

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.lang.Packages;

/**
 * Implementation of {@link LessSource} which provides loading and importing
 * less sources from classpath.<br>
 * <br>
 * Examples:<br>
 * 
 * <pre>
 *  &#64;import "classpath:foo/bar/baz"; //import by fully qualified name
 *  &#64;import "../foo/bar.less";       //import relative to 'this'
 *  &#64;import "foo/bar.less";          //import relative to 'this'
 * </pre>
 * 
 * @author Martin Sturm
 */
public class ClasspathLessSource extends AbstractLessSource implements LessSource
{
	private static final long serialVersionUID = 1L;

	private final String filename;

	/**
	 * Creates a {@link LessSource} with name {@code filename} relative to a
	 * given class {@code scope}.<br>
	 * 
	 * @param scope
	 *            class from relative to this the resource should be loaded.
	 * @param filename
	 *            the name of the resource relative to class scope
	 * @throws ResourceException
	 *             if specified resource does'nt exist, or could not be loaded.
	 */
	public ClasspathLessSource(Class<?> scope, String filename) throws ResourceException
	{
		this(Packages.absolutePath(scope, filename));
	}

	/**
	 * Private constructor needed to create instances for resolved imports.
	 * 
	 * @param filename
	 *            the fully qualified name of the resource
	 * @throws ResourceException
	 *             if specified resource does'nt exist, or could not be loaded.
	 */
	private ClasspathLessSource(String filename) throws ResourceException
	{
		Args.notNull(filename, "filename");

		this.filename = filename.toLowerCase().endsWith(".less") ? filename : filename + ".less";

		try
		{
			URL url = this.getClass().getClassLoader().getResource(getFilename());
			if (url == null)
			{
				throw new FileNotFoundException("InputStream must not be null");
			}
		}
		catch (Exception e)
		{
			throw new ResourceException("Resource '" + getFilename() + "' not found", e);
		}
	}

	@Override
	public LessSource resolveImport(String _import) throws ResourceException
	{
		if (_import.startsWith("classpath:"))
		{
			return newChild(_import.substring("classpath:".length()));
		}
		else
		{
			File f = new File(new File(getFilename()).getParent(), _import);
			return newChild(f.getPath());
		}
	}

	private LessSource newChild(String path) throws ResourceException
	{
		return new ClasspathLessSource(path)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected ClasspathLessSource getParent()
			{
				return ClasspathLessSource.this;
			}
		};
	}

	@Override
	public String getFilename()
	{
		return filename;
	}

	@Override
	public String getSource()
	{
		try
		{
			return IOUtils.toString(this.getClass().getClassLoader()
					.getResourceAsStream(getFilename()));
		}
		catch (Exception e)
		{
			// this must not happen while we've already checked the existence of
			// the file in the constructor
			throw new RuntimeException("Somthing went wrong...", e);
		}
	}

	/**
	 * Returns the parent of an imported {@link ClasspathLessSource}. Can be
	 * used for debugging if you want to know where you are in the import
	 * hierarchy.
	 * 
	 * @return the parent if this is an imported source, else null
	 */
	protected LessSource getParent()
	{
		return null;
	}

	/**
	 * Returns true if the {@link ClasspathLessSource} has a parent (e.g. if
	 * it's an import)
	 * 
	 * @return true whether this is an imported source.
	 */
	protected final boolean hasParent()
	{
		return getParent() != null;
	}

}
