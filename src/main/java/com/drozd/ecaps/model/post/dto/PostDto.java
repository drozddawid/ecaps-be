package com.drozd.ecaps.model.post.dto;

import com.drozd.ecaps.model.attachment.dto.GoogleAttachmentDto;
import com.drozd.ecaps.model.post.Post;
import com.drozd.ecaps.model.tag.EcapsTag;
import com.drozd.ecaps.model.user.dto.EcapsUserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostDto {
    private Long id;
    private String content;
    private LocalDateTime createdOn;
    private EcapsUserDto author;
    private Set<EcapsTag> tags;
    private Set<GoogleAttachmentDto> googleAttachments = new HashSet<>();


    public PostDto(Post post) {
        this(post.getId(),
                post.getContent(),
                post.getCreatedOn(),
                new EcapsUserDto(post.getAuthor()),
                post.getTags(),
                post.getGoogleAttachmentsDto());
    }
}
