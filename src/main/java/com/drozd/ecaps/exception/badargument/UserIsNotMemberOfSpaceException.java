package com.drozd.ecaps.exception.badargument;

import com.drozd.ecaps.exception.BadArgumentException;

public class UserIsNotMemberOfSpaceException extends BadArgumentException {
    public UserIsNotMemberOfSpaceException() {
    }

    public UserIsNotMemberOfSpaceException(String message) {
        super(message);
    }

    public UserIsNotMemberOfSpaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserIsNotMemberOfSpaceException(Throwable cause) {
        super(cause);
    }

    public UserIsNotMemberOfSpaceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
