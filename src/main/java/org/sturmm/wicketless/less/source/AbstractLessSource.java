package org.sturmm.wicketless.less.source;

import static org.mozilla.javascript.ScriptableObject.callMethod;
import static org.mozilla.javascript.ScriptableObject.putProperty;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.sturmm.wicketless.less.parser.LessParser;
import org.sturmm.wicketless.less.parser.ParsingError;

public abstract class AbstractLessSource implements LessSource
{
	private static final long serialVersionUID = 1L;
	
	private Scriptable ast;
	private String css;

	private final void parse()
	{
		if (ast == null)
		{
			ast = LessParser.INSTANCE.parse(this);
		}
	}

	/* (non-Javadoc)
	 * @see org.sturmm.wicketless.less.LessSource#getAST()
	 */
	public final Scriptable getAST()
	{
		parse();
		return ast;
	}

	/* (non-Javadoc)
	 * @see org.sturmm.wicketless.less.LessSource#toCSS()
	 */
	public final String toCSS()
	{
		parse();
		if (css == null)
		{
			try
			{
				NativeObject options = new NativeObject();
				putProperty(options, "compress", isCompressed());
				css = (String)callMethod(ast, "toCSS", new Object[] { options });
			}
			catch (Exception e)
			{
				throw new ParsingError("Unable to generate CSS", e);
			}
		}
		return css;
	}

	/* (non-Javadoc)
	 * @see org.sturmm.wicketless.less.LessSource#isCompressed()
	 */
	public boolean isCompressed()
	{
		return false;
	}
	
}
