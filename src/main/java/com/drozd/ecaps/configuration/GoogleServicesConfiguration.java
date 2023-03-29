package com.drozd.ecaps.configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

@Configuration
@RequiredArgsConstructor
public class GoogleServicesConfiguration {
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static final String TOKENS_DIRECTORY_PATH = "tokens";

    private final SecretConfigProperties secretConfigProperties;

    @Bean
    public GoogleClientSecrets googleClientSecrets() throws IOException {
        final String googleClientSecretJson = secretConfigProperties.googleClientJson();
        return GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(
                        new ByteArrayInputStream(googleClientSecretJson.getBytes(StandardCharsets.UTF_8)))
        );
    }

    @Bean
    public NetHttpTransport netHttpTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean
    public FileDataStoreFactory fileDataStoreFactory() throws IOException {
        return new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH));
    }

}
