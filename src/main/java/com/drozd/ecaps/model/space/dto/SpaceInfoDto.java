package com.drozd.ecaps.model.space.dto;

import com.drozd.ecaps.model.space.Space;
import com.drozd.ecaps.model.tag.EcapsTag;

import java.time.LocalDate;
import java.util.List;

public record SpaceInfoDto(
        Long id,
        String name,
        boolean isActive,
        LocalDate createdOn,
        boolean hasGoogleDriveConfigured,
        String googleDriveApiKey,
        String invitationHash,
        String spaceHash,
        List<EcapsTag> allowedTags
) {
    public SpaceInfoDto(Space space) {
        this(
                space.getId(),
                space.getName(),
                space.isActive(),
                space.getCreatedOn(),
                space.isGoogleDriveConfigured(),
                space.getGoogleDriveApiKey(),
                space.getInvitationHash(),
                space.getSpaceHash(),
                space.getAllowedTags().stream().toList());
    }
}
