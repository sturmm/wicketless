package org.sturmm.wicketless.less;

import static org.mozilla.javascript.ScriptableObject.callMethod;
import static org.mozilla.javascript.ScriptableObject.putProperty;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.net.URL;

import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.lang.Args;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

/**
 * 
 * @author Martin Sturm
 */
public class LessSource implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final String filename;

	private Scriptable ast;
	private String css;

	public LessSource(Class<?> scope, String filename)
	{
		this(scope.getPackage().getName().replace(".", "/") + "/" + filename);
	}

	private LessSource(String filename)
	{
		Args.notNull(filename, "filename");

		this.filename = filename.endsWith(".less") ? filename : filename + ".less";

		try
		{
			URL url = this.getClass().getClassLoader().getResource(getFilename());
			if (url == null)
			{
				throw new FileNotFoundException("InputStream must not be null");
			}
		}
		catch (Exception e)
		{
			throw new ResourceException("Resource '" + getFilename() + "' not found", e);
		}
	}

	public LessSource resolveImport(String _import)
	{
		if (_import.startsWith("classpath:"))
		{
			return newChild(_import.substring("classpath:".length()));
		}
		else
		{
			File f = new File(new File(getFilename()).getParent(), _import);
			return newChild(f.getPath());
		}
	}

	private LessSource newChild(String path)
	{
		return new LessSource(path)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected LessSource getParent()
			{
				return LessSource.this;
			}
		};
	}

	public String getFilename()
	{
		return filename;
	}

	public String getSource()
	{
		try
		{
			return IOUtils.toString(this.getClass().getClassLoader()
					.getResourceAsStream(getFilename()));
		}
		catch (Exception e)
		{
			// this must not happen while we've already checked the existence of
			// the file in the constructor
			throw new RuntimeException("Somthing went wrong...", e);
		}
	}

	private final void parse()
	{
		if (ast == null)
		{
			ast = LessParser.INSTANCE.parse(this);
		}
	}

	public final Scriptable getAST()
	{
		parse();
		return ast;
	}

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

	public boolean isCompressed()
	{
		return false;
	}

	protected LessSource getParent()
	{
		return null;
	}

	protected final boolean hasParent()
	{
		return getParent() != null;
	}

}
