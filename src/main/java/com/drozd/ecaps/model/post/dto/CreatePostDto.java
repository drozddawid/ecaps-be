package com.drozd.ecaps.model.post.dto;

import com.drozd.ecaps.model.tag.EcapsTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreatePostDto {
    @NotBlank
    private String content;
    @NotNull
    @Min(0)
    private Long spaceId;
    @NotEmpty
    private Set<EcapsTag> tags;
}
