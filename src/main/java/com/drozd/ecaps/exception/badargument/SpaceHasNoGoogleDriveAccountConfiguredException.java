package com.drozd.ecaps.exception.badargument;

import com.drozd.ecaps.exception.BadArgumentException;

public class SpaceHasNoGoogleDriveAccountConfiguredException extends BadArgumentException {
    public SpaceHasNoGoogleDriveAccountConfiguredException() {
    }

    public SpaceHasNoGoogleDriveAccountConfiguredException(String message) {
        super(message);
    }

    public SpaceHasNoGoogleDriveAccountConfiguredException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpaceHasNoGoogleDriveAccountConfiguredException(Throwable cause) {
        super(cause);
    }

    public SpaceHasNoGoogleDriveAccountConfiguredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
