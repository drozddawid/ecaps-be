package com.drozd.ecaps.exception.badargument;

public class SpaceNotFoundException extends NotFoundException {
    public SpaceNotFoundException() {
    }

    public SpaceNotFoundException(String message) {
        super(message);
    }

    public SpaceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpaceNotFoundException(Throwable cause) {
        super(cause);
    }

    public SpaceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
