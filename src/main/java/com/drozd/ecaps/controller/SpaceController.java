package com.drozd.ecaps.controller;

import com.drozd.ecaps.model.Space;
import com.drozd.ecaps.service.SpaceService;
import com.drozd.ecaps.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController()
@RequestMapping("/spaces")
@RequiredArgsConstructor
public class SpaceController {
    private final SpaceService spaceService;

    @PostMapping()
    public ResponseEntity<Space> createSpace(@RequestBody String spaceName) throws NoSuchElementException {
        var createdSpace =
                spaceService.createSaveAndGetSpace(spaceName, SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok().body(createdSpace);
    }


}
