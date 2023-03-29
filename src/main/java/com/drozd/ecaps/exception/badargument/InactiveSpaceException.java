package com.drozd.ecaps.exception.badargument;

import com.drozd.ecaps.exception.BadArgumentException;

public class InactiveSpaceException extends BadArgumentException {
    public InactiveSpaceException() {
    }

    public InactiveSpaceException(String message) {
        super(message);
    }

    public InactiveSpaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InactiveSpaceException(Throwable cause) {
        super(cause);
    }

    public InactiveSpaceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
