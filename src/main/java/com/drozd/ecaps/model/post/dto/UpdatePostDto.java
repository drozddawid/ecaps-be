package com.drozd.ecaps.model.post.dto;

import com.drozd.ecaps.model.attachment.GoogleAttachment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdatePostDto extends CreatePostDto{
    @NotNull
    @Min(0)
    private Long postId;
    private List<GoogleAttachment> googleAttachments;
}
