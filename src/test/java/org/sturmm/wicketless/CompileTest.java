package org.sturmm.wicketless;

import org.junit.Test;
import org.sturmm.wicketless.less.parser.ParsingError;
import org.sturmm.wicketless.less.source.ClasspathLessSource;
import org.sturmm.wicketless.less.source.LessSource;

public class CompileTest
{

	@Test(expected = ParsingError.class)
	public void homepageRendersSuccessfully()
	{
		LessSource file = new ClasspathLessSource(CompileTest.class, "HomePage.less");
		try
		{
			file.toCSS();
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			throw e;
		}
	}
}
