package com.drozd.ecaps.service;

import com.drozd.ecaps.model.EcapsUser;
import com.drozd.ecaps.model.Space;
import com.drozd.ecaps.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SpaceService {
    private final EcapsUserService userService;
    private final SpaceRepository spaceRepository;

    public Space createSaveAndGetSpace(String spaceName, String ownerEmail){
        return userService.getUser(ownerEmail)
                .map(u -> createSaveAndGetSpace(spaceName, u))
                .orElseThrow(() ->
                        new NoSuchElementException("User with email " + ownerEmail + " doesn't exist."));


    }

    private Space createSaveAndGetSpace(String spaceName, EcapsUser owner){
        var space = new Space(spaceName);
return null;
    }


}
