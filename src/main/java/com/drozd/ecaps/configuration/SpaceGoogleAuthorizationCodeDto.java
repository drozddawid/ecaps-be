package com.drozd.ecaps.configuration;

public record SpaceGoogleAuthorizationCodeDto(
        String code,
        String scopes,
        Long spaceId
){
}
