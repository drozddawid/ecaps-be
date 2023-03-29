package com.drozd.ecaps.model.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetPostCommentsDto {
    @NotNull
    private Long postId;
    @NotNull
    private Integer pageNumber;
    @NotNull
    private Integer pageSize;
}
