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

import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.IResource;
import org.sturmm.wicketless.coffee.CoffeeScriptCompiler;

/**
 * 
 * Simple implementation of {@link IResource} by extending
 * {@link ByteArrayResource}. Creates JavaSript from CoffeeScript by using
 * {@link CoffeeScriptCompiler}.
 * 
 * @author Martin Sturm
 * 
 */
public class CoffeeScriptResource extends ByteArrayResource
{
	private String script;
	private String compiledSource;

	public CoffeeScriptResource(String script)
	{
		super("text/javascript");
		this.script = script;
	}

	@Override
	protected byte[] getData(Attributes attributes)
	{
		if (compiledSource == null)
		{
			compiledSource = CoffeeScriptCompiler.getInstance().compile(script, true);
			// not required anymore!
			script = null;
		}
		return compiledSource.getBytes();
	}

	@Override
	protected void configureCache(ResourceResponse data, Attributes attributes)
	{
		super.configureCache(data, attributes);
		data.setCacheDurationToMaximum();
	}

}
