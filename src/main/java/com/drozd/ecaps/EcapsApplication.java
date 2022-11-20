package com.drozd.ecaps;

import com.drozd.ecaps.configuration.SecretConfigProperties;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(SecretConfigProperties.class)
public class EcapsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcapsApplication.class, args);
	}

	@Bean
	public GoogleIdTokenVerifier googleIdTokenVerifier(SecretConfigProperties secretConfigProperties){
		return new GoogleIdTokenVerifier.Builder(new ApacheHttpTransport(), new GsonFactory())
						.setAudience(List.of(secretConfigProperties.googleClientId()))
						.build();
	}

}
