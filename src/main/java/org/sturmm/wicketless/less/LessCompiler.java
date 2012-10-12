package org.sturmm.wicketless.less;

import java.io.FileReader;
import java.io.Reader;

import org.apache.wicket.util.string.AppendingStringBuffer;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Global;

/**
 * 
 * @author Martin Sturm
 *
 */
public final class LessCompiler {

    public static LessCompiler INSTANCE = new LessCompiler();

    private Scriptable jsScope;
    private Function compileJs;

    private LessCompiler() {
        try {
            Reader lesscss = new FileReader(LessCompiler.class.getResource("less-1.3.0.js").getPath());

            Context ctx = Context.enter();
            ctx.setOptimizationLevel(9);

            Global global = new Global();
            global.init(ctx);

            jsScope = ctx.initStandardObjects(global);

            Object $this = Context.javaToJS(this, jsScope);
            ScriptableObject.putProperty(jsScope, "LessCompiler", $this);

            ctx.evaluateString(jsScope, ScriptFunctions.browser, "env.js", 1, null);
            ctx.evaluateReader(jsScope, lesscss, "less-1.3.0.js", 1, null);
            compileJs = ctx.compileFunction(jsScope, ScriptFunctions.lesscompile, "lesscompile.js", 1, null);
        } catch (Throwable t) {
            throw new LessInitializationException("Unable to initialize LessCompiler scope.", t);
        } finally {
            Context.exit();
        }
    }

    public final String compile(LessCssSource file) {
        try {
            return (String) Context.call(null, compileJs, jsScope, jsScope, new Object[] { file });
        } catch (Exception e) {
            throw new LessCompilationError("Unable to compile less file " + file.getFilename(), e);
        }
    }

    private static class ScriptFunctions {
        public static final String browser, lesscompile;
        static {
            AppendingStringBuffer asb = new AppendingStringBuffer();
            asb.append("var window = {\n");
            asb.append("  location: {\n");
            asb.append("    protocol: 'file',\n");
            asb.append("    href: 'localhost',\n");
            asb.append("    port: '80'\n");
            asb.append("  },\n");
            asb.append("  document: {\n");
            asb.append("    getElementById: function(id){\n");
            asb.append("      return [];\n");
            asb.append("    },\n");
            asb.append("    getElementsByTagName: function(name){\n");
            asb.append("      return [];\n");
            asb.append("    },\n");
            asb.append("  },\n");
            asb.append("  setInterval:function(cb, int) {\n");
            asb.append("    cb.call(this,null);\n");
            asb.append("    return 0;\n");
            asb.append("  }\n");
            asb.append("};\n");
            asb.append("var location    = window.location;\n");
            asb.append("var document    = window.document;\n");
            asb.append("var setInterval = window.setInterval;");
            browser = asb.toString();
        }
        static {
            AppendingStringBuffer asb = new AppendingStringBuffer();
            asb.append("function(lessfile){\n");
            asb.append("  var result;\n");
            asb.append("  window.less.Parser.importer = function(import, paths, fn, env) {\n");
            asb.append("    var importedLessFile = env.rootfile.resolveImport(import);\n");
            asb.append("    new(window.less.Parser)({ filename: import, rootfile: importedLessFile }).parse(String(importedLessFile.getSource()), function (err, tree) {\n");
            asb.append("      if(err) throw JSON.stringify(err);\n");
            asb.append("      fn(err, tree, String(importedLessFile.getSource()));\n");
            asb.append("    });\n");
            asb.append("  };\n");
            asb.append("  new(window.less.Parser)({ filename: String(lessfile.getFilename()), rootfile: lessfile }).parse(String(lessfile.getSource()), function(err, tree){\n");
            asb.append("    if(err) throw JSON.stringify(err);\n");
            asb.append("    result = tree.toCSS({ compress: lessfile.isCompressed()});\n");
            asb.append("  });\n");
            asb.append("  return result;\n");
            asb.append("};");
            lesscompile = asb.toString();
        }
    }

}
