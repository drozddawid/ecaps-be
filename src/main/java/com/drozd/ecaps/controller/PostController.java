package com.drozd.ecaps.controller;

import com.drozd.ecaps.exception.badargument.*;
import com.drozd.ecaps.model.comment.dto.CommentDto;
import com.drozd.ecaps.model.comment.dto.GetPostCommentsDto;
import com.drozd.ecaps.model.comment.dto.NewCommentDto;
import com.drozd.ecaps.model.post.dto.CreatePostDto;
import com.drozd.ecaps.model.post.dto.GetSpacesPostsDto;
import com.drozd.ecaps.model.post.dto.PostDto;
import com.drozd.ecaps.service.SpaceService;
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
    private final SpaceService spaceService;

    @PostMapping()
    public ResponseEntity<PostDto> createPost(@RequestBody CreatePostDto postToCreate) throws UserNotFoundException, DisallowedTagsException, SpaceNotFoundException, InactiveSpaceException, UserIsNotMemberOfSpaceException {
        var createdPost =
                spaceService.addPost(postToCreate, SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok().body(createdPost);
    }

    @PostMapping("/space")
    public ResponseEntity<List<PostDto>> getSpacesPosts(@RequestBody GetSpacesPostsDto getSpacesPostsDto) throws SpaceNotFoundException, InactiveSpaceException {
        var spacesPosts = spaceService.getSpacesPosts(getSpacesPostsDto, SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok().body(spacesPosts);
    }

    @PostMapping("/new-comment")
    public ResponseEntity<CommentDto> newComment(@RequestBody NewCommentDto newComment) throws UserNotFoundException, PostNotFoundException, UserIsNotManagerOfSpaceException {
        final var comment = spaceService.addComment(newComment, SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/comments")
    public ResponseEntity<List<CommentDto>> getPostComments(@RequestBody GetPostCommentsDto getPostComments) throws UserNotFoundException, PostNotFoundException, UserIsNotManagerOfSpaceException {
        final var comments = spaceService.getPostComments(getPostComments, SecurityContextUtils.getCurrentUserEmail());
        return ResponseEntity.ok(comments);
    }
}
