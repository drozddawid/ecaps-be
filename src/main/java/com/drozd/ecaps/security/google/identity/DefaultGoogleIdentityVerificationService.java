package com.drozd.ecaps.security.google.identity;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
@RequiredArgsConstructor
public class DefaultGoogleIdentityVerificationService implements GoogleIdentityVerificationService {
    private final GoogleIdTokenVerifier idTokenVerifier;

    @Override
    public GoogleIdToken verify(String googleJwtToken) throws GeneralSecurityException, IOException {
        return idTokenVerifier.verify(googleJwtToken);
    }
}
