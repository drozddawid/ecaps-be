package com.drozd.ecaps.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import java.util.Optional;

public class GoogleIdTokenPayloadUtils {
    private final GoogleIdToken.Payload user;

    public GoogleIdTokenPayloadUtils(GoogleIdToken.Payload user) {
        this.user = user;
    }

    public String getName() {
        var name = user.get("name");
        if (name != null)
            return (String) name;

        name = user.get("given_name");
        var surname = user.get("family_name");
        if (name != null) {
            if (surname != null)
                return name + " " + surname;
            else
                return (String) name;
        } else if (surname != null) {
            return (String) surname;
        } else return "Default username";
    }

    public Optional<String> getEmail(){
        return Optional.ofNullable(user.getEmail());
    }

    public Optional<String> getPictureUrl(){
        return Optional.ofNullable((String) user.get("picture"));
    }

}
