package com.drozd.ecaps.service;

import com.drozd.ecaps.exception.UserIsNotMemberOfSpaceException;
import com.drozd.ecaps.exception.badargument.InactiveSpaceException;
import com.drozd.ecaps.exception.badargument.SpaceNotFoundException;
import com.drozd.ecaps.exception.badargument.UserNotFoundException;
import com.drozd.ecaps.model.SpaceManagerRole;
import com.drozd.ecaps.model.space.Space;
import com.drozd.ecaps.model.space.dto.SpaceInfoDto;
import com.drozd.ecaps.model.user.EcapsUser;
import com.drozd.ecaps.repository.SpaceRepository;
import com.drozd.ecaps.utils.HashGenerationUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public SpaceInfoDto getSpaceInfo(String spaceHash, String askingUserEmail) throws UserIsNotMemberOfSpaceException, InactiveSpaceException, SpaceNotFoundException {
        checkIfSpaceIsActiveByHashAndGet(spaceHash);
        return new SpaceInfoDto(getUserSpaceOrThrowIfUserIsNotMember(spaceHash, askingUserEmail));
    }

    public Space getUserSpaceOrThrowIfUserIsNotMember(String spaceHash, String askingUserEmail) throws UserIsNotMemberOfSpaceException {

        return getUserSpaces(askingUserEmail).stream()
                .filter(s -> s.getSpaceHash().equals(spaceHash))
                .findFirst()
                .orElseThrow(() -> new UserIsNotMemberOfSpaceException("User " + askingUserEmail + " is not member of space with hash " + spaceHash));
    }

    public List<SpaceInfoDto> getSpacesOwnedByUser(String ownerEmail) {
        return spaceRepository.getSpacesOwnedByUser(ownerEmail).stream().map(SpaceInfoDto::new).toList();
    }

    public List<SpaceInfoDto> getSpacesManagedByUser(String ownerEmail) {
        return spaceRepository.getSpacesManagedByUser(ownerEmail).stream().map(SpaceInfoDto::new).toList();
    }



    public List<Space> getUserSpaces(String ownerEmail) {
        return spaceRepository.getSpacesWhereUserIsMember(ownerEmail);
    }

    public SpaceInfoDto addUserToSpace(String invitationHash, String userEmail) throws SpaceNotFoundException, UserNotFoundException, InactiveSpaceException {
        var user = userService.getUser(userEmail);
        var space = checkIfSpaceIsActiveByInvitationHashAndGet(invitationHash);

        space.addUser(user);
        spaceRepository.save(space);
        return new SpaceInfoDto(space);
    }

    public SpaceInfoDto generateNewInvitationHash(Long spaceId) throws SpaceNotFoundException {
        var space = getSpaceById(spaceId);
        space.setInvitationHash(getUniqueInvitationHash());
        spaceRepository.save(space);
        return new SpaceInfoDto(space);
    }

    public Space getSpaceByInvitationHash(String invitationHash) throws SpaceNotFoundException {
        return spaceRepository.findByInvitationHash(invitationHash)
                .orElseThrow(SpaceNotFoundException::new);
    }
    public Space getSpaceByHash(String spaceHash) throws SpaceNotFoundException {
        return spaceRepository.findBySpaceHash(spaceHash)
                .orElseThrow(SpaceNotFoundException::new);
    }

    public Space getSpaceById(Long spaceId) throws SpaceNotFoundException {
        return spaceRepository.findById(spaceId)
                .orElseThrow(SpaceNotFoundException::new);
    }

    public Space checkIfSpaceIsActiveByIdAndGet(Long spaceId) throws SpaceNotFoundException, InactiveSpaceException {
        return checkIfSpaceIsActive(getSpaceById(spaceId));
    }
    public Space checkIfSpaceIsActiveByInvitationHashAndGet(String invitationHash) throws SpaceNotFoundException, InactiveSpaceException {
        return checkIfSpaceIsActive(getSpaceByInvitationHash(invitationHash));
    }
    public Space checkIfSpaceIsActiveByHashAndGet(String spaceHash) throws SpaceNotFoundException, InactiveSpaceException {
        return checkIfSpaceIsActive(getSpaceByHash(spaceHash));
    }

    public Space checkIfSpaceIsActive(Space space) throws InactiveSpaceException {
        if(!space.isActive())
            throw new InactiveSpaceException("Space " + space.getName() + " is inactive.");
        return space;
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
