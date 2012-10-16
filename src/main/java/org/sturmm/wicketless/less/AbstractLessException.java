package org.sturmm.wicketless.less;

public abstract class AbstractLessException extends RuntimeException
{
	private static final long serialVersionUID = 26016767918781830L;

	public AbstractLessException()
	{
		super();
	}

	public AbstractLessException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

	public AbstractLessException(String arg0)
	{
		super(arg0);
	}

	public AbstractLessException(Throwable arg0)
	{
		super(arg0);
	}

}
