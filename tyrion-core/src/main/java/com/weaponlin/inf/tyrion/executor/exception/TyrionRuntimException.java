package com.weaponlin.inf.tyrion.executor.exception;

public class TyrionRuntimException extends RuntimeException {
    private static final long serialVersionUID = -7816634276793399533L;

    public TyrionRuntimException(String message) {
        super(message);
    }

    public TyrionRuntimException(String message, Throwable cause) {
        super(message, cause);
    }

    public TyrionRuntimException(Throwable cause) {
        super(cause);
    }

    public TyrionRuntimException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
