package com.drozd.ecaps.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "config")
public record ConfigProperties(
        String allowedOrigins,
        String appName
) { }
