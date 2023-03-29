package com.drozd.ecaps.controller;

import com.drozd.ecaps.configuration.SpaceGoogleAuthorizationCodeDto;
import com.drozd.ecaps.exception.BadArgumentException;
import com.drozd.ecaps.exception.GoogleAuthorizationFailedException;
import com.drozd.ecaps.model.space.dto.SpaceInfoDto;
import com.drozd.ecaps.service.SpaceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController()
@RequestMapping("oauth2/google")
@RequiredArgsConstructor
public class GoogleOauth2Controller {
    private final SpaceService spaceService;
    private final Logger log = LoggerFactory.getLogger(GoogleOauth2Controller.class);

    @PostMapping
    public ResponseEntity<SpaceInfoDto> authorizeSpace(@RequestBody SpaceGoogleAuthorizationCodeDto authCode, HttpServletRequest request)
            throws BadArgumentException, GoogleAuthorizationFailedException,
            JsonProcessingException {
        try {
            var spaceInfo = spaceService.authorizeAndSetSpaceDriveAccount(authCode, request.getHeader(HttpHeaders.ORIGIN));
            return ResponseEntity.ok(spaceInfo);
        }catch(IOException | GeneralSecurityException e){
            log.error("Authorizing space failed. ", e);
            throw new GoogleAuthorizationFailedException("Authorization failed.");
        }
    }


}
