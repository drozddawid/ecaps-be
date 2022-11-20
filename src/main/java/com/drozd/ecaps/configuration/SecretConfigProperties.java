package com.drozd.ecaps.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "secrets")
public record SecretConfigProperties(
        String googleClientId,
        String googleClientSecret,
        String googleClientJson
) { }
