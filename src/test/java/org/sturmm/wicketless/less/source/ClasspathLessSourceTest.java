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

import junit.framework.Assert;

import org.junit.Test;

public class ClasspathLessSourceTest
{

	@Test
	public void testParsing()
	{
		LessSource file = new ClasspathLessSource(ClasspathLessSourceTest.class, "HomePage.less");
		String css = file.toCSS();
		Assert.assertTrue(css.contains(".imported"));
	}

	@Test
	public void testResolveImport()
	{
		LessSource file = new ClasspathLessSource(ClasspathLessSourceTest.class, "HomePage.less");
		LessSource resolved = file.resolveImport("Import.less");
		String source = resolved.getSource();
		Assert.assertTrue(source.startsWith("//->Import.less"));

		resolved = file.resolveImport("classpath:org/sturmm/wicketless/less/source/Import.less");
		source = resolved.getSource();
		Assert.assertTrue(source.startsWith("//->Import.less"));
	}

	@Test(expected = ResourceException.class)
	public void testResolveImportError()
	{
		LessSource file = new ClasspathLessSource(ClasspathLessSourceTest.class, "HomePage.less");
		file.resolveImport("org/sturmm/wicketless/less/source/Import.less");
	}

	@Test
	public void testCssImport()
	{
		LessSource file = new ClasspathLessSource(ClasspathLessSourceTest.class, "CssImport.less");
		String css = file.toCSS();
		System.out.println(css);
		Assert.assertTrue(css.contains("@import \"Simple.css\";"));
	}

}
