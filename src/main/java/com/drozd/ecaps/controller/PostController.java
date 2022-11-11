package com.drozd.ecaps.controller;

import com.drozd.ecaps.exception.badargument.DisallowedTagsException;
import com.drozd.ecaps.exception.badargument.InactiveSpaceException;
import com.drozd.ecaps.exception.badargument.SpaceNotFoundException;
import com.drozd.ecaps.exception.badargument.UserNotFoundException;
import com.drozd.ecaps.model.post.dto.CreatePostDto;
import com.drozd.ecaps.model.post.dto.GetSpacesPostsDto;
import com.drozd.ecaps.model.post.dto.PostDto;
import com.drozd.ecaps.service.PostService;
import com.drozd.ecaps.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping()
    public ResponseEntity<PostDto> createPost(@RequestBody CreatePostDto postToCreate) throws UserNotFoundException, DisallowedTagsException, SpaceNotFoundException, InactiveSpaceException {
        var createdPost =
                postService.addPost(postToCreate, SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok().body(createdPost);
    }

    @PostMapping("/space")
    public ResponseEntity<List<PostDto>> getSpacesPosts(@RequestBody GetSpacesPostsDto getSpacesPostsDto) throws SpaceNotFoundException, InactiveSpaceException {
        var spacesPosts = postService.getSpacesPosts(getSpacesPostsDto, SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok().body(spacesPosts);
    }
}
