package org.sturmm.wicketless;

import org.junit.Test;
import org.sturmm.wicketless.less.LessSource;
import org.sturmm.wicketless.less.ParsingError;

public class CompileTest
{

	@Test(expected = ParsingError.class)
	public void homepageRendersSuccessfully()
	{
		LessSource file = new LessSource(CompileTest.class, "HomePage.less");
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
