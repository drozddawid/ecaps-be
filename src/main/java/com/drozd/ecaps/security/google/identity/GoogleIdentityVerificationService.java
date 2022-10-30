package com.drozd.ecaps.security.google.identity;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleIdentityVerificationService {
    GoogleIdToken verify(String googleJwtToken) throws GeneralSecurityException, IOException;
}
