package com.drozd.ecaps.controller;

import com.drozd.ecaps.model.space.dto.SpaceInfoDto;
import com.drozd.ecaps.service.EcapsUserService;
import com.drozd.ecaps.service.SpaceService;
import com.drozd.ecaps.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController()
@RequestMapping("/spaces")
@RequiredArgsConstructor
public class SpaceController {
    private final SpaceService spaceService;
    private final EcapsUserService userService;

    @PostMapping()
    public ResponseEntity<SpaceInfoDto> createSpace(@RequestBody String spaceName) throws NoSuchElementException {
        var createdSpace =
                spaceService.createSaveAndGetSpace(spaceName, SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok().body(createdSpace);
    }

    @GetMapping()
    public ResponseEntity<List<SpaceInfoDto>> getMySpaces() throws NoSuchElementException {
        var requestingUserSpaces =
                userService.getUserSpaces(SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok().body(requestingUserSpaces);
    }

    @GetMapping("/owned")
    public ResponseEntity<List<SpaceInfoDto>> getSpacesOwnedByMe() throws NoSuchElementException {
        var requestingUserSpaces =
                spaceService.getSpacesOwnedByUser(SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok().body(requestingUserSpaces);
    }
}
