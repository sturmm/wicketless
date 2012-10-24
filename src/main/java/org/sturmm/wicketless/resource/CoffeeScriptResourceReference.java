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

import java.io.IOException;

import org.apache.wicket.Application;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.lang.Packages;
import org.sturmm.wicketless.coffee.CoffeeScriptResourceException;

/**
 * 
 * This implementation of a {@link ResourceReference} is a replacement for
 * {@link JavaScriptResourceReference} and could be used like that.<br>
 * <br>
 * Note that this implementation will compile the CoffeeScript on each request
 * if you are in Wickets development mode. Otherwise it will use the 'natural
 * caching' of {@link CoffeeScriptResource} which holds the generated JavaScripe
 * after it's generated first. So if you deliver your own implementation of
 * {@link CoffeeScriptResource} this behavior might be broken.<br>
 * The total number of compilation of the CoffeeScript source can be reduced to
 * one (in deployment mode) by using the resource reference as static constant
 * e.g.:<br>
 * 
 * <pre>
 * private static ResourceReference SCRIPT = new CoffeeScriptResourceReference(MyWebPage.class, "MyWebPage.cs");
 *   
 * {@code @Override}
 * public void renderHead(IHeaderResponse response) {
 *   ...
 *   response.renderJavaScriptReference(SCRIPT);
 * }
 * 
 * <pre>
 * 
 * @author Martin Sturm
 * 
 */
public class CoffeeScriptResourceReference extends ResourceReference
{
	private static final long serialVersionUID = 1L;
	private CoffeeScriptResource resource;

	public CoffeeScriptResourceReference(Class<?> scope, String name)
	{
		super(scope, name);
		if (Application.get().usesDevelopmentConfig())
		{
			Application.get().getResourceReferenceRegistry()
					.unregisterResourceReference(this.getKey());
		}
		Application.get().getResourceReferenceRegistry().registerResourceReference(this);

	}

	@Override
	public IResource getResource()
	{
		if (resource == null || Application.get().usesDevelopmentConfig())
		{
			String script;
			try
			{
				script = IOUtils.toString(getScope().getClassLoader().getResourceAsStream(
						getFullyQualifiedResourceName()));
			}
			catch (IOException e)
			{
				throw new CoffeeScriptResourceException("Unable to load CoffeeScript resource at '"
						+ getFullyQualifiedResourceName() + "'", e);
			}

			resource = new CoffeeScriptResource(script);
		}

		return resource;
	}

	private String getFullyQualifiedResourceName()
	{
		return Packages.absolutePath(getScope(), getName());
	}

}
