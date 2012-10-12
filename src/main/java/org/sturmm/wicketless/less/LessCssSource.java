package org.sturmm.wicketless.less;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.lang.Args;

/**
 *
 * @author Martin Sturm
 */
public class LessCssSource implements Serializable{
    private static final long serialVersionUID = 1L;

    private final String filename;

    private String compiledSource;

    public LessCssSource(Class<?> scope, String filename) {
        this(scope.getPackage().getName().replace(".", "/") + "/" + filename);
    }

    private LessCssSource(String filename) {
        Args.notNull(filename, "filename");
        
        this.filename = filename.endsWith(".less") ? filename : filename+".less";

        try {
            URL url = this.getClass().getClassLoader().getResource(filename);
            if (url == null) {
                throw new FileNotFoundException("InputStream must not be null");
            }
        } catch (Exception e) {
            throw new LessResourceException("Resource not found: '"+filename+"'.", e);
        }
    }

    public LessCssSource resolveImport(String _import){
        if (_import.startsWith("classpath:")) {
            return new LessCssSource(_import.substring("classpath:".length()));
        } else {
            File f = new File(new File(filename).getParent(), _import);
            return new LessCssSource(f.getPath());
        }
    }

    public String getFilename() {
        return filename;
    }

    public String getSource() {
        try {
            return IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(filename));
        } catch (Exception e) {
            throw new LessResourceException("Unable to read source: '"+filename+"'.", e);
        }
    }

    public String getCompiledSource(){
        if (compiledSource == null) {
            compiledSource = LessCompiler.INSTANCE.compile(this);
        }
        return compiledSource;
    }

    public boolean isCompressed() {
        return false;
    }

}
