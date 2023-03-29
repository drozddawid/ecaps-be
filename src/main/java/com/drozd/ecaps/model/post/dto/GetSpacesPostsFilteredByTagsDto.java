package com.drozd.ecaps.model.post.dto;

import com.drozd.ecaps.model.tag.EcapsTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetSpacesPostsFilteredByTagsDto extends GetSpacesPostsDto{
    private List<EcapsTag> tags;
}
