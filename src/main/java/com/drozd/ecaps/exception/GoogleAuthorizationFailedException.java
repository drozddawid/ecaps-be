package com.drozd.ecaps.exception;

public class GoogleAuthorizationFailedException extends Exception{
    public GoogleAuthorizationFailedException() {
    }

    public GoogleAuthorizationFailedException(String message) {
        super(message);
    }

    public GoogleAuthorizationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public GoogleAuthorizationFailedException(Throwable cause) {
        super(cause);
    }

    public GoogleAuthorizationFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
