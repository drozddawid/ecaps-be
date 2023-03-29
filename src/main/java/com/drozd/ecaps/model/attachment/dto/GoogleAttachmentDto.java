package com.drozd.ecaps.model.attachment.dto;


import com.drozd.ecaps.model.attachment.GoogleAttachment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GoogleAttachmentDto {
    private String googleDriveId;
    private String fileName;

    public GoogleAttachmentDto(GoogleAttachment attachment) {
        this(attachment.getGoogleDriveId(),
                attachment.getFileName());
    }
}
