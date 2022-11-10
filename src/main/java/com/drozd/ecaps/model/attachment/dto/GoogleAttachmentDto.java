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
    private Long id;
    private String path;
    private String fileName;

    public GoogleAttachmentDto(GoogleAttachment attachment) {
        this(attachment.getId(),
                attachment.getPath(),
                attachment.getPath().substring(attachment.getPath().lastIndexOf("/")));
    }
}
