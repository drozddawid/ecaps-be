package com.drozd.ecaps.controller;

import com.drozd.ecaps.exception.GoogleFileUploadException;
import com.drozd.ecaps.exception.badargument.*;
import com.drozd.ecaps.model.comment.dto.CommentDto;
import com.drozd.ecaps.model.comment.dto.GetPostCommentsDto;
import com.drozd.ecaps.model.comment.dto.NewCommentDto;
import com.drozd.ecaps.model.post.dto.CreatePostDto;
import com.drozd.ecaps.model.post.dto.GetSpacesPostsDto;
import com.drozd.ecaps.model.post.dto.PostDto;
import com.drozd.ecaps.service.GoogleApiService;
import com.drozd.ecaps.service.SpaceService;
import com.drozd.ecaps.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@RestController()
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final SpaceService spaceService;
    private final GoogleApiService googleApiService;
    private final Logger log = LoggerFactory.getLogger(PostController.class);

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

    @PostMapping("/upload-file")
    public ResponseEntity<PostDto> uploadFile(@RequestParam Long postId, @RequestParam MultipartFile[] files) throws UserIsNotPostAuthorException, PostNotFoundException, GoogleFileUploadException, IOException, SpaceHasNoGoogleDriveAccountConfiguredException {
        PostDto post = null;
        for(MultipartFile file : files){
            post = spaceService.addPostAttachment(postId, SecurityContextUtils.getCurrentUserEmail(), file);
        }
        return ResponseEntity.ok(post);
    }

    @GetMapping("/get-file")
    public void downloadFile(@RequestParam String fileId, @RequestParam Long postId, HttpServletResponse response) throws IOException, UserNotFoundException, PostNotFoundException, UserIsNotManagerOfSpaceException, SpaceHasNoGoogleDriveAccountConfiguredException {
        var file = spaceService.downloadPostAttachment(SecurityContextUtils.getCurrentUserEmail(), postId, fileId, response.getOutputStream());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getOriginalFilename() + "\"");
        response.setContentType(file.getMimeType());
        response.setStatus(SC_OK);
        response.flushBuffer();
    }

}
