package org.sturmm.wicketless;

import java.io.FileReader;
import java.io.Reader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.tools.shell.Global;


public class RhinoEngineTest {

	public static void main(String[] args) throws Throwable {
		Reader lesscss = new FileReader(RhinoEngineTest.class.getResource("less-1.3.0.min.js").getPath());
		Reader env =  new FileReader(RhinoEngineTest.class.getResource("env.rhino.js").getPath());
		
		Context ctx = Context.enter();
		ctx.setOptimizationLevel(-1);
		ctx.setLanguageVersion(Context.VERSION_1_7);
		
		Global global = new Global(); 
        global.init(ctx); 

		Scriptable scope = ctx.initStandardObjects(global);
		ctx.evaluateReader(scope, env, "<env>", 1, null);
		ctx.evaluateReader(scope, lesscss, "<lesscss>", 1, null);
	}

}
