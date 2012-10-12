package org.sturmm.wicketless.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.wicket.Application;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.caching.IStaticCacheableResource;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.sturmm.wicketless.less.LessCssSource;

public class LessCssResource extends ByteArrayResource implements IStaticCacheableResource{

    private static final long serialVersionUID = 1L;

    private final LessCssSource source;

    public LessCssResource(Class<?> scope, String name) {
        super("text/css");
        source = new LessCssSource(scope, name){
            @Override
            public boolean isCompressed() {
                return Application.get().usesDeploymentConfig();
            }
        };
    }

    @Override
    protected byte[] getData(final Attributes attributes) {
        return source.getCompiledSource().getBytes();
    }

    @Override
    protected void configureResponse(ResourceResponse response, Attributes attributes) {
        super.configureResponse(response, attributes);
        response.setCacheDurationToMaximum();
    }

    @Override
    public Serializable getCacheKey() {
        return source.getFilename();
    }

    @Override
    public IResourceStream getCacheableResourceStream() {
        return new AbstractResourceStream() {
            private static final long serialVersionUID = 1L;

            private InputStream is;
            
            @Override
            public InputStream getInputStream() throws ResourceStreamNotFoundException {
                if(is == null){
                    is = new ByteArrayInputStream(source.getCompiledSource().getBytes());
                }
                return is;
            }
            
            @Override
            public void close() throws IOException {
                IOUtils.close(is);
            }
        };
    }

}
