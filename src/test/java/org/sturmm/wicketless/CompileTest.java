package org.sturmm.wicketless;

import junit.framework.Assert;

import org.junit.Test;
import org.sturmm.wicketless.less.parser.LessParser;
import org.sturmm.wicketless.less.source.ClasspathLessSource;
import org.sturmm.wicketless.less.source.LessSource;

public class CompileTest
{

	@Test
	public void parseSuccessfully()
	{
		LessParser.getInstance();
		long foo = System.currentTimeMillis();
		LessSource file = new ClasspathLessSource(CompileTest.class, "HomePage.less");
		String css = file.toCSS();
		System.out.println("Compilation tooks: " + (System.currentTimeMillis() - foo) + "ms");
		System.out.println(css);
		Assert.assertTrue(css.contains(".imported"));
	}
}
