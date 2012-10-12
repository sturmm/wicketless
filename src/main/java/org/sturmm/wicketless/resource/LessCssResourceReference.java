package org.sturmm.wicketless.resource;

import org.apache.wicket.Application;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

public class LessCssResourceReference extends ResourceReference {
    private static final long serialVersionUID = 1L;
    private final LessCssResource resource;

    /**
     * Construct.
     *
     * @param scope
     *            mandatory parameter
     * @param name
     *            mandatory parameter
     */
    public LessCssResourceReference(Class<?> scope, String name) {
        super(scope, name);
        if (Application.get().usesDevelopmentConfig()) {
            Application.get().getResourceReferenceRegistry().unregisterResourceReference(this.getKey());
        }
        Application.get().getResourceReferenceRegistry().registerResourceReference(this);
        resource = new LessCssResource(getScope(), getName());
    }

    @Override
    public IResource getResource() {
        return resource;
    }

}
