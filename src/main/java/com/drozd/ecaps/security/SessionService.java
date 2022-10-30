package com.drozd.ecaps.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface SessionService {
    boolean isAuthenticated(GoogleIdToken idToken);
    void store(GoogleIdToken idToken);

}
