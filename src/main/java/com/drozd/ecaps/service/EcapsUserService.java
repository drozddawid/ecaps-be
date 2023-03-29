package com.drozd.ecaps.service;

import com.drozd.ecaps.exception.badargument.UserNotFoundException;
import com.drozd.ecaps.model.space.dto.SpaceInfoDto;
import com.drozd.ecaps.model.user.EcapsUser;
import com.drozd.ecaps.repository.EcapsUserRepository;
import com.drozd.ecaps.utils.GoogleIdTokenPayloadUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EcapsUserService {
    private final EcapsUserRepository userRepository;

    public EcapsUser getUser(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));
    }

    public void saveUser(GoogleIdToken.Payload googleIdTokenPayload){
        final String userEmail = googleIdTokenPayload.getEmail();
        EcapsUser user;
        try{
            user = getUser(userEmail);
            new GoogleIdTokenPayloadUtils(googleIdTokenPayload)
                    .getPictureUrl()
                            .ifPresent(user::setPictureURL);
        }catch(UserNotFoundException e){
            user = new EcapsUser(googleIdTokenPayload);
        }
        userRepository.save(user);
    }

    public List<SpaceInfoDto> getUserSpaces(String email) throws UserNotFoundException{
        return getUser(email)
                .getSpaces().stream()
                .map(SpaceInfoDto::new)
                .toList();
    }
    
    
    


}
