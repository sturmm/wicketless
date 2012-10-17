package org.sturmm.wicketless.resource;

import org.apache.wicket.Application;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.sturmm.wicketless.less.source.ClasspathLessSource;
import org.sturmm.wicketless.less.source.LessSource;

public class LessCssResource extends ByteArrayResource
{

	private static final long serialVersionUID = 1L;

	private final LessSource source;

	public LessCssResource(Class<?> scope, String name)
	{
		super("text/css");
		source = new ClasspathLessSource(scope, name)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCompressed()
			{
				return Application.get().usesDeploymentConfig();
			}
		};
	}

	@Override
	protected byte[] getData(final Attributes attributes)
	{
		return source.toCSS().getBytes();
	}

	@Override
	protected void configureResponse(ResourceResponse response, Attributes attributes)
	{
		super.configureResponse(response, attributes);
		response.setCacheDurationToMaximum();
	}

	public LessSource getSource()
	{
		return source;
	}
}
