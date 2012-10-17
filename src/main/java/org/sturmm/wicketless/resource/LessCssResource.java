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
package org.sturmm.wicketless.resource;

import org.apache.wicket.Application;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.IResource;
import org.sturmm.wicketless.less.source.ClasspathLessSource;
import org.sturmm.wicketless.less.source.LessSource;

/**
 * 
 * Simple implementation of {@link IResource} by extending
 * {@link ByteArrayResource}. Creates {@link ClasspathLessSource} and delivers
 * it's css as byte array.
 * 
 * @author Martin Sturm
 * 
 */
public class LessCssResource extends ByteArrayResource implements IResource
{

	private static final long serialVersionUID = 1L;

	private final LessSource source;

	public LessCssResource(Class<?> scope, String name)
	{
		super("text/css");
		source = new ClasspathLessSource(scope, name)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCompressed()
			{
				return Application.get().usesDeploymentConfig();
			}
		};
	}

	@Override
	protected byte[] getData(final Attributes attributes)
	{
		return source.toCSS().getBytes();
	}

	@Override
	protected void configureResponse(ResourceResponse response, Attributes attributes)
	{
		super.configureResponse(response, attributes);
		// to minimize less compilation we're setting cache duration to max for
		// this resource
		response.setCacheDurationToMaximum();
	}

	/**
	 * Getter for {@link LessSource} wrapped by this {@link IResource}. For use
	 * in unit tests for example if you want to test if your static resource
	 * reference returns the correct css.
	 * 
	 * @return the LessSource created by this Resource
	 */
	public LessSource getSource()
	{
		return source;
	}
}
