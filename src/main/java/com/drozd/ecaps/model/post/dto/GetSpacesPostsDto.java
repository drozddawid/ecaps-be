package com.drozd.ecaps.model.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetSpacesPostsDto {
    @NotNull
    private Long spaceId;
    @NotNull
    private Integer pageNumber;
    @NotNull
    private Integer pageSize;
}
