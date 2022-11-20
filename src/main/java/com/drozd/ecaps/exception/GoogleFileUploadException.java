package com.drozd.ecaps.exception;

public class GoogleFileUploadException extends Exception{
    public GoogleFileUploadException() {
    }

    public GoogleFileUploadException(String message) {
        super(message);
    }

    public GoogleFileUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public GoogleFileUploadException(Throwable cause) {
        super(cause);
    }

    public GoogleFileUploadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
