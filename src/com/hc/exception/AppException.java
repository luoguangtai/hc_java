package com.hc.exception;

public class AppException extends RuntimeException implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private Throwable ex = null;

    public AppException(String message) {
        super(message);
    }

    public AppException(Throwable ex) {
        super(ex);
        this.ex = ex;
    }

    public AppException(String message, Throwable ex) {
        super(message);
        this.ex = ex;
    }

    public Throwable getCause() {
        return ex;
    }
}
