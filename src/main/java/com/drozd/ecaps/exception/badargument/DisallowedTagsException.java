package com.drozd.ecaps.exception.badargument;

import com.drozd.ecaps.exception.BadArgumentException;

public class DisallowedTagsException extends BadArgumentException {
    public DisallowedTagsException() {
    }

    public DisallowedTagsException(String message) {
        super(message);
    }

    public DisallowedTagsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisallowedTagsException(Throwable cause) {
        super(cause);
    }

    public DisallowedTagsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
