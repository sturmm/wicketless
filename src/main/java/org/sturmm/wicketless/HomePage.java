package org.sturmm.wicketless;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sturmm.wicketless.less.LessCompiler;
import org.sturmm.wicketless.less.LessCssSource;
import org.sturmm.wicketless.resource.LessCssResourceReference;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger("LessCssPackageResource");

    public HomePage(final PageParameters parameters) {
		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
        // TODO Add your page's components here
    }

    @Override
    public void renderHead(IHeaderResponse response) {
    	super.renderHead(response);
    	log.info("redering head");
    	response.renderCSSReference(new LessCssResourceReference(LessCompiler.class, "HomePage.less"));
    }
    
    public static void main(String[] args) {
        LessCssSource file = new LessCssSource(HomePage.class, "HomePage.less");
        System.out.println(file.getCompiledSource());
    }
}
