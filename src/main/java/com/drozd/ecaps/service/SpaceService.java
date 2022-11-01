package com.drozd.ecaps.service;

import com.drozd.ecaps.model.EcapsUser;
import com.drozd.ecaps.model.SpaceManagerRole;
import com.drozd.ecaps.model.space.Space;
import com.drozd.ecaps.model.space.dto.SpaceInfoDto;
import com.drozd.ecaps.repository.SpaceRepository;
import com.drozd.ecaps.utils.HashGenerationUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SpaceService {
    private final Logger log = LoggerFactory.getLogger(SpaceService.class);
    private final EcapsUserService userService;
    private final SpaceRepository spaceRepository;

    public SpaceInfoDto createSaveAndGetSpace(String spaceName, String ownerEmail) {
        return userService.getUser(ownerEmail)
                .map(u -> createSaveAndGetSpace(spaceName, u))
                .map(SpaceInfoDto::new)
                .orElseThrow(() ->
                        new NoSuchElementException("User with email " + ownerEmail + " doesn't exist."));
    }

    public List<SpaceInfoDto> getSpacesOwnedByUser(String ownerEmail) {
        return spaceRepository.getSpacesOwnedByUser(ownerEmail).stream().map(SpaceInfoDto::new).toList();
    }

    private Space createSaveAndGetSpace(String spaceName, EcapsUser owner) {
        Space space = new Space(spaceName);
        space.setSpaceHash(getUniqueSpaceHash());
        space.setInvitationHash(getUniqueInvitationHash());
        space.addSpaceManager(owner, SpaceManagerRole.OWNER);
        space.addUser(owner);
        spaceRepository.save(space);
        log.debug("Created new space. {}", space);
        return space;
    }

    private String getUniqueInvitationHash() {
        return HashGenerationUtils.getUniqueHash(spaceRepository::existsByInvitationHash, 60);
    }

    private String getUniqueSpaceHash() {
        return HashGenerationUtils.getUniqueHash(spaceRepository::existsBySpaceHash, 30);
    }

}
