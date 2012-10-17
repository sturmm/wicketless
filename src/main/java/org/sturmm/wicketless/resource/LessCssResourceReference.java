package org.sturmm.wicketless.resource;

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
import org.apache.wicket.Application;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.sturmm.wicketless.less.source.ClasspathLessSource;
import org.sturmm.wicketless.less.source.LessSource;

/**
 * 
 * This implementation of a {@link ResourceReference} is a replacement for
 * {@link CssResourceReference} and could be used like that.<br>
 * <br>
 * Note that this implementation will compile the {@link LessSource} on each
 * request if you are in Wickets development mode. Otherwise it will use the
 * 'natural caching' of {@link ClasspathLessSource} which holds it's own css
 * after it's generated first. So if you deliver your own implementation of
 * {@link LessSource} this behavior might be broken.<br>
 * The total number of compilation of the {@link LessSource} can be reduced to
 * one (in deployment mode) by using the resource reference as static constant
 * e.g.:<br>
 * 
 * <pre>
 * private static ResourceReference CSS = new LessCssResourceReference(MyWebPage.class, "MyWebPage.less");
 *   
 * {@code @Override}
 * public void renderHead(IHeaderResponse response) {
 *   ...
 *   response.renderCSSReference(CSS);
 * }
 * 
 * <pre>
 * 
 * @author Martin Sturm
 * 
 */
public class LessCssResourceReference extends ResourceReference
{
	private static final long serialVersionUID = 1L;
	private LessCssResource resource;

	public LessCssResourceReference(Class<?> scope, String name)
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
			resource = new LessCssResource(getScope(), getName());
		}

		return resource;
	}

}
