package com.drozd.ecaps.service;

import com.drozd.ecaps.model.EcapsUser;
import com.drozd.ecaps.model.space.dto.SpaceInfoDto;
import com.drozd.ecaps.repository.EcapsUserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EcapsUserService {
    private final EcapsUserRepository userRepository;

    public Optional<EcapsUser> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(GoogleIdToken.Payload googleIdTokenPayload) throws IllegalArgumentException {
        final String userEmail = googleIdTokenPayload.getEmail();
        if(getUser(userEmail).isEmpty()){
            var user = new EcapsUser(googleIdTokenPayload);
            userRepository.save(user);
        }
    }

    public List<SpaceInfoDto> getUserSpaces(String email) {
        return getUser(email)
                .map(EcapsUser::getSpaces)
                .map(spaces -> spaces.stream().map(SpaceInfoDto::new).toList())
                .orElseThrow(() -> new NoSuchElementException("User with email " + email + " not found."));
    }
}
