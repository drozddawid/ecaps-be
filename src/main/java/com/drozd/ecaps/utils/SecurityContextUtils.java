package com.drozd.ecaps.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtils {
    public static String getCurrentUserEmail(){
        return getGoogleIdTokenPayload().getEmail();
    }

    public static GoogleIdToken.Payload getGoogleIdTokenPayload() {
        return getGoogleIdToken().getPayload();
    }

    public static GoogleIdToken getGoogleIdToken() {
        return (GoogleIdToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
