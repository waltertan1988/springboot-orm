package com.walter.orm.throwable;

public class SqlSetException extends RuntimeException {

    public SqlSetException() {
        super();
    }

    public SqlSetException(String message) {
        super(message);
    }

    public SqlSetException(String messagePattern, Object... params) {
        super(String.format(messagePattern, params));
    }

    public SqlSetException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlSetException(Throwable cause) {
        super(cause);
    }


    protected SqlSetException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
