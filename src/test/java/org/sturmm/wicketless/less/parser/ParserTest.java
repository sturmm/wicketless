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
package org.sturmm.wicketless.less.parser;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.sturmm.wicketless.less.source.AbstractLessSource;
import org.sturmm.wicketless.less.source.LessSource;

public class ParserTest
{

	private boolean compressed;

	@Before
	public void setup()
	{
		compressed = false;
	}

	@Test
	public void parseAndGenerateUncompressed()
	{
		LessSource src = new StringLessSource(".foo{ height: 15px;}");
		String expectedResult = ".foo {\n  height: 15px;\n}\n";
		assertEquals(expectedResult, src.toCSS());
	}

	@Test
	public void parseAndGenerateCompressed()
	{
		compressed = true;
		LessSource src = new StringLessSource(".foo{ height: 15px;}");
		String expectedResult = ".foo{height:15px;}\n";
		assertEquals(expectedResult, src.toCSS());
	}

	@Test(expected = LessParsingError.class)
	public void parseAndError()
	{
		compressed = true;
		LessSource src = new StringLessSource(".foo<{\n  height: 15px;\n}");
		try
		{
			src.toCSS();
		}
		catch (RuntimeException e)
		{
			String msg = e.getMessage();

			assertTrue(msg.contains("\n\t\t1*| .foo<{"));

			throw e;
		}
	}

	@Test
	public void importSource()
	{
		final LessSource toImport = new StringLessSource("@color: #f00baa;");
		LessSource src = new StringLessSource("@import \"foo\";\n.foo{\n  background: @color;\n}")
		{
			@Override
			public LessSource resolveImport(String path)
			{
				assertEquals("foo.less", path);
				return toImport;
			}
		};
		String css = src.toCSS();
		assertTrue(css.contains("background: #f00baa"));
	}


	private class StringLessSource extends AbstractLessSource
	{
		private static final long serialVersionUID = 1L;

		private final String source;

		public StringLessSource(String source)
		{
			this.source = source;
		}

		@Override
		public LessSource resolveImport(String path)
		{
			throw new UnsupportedOperationException("There's nothing to import!");
		}

		@Override
		public String getFilename()
		{
			return "TestSource.less.";
		}

		@Override
		public String getSource()
		{
			return source;
		}

		@Override
		public boolean isCompressed()
		{
			return compressed;
		}

	}
}
