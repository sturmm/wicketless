package org.sturmm.wicketless.less;

/**
 * 
 * 
 * @author Martin Sturm
 */
public class LessCompilationError extends LessCssException {
    private static final long serialVersionUID = 26016767918781830L;

    public LessCompilationError() {
        super();
    }

    public LessCompilationError(String msg, Throwable cause) {
        super(msg, cause);
    }

    public LessCompilationError(String msg) {
        super(msg);
    }

    public LessCompilationError(Throwable cause) {
        super(cause);
    }

}
