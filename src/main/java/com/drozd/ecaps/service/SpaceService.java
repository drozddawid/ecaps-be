package com.drozd.ecaps.service;

import com.drozd.ecaps.exception.UserIsNotMemberOfSpaceException;
import com.drozd.ecaps.exception.badargument.SpaceNotFoundException;
import com.drozd.ecaps.exception.badargument.UserNotFoundException;
import com.drozd.ecaps.model.SpaceManagerRole;
import com.drozd.ecaps.model.space.Space;
import com.drozd.ecaps.model.space.dto.SpaceInfoDto;
import com.drozd.ecaps.model.user.dto.EcapsUser;
import com.drozd.ecaps.repository.SpaceRepository;
import com.drozd.ecaps.utils.HashGenerationUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceService {
    private final Logger log = LoggerFactory.getLogger(SpaceService.class);
    private final EcapsUserService userService;
    private final SpaceRepository spaceRepository;

    public SpaceInfoDto createSaveAndGetSpace(String spaceName, String ownerEmail) throws UserNotFoundException {
        var user = userService.getUser(ownerEmail);
        var space = createSaveAndGetSpace(spaceName, user);
        return new SpaceInfoDto(space);
    }

    public SpaceInfoDto getSpaceInfo(String spaceHash, String askingUserEmail) throws UserIsNotMemberOfSpaceException {
        return new SpaceInfoDto(getUserSpaceOrThrowIfUserIsNotMember(spaceHash, askingUserEmail));
    }

    public Space getUserSpaceOrThrowIfUserIsNotMember(String spaceHash, String askingUserEmail) throws UserIsNotMemberOfSpaceException {
        return getUserSpaces(askingUserEmail).stream()
                .filter(s -> s.getSpaceHash().equals(spaceHash))
                .findFirst()
                .orElseThrow(() -> new UserIsNotMemberOfSpaceException("User " + askingUserEmail + " is not member of space with hash" + spaceHash));
    }

    public List<SpaceInfoDto> getSpacesInfoOwnedByUser(String ownerEmail) {
        return spaceRepository.getSpacesOwnedByUser(ownerEmail).stream().map(SpaceInfoDto::new).toList();
    }

    public List<Space> getUserSpaces(String ownerEmail) {
        return spaceRepository.getSpacesWhereUserIsMember(ownerEmail);
    }

    public Space getSpaceById(Long id) throws SpaceNotFoundException {
        try {
            return spaceRepository.getReferenceById(id);
        } catch (EntityNotFoundException e) {
            throw new SpaceNotFoundException("Space with id " + id + " not found.", e);
        }
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
