package com.drozd.ecaps.exception.badargument;

import com.drozd.ecaps.exception.BadArgumentException;

public class UserIsNotManagerOfSpaceException extends BadArgumentException {
    public UserIsNotManagerOfSpaceException() {
    }

    public UserIsNotManagerOfSpaceException(String message) {
        super(message);
    }

    public UserIsNotManagerOfSpaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIsNotManagerOfSpaceException(Throwable cause) {
        super(cause);
    }

    public UserIsNotManagerOfSpaceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
