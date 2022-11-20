package com.drozd.ecaps.controller;

import com.drozd.ecaps.service.EcapsUserService;
import com.drozd.ecaps.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final EcapsUserService userService;

    @GetMapping("/signup")
    public ResponseEntity<String> signUp() throws IllegalArgumentException {
        userService.saveUser(SecurityContextUtils.getGoogleIdTokenPayload());
        return ResponseEntity
                .ok("Successfully saved user with email + " + SecurityContextUtils.getCurrentUserEmail());
    }


}
