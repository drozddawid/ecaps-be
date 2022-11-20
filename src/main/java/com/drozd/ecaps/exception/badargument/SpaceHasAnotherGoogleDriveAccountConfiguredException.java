package com.drozd.ecaps.exception.badargument;

import com.drozd.ecaps.exception.BadArgumentException;

public class SpaceHasAnotherGoogleDriveAccountConfiguredException extends BadArgumentException {
    public SpaceHasAnotherGoogleDriveAccountConfiguredException() {
    }

    public SpaceHasAnotherGoogleDriveAccountConfiguredException(String message) {
        super(message);
    }

    public SpaceHasAnotherGoogleDriveAccountConfiguredException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpaceHasAnotherGoogleDriveAccountConfiguredException(Throwable cause) {
        super(cause);
    }

    public SpaceHasAnotherGoogleDriveAccountConfiguredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
