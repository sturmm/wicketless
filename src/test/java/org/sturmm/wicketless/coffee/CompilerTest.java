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

import static junit.framework.Assert.assertEquals;

import java.io.IOException;

import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.lang.Packages;
import org.junit.Before;
import org.junit.Test;

public class CompilerTest
{

	private boolean bare;

	@Before
	public void setup()
	{
		bare = true;
	}

	@Test
	public void parseAndGenerateUncompressed() throws IOException
	{
		String file = Packages.absolutePath(CompilerTest.class, "CoffeeTest.cs");
		CoffeeScriptCompiler.getInstance().compile(
				IOUtils.toString(CompilerTest.class.getClassLoader().getResourceAsStream(file)),
				bare);
	}

	@Test
	public void parseAndGenerateBare()
	{
		String src = "number = 40";
		String expectedResult = "var number;\n\nnumber = 40;\n";
		assertEquals(expectedResult, CoffeeScriptCompiler.getInstance().compile(src, bare));
	}


	@Test(expected = CoffeeScriptParsingError.class)
	public void parseAndError()
	{
		String src = ".number = 40";
		CoffeeScriptCompiler.getInstance().compile(src, bare);
	}

}
