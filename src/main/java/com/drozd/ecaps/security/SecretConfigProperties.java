package com.drozd.ecaps.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "secrets")
public record SecretConfigProperties(
        String googleClientId,
        String googleClientSecret
) { }
