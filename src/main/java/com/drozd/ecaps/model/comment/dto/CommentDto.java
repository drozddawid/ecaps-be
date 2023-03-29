package com.drozd.ecaps.model.comment.dto;

import com.drozd.ecaps.model.comment.Comment;
import com.drozd.ecaps.model.user.dto.EcapsUserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentDto {
    private Long id;
    private LocalDateTime createdOn;
    private EcapsUserDto author;
    private String content;
//    private List<GoogleAttachmentDto> googleAttachments;

    public CommentDto(Comment comment) {
        this(comment.getId(),
                comment.getCreatedOn(),
                new EcapsUserDto(comment.getAuthor()),
                comment.getContent()
//                comment.getGoogleAttachmentsDto()
                );
    }
}
