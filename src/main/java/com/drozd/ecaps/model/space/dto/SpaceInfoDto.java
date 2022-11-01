package com.drozd.ecaps.model.space.dto;

import com.drozd.ecaps.model.space.Space;

import java.time.LocalDate;

public record SpaceInfoDto(
        Long id,
        String name,
        boolean isActive,
        LocalDate createdOn,
        boolean hasGoogleDriveConfigured,
        String googleDriveApiKey,
        String invitationHash,
        String spaceHash
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
                space.getSpaceHash());
    }
}
