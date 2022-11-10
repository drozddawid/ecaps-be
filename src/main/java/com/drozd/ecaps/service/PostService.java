package com.drozd.ecaps.service;

import com.drozd.ecaps.exception.badargument.DisallowedTagsException;
import com.drozd.ecaps.exception.badargument.SpaceNotFoundException;
import com.drozd.ecaps.exception.badargument.UserNotFoundException;
import com.drozd.ecaps.model.post.Post;
import com.drozd.ecaps.model.post.dto.CreatePostDto;
import com.drozd.ecaps.model.post.dto.GetSpacesPostsDto;
import com.drozd.ecaps.model.post.dto.PostDto;
import com.drozd.ecaps.model.space.Space;
import com.drozd.ecaps.model.tag.EcapsTag;
import com.drozd.ecaps.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final EcapsUserService userService;
    private final SpaceService spaceService;

    public PostDto addPost(CreatePostDto postToCreate, String authorEmail)
            throws UserNotFoundException, SpaceNotFoundException, DisallowedTagsException {
        var author = userService.getUser(authorEmail);
        var space = spaceService.getSpaceById(postToCreate.getSpaceId());

        final Set<EcapsTag> postTags = postToCreate.getTags();
        final Set<EcapsTag> allowedPostTags =
                space.getAllowedTags().stream()
                        .filter(postTags::contains)
                        .collect(Collectors.toSet());

        if (allowedPostTags.size() != postTags.size()) {
            final String disallowedTagNames =
                    postTags.stream()
                            .filter(allowedPostTags::contains)
                            .map(EcapsTag::getName)
                            .collect(Collectors.joining(" | "));
            throw new DisallowedTagsException("These tags aren't allowed " + disallowedTagNames);
        }
        var createdPost = new Post()
                .setContent(postToCreate.getContent())
                .setAuthor(author)
                .setTags(allowedPostTags)
                .setSpace(space)
                .setCreatedOn(LocalDateTime.now());

        return new PostDto(postRepository.save(createdPost));
    }

    public List<PostDto> getSpacesPosts(GetSpacesPostsDto spacesPosts, String askingUserEmail) throws SpaceNotFoundException {
        final Optional<Space> space = spaceService.getUserSpaces(askingUserEmail).stream()
                .filter(s -> Objects.equals(s.getId(), spacesPosts.getSpaceId()))
                .findFirst();

        if(space.isEmpty())
            throw new SpaceNotFoundException("User is not member of space with id " + spacesPosts.getSpaceId());

        final PageRequest page = PageRequest.of(spacesPosts.getPageNumber(), spacesPosts.getPageSize(),
                Sort.by("createdOn").descending());

        return postRepository.findBySpace(space.get(), page).stream()
                .map(PostDto::new)
                .toList();
    }

}
