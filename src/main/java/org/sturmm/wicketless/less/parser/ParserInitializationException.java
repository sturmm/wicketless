package org.sturmm.wicketless.less.parser;

import org.sturmm.wicketless.less.AbstractLessException;

class ParserInitializationException extends AbstractLessException
{
	private static final long serialVersionUID = 1L;

	public ParserInitializationException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}