package com.drozd.ecaps.exception.badargument;

import com.drozd.ecaps.exception.BadArgumentException;

public class UserIsNotPostAuthorException extends BadArgumentException {
    public UserIsNotPostAuthorException() {
    }

    public UserIsNotPostAuthorException(String message) {
        super(message);
    }

    public UserIsNotPostAuthorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIsNotPostAuthorException(Throwable cause) {
        super(cause);
    }

    public UserIsNotPostAuthorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
