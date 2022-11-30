package com.drozd.ecaps.controller;

import com.drozd.ecaps.exception.badargument.*;
import com.drozd.ecaps.model.space.dto.SpaceInfoDto;
import com.drozd.ecaps.service.EcapsUserService;
import com.drozd.ecaps.service.SpaceService;
import com.drozd.ecaps.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/spaces")
@RequiredArgsConstructor
public class SpaceController {
    private final SpaceService spaceService;
    private final EcapsUserService userService;

    @PostMapping()
    public ResponseEntity<SpaceInfoDto> createSpace(@RequestBody String spaceName) throws UserNotFoundException {
        var createdSpace =
                spaceService.createSaveAndGetSpace(spaceName, SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok().body(createdSpace);
    }

    @PostMapping("/join")
    public ResponseEntity<SpaceInfoDto> joinSpace(@RequestBody String invitationHash) throws UserNotFoundException, SpaceNotFoundException, InactiveSpaceException {
        var spaceInfo = spaceService.addUserToSpace(invitationHash, SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok().body(spaceInfo);
    }

    @PostMapping(value = "/new-invitation-hash")
    public ResponseEntity<SpaceInfoDto> generateNewInvitationHash(@RequestBody Long spaceId) throws SpaceNotFoundException {
        var spaceInfo = spaceService.generateNewInvitationHash(spaceId);
        return ResponseEntity.ok().body(spaceInfo);
    }

    @PutMapping("/change-settings")
    public ResponseEntity<SpaceInfoDto> changeSpaceSettings(@RequestBody SpaceInfoDto propertiesToPut) throws SpaceNotFoundException, UserIsNotManagerOfSpaceException {
        var spaceInfo = spaceService.changeSpaceSettings(propertiesToPut, SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok().body(spaceInfo);
    }

    @GetMapping("/my")
    public ResponseEntity<List<SpaceInfoDto>> getMySpaces() throws UserNotFoundException {
        var requestingUserSpaces =
                userService.getUserSpaces(SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok().body(requestingUserSpaces);
    }

    @GetMapping("/info")
    public ResponseEntity<SpaceInfoDto> getSpaceInfo(@RequestParam() String spaceHash) throws UserIsNotMemberOfSpaceException{
        var requestingUserSpaces =
                spaceService.getSpaceInfo(spaceHash, SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok().body(requestingUserSpaces);
    }

    @GetMapping("/owned")
    public ResponseEntity<List<SpaceInfoDto>> getSpacesOwnedByMe() {
        var requestingUserSpaces =
                spaceService.getSpacesOwnedByUser(SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok().body(requestingUserSpaces);
    }

    @GetMapping("/managed")
    public ResponseEntity<List<SpaceInfoDto>> getSpacesManagedByMe() {
        var requestingUserSpaces =
                spaceService.getSpacesManagedByUser(SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok().body(requestingUserSpaces);
    }
}
