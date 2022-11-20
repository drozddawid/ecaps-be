package com.drozd.ecaps.service;

import com.drozd.ecaps.configuration.SpaceGoogleAuthorizationCodeDto;
import com.drozd.ecaps.exception.BadArgumentException;
import com.drozd.ecaps.exception.GoogleFileUploadException;
import com.drozd.ecaps.exception.badargument.*;
import com.drozd.ecaps.model.SpaceManagerRole;
import com.drozd.ecaps.model.attachment.GoogleAttachment;
import com.drozd.ecaps.model.comment.Comment;
import com.drozd.ecaps.model.comment.dto.CommentDto;
import com.drozd.ecaps.model.comment.dto.GetPostCommentsDto;
import com.drozd.ecaps.model.comment.dto.NewCommentDto;
import com.drozd.ecaps.model.post.Post;
import com.drozd.ecaps.model.post.dto.CreatePostDto;
import com.drozd.ecaps.model.post.dto.GetSpacesPostsDto;
import com.drozd.ecaps.model.post.dto.PostDto;
import com.drozd.ecaps.model.space.Space;
import com.drozd.ecaps.model.space.dto.SpaceInfoDto;
import com.drozd.ecaps.model.tag.EcapsTag;
import com.drozd.ecaps.model.user.EcapsUser;
import com.drozd.ecaps.repository.CommentRepository;
import com.drozd.ecaps.repository.PostRepository;
import com.drozd.ecaps.repository.SpaceRepository;
import com.drozd.ecaps.utils.HashGenerationUtils;
import com.drozd.ecaps.utils.StringUtils;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.model.File;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.drozd.ecaps.utils.StringUtils.toEcapsSpaceFolderName;

@Service
@RequiredArgsConstructor
public class SpaceService {
    private final Logger log = LoggerFactory.getLogger(SpaceService.class);
    private final EcapsUserService userService;
    private final SpaceRepository spaceRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final GoogleApiService googleApiService;
    private final TagService tagService;

    public SpaceInfoDto createSaveAndGetSpace(String spaceName, String ownerEmail) throws UserNotFoundException {
        var user = userService.getUser(ownerEmail);
        var space = createSaveAndGetSpace(spaceName, user);
        return new SpaceInfoDto(space);
    }

    public SpaceInfoDto getSpaceInfo(String spaceHash, String askingUserEmail) throws UserIsNotMemberOfSpaceException {
        return new SpaceInfoDto(getUserSpaceOrThrowIfUserIsNotMember(spaceHash, askingUserEmail));
    }

    public Space getUserSpaceOrThrowIfUserIsNotMember(String spaceHash, String askingUserEmail) throws UserIsNotMemberOfSpaceException {
        return getUserSpaces(askingUserEmail).stream()
                .filter(s -> s.getSpaceHash().equals(spaceHash))
                .findFirst()
                .orElseThrow(() -> new UserIsNotMemberOfSpaceException("User " + askingUserEmail + " is not member of space with hash " + spaceHash));
    }

    public List<SpaceInfoDto> getSpacesOwnedByUser(String ownerEmail) {
        return spaceRepository.getSpacesOwnedByUser(ownerEmail).stream().map(SpaceInfoDto::new).toList();
    }

    public List<SpaceInfoDto> getSpacesManagedByUser(String ownerEmail) {
        return spaceRepository.getSpacesManagedByUser(ownerEmail).stream().map(SpaceInfoDto::new).toList();
    }


    public List<Space> getUserSpaces(String ownerEmail) {
        return spaceRepository.getSpacesWhereUserIsMember(ownerEmail);
    }

    public SpaceInfoDto addUserToSpace(String invitationHash, String userEmail) throws SpaceNotFoundException, UserNotFoundException, InactiveSpaceException {
        var user = userService.getUser(userEmail);
        var space = checkIfSpaceIsActiveByInvitationHashAndGet(invitationHash);
        if (!user.getSpaces().contains(space)) {
            space.addUser(user);
            spaceRepository.save(space);
        }
        return new SpaceInfoDto(space);
    }

    public SpaceInfoDto generateNewInvitationHash(Long spaceId) throws SpaceNotFoundException {
        var space = getSpaceById(spaceId);
        space.setInvitationHash(getUniqueInvitationHash());
        spaceRepository.save(space);
        return new SpaceInfoDto(space);
    }

    public Space getSpaceByInvitationHash(String invitationHash) throws SpaceNotFoundException {
        return spaceRepository.findByInvitationHash(invitationHash)
                .orElseThrow(SpaceNotFoundException::new);
    }

    public Space getSpaceByHash(String spaceHash) throws SpaceNotFoundException {
        return spaceRepository.findBySpaceHash(spaceHash)
                .orElseThrow(SpaceNotFoundException::new);
    }

    public Space getSpaceById(Long spaceId) throws SpaceNotFoundException {
        return spaceRepository.findById(spaceId)
                .orElseThrow(SpaceNotFoundException::new);
    }

    public Space checkIfSpaceIsActiveByIdAndGet(Long spaceId) throws SpaceNotFoundException, InactiveSpaceException {
        return checkIfSpaceIsActive(getSpaceById(spaceId));
    }

    public Space checkIfSpaceIsActiveByInvitationHashAndGet(String invitationHash) throws SpaceNotFoundException, InactiveSpaceException {
        return checkIfSpaceIsActive(getSpaceByInvitationHash(invitationHash));
    }

    public Space checkIfSpaceIsActiveByHashAndGet(String spaceHash) throws SpaceNotFoundException, InactiveSpaceException {
        return checkIfSpaceIsActive(getSpaceByHash(spaceHash));
    }

    public Space checkIfSpaceIsActive(Space space) throws InactiveSpaceException {
        if (!space.isActive())
            throw new InactiveSpaceException("Space " + space.getName() + " is inactive.");
        return space;
    }

    private Space createSaveAndGetSpace(String spaceName, EcapsUser owner) {
        Space space = new Space(spaceName);
        space.setSpaceHash(getUniqueSpaceHash());
        space.setInvitationHash(getUniqueInvitationHash());
        space.addSpaceManager(owner, SpaceManagerRole.OWNER);
        space.addUser(owner);
        spaceRepository.save(space);
        log.debug("Created new space. {}", space);
        return space;
    }

    private String getUniqueInvitationHash() {
        return HashGenerationUtils.getUniqueHash(spaceRepository::existsByInvitationHash, 60);
    }

    private String getUniqueSpaceHash() {
        return HashGenerationUtils.getUniqueHash(spaceRepository::existsBySpaceHash, 30);
    }

    public SpaceInfoDto changeSpaceSettings(SpaceInfoDto propertiesToPut, String userEmail) throws SpaceNotFoundException, UserIsNotManagerOfSpaceException {
        if (propertiesToPut.id() == null) {
            throw new SpaceNotFoundException("Space with id \"null\" doesn't exist, please pass some existing space id.");
        }
        final var space = getSpaceById(propertiesToPut.id());
        var isUserSpaceManager =
                getSpacesManagedByUser(userEmail).stream()
                        .anyMatch(s -> Objects.equals(s.id(), space.getId()));

        if (!isUserSpaceManager) {
            throw new UserIsNotManagerOfSpaceException("User is not manager of space.");
        }

        var newTags = propertiesToPut.allowedTags();
        if (newTags != null && !newTags.isEmpty()) {
            var tagsToSet = tagService.getOrCreateEcapsTagsSet(newTags.stream().map(EcapsTag::getName).toList());
            assignNewTagsToSpace(tagsToSet, space);
        }
        var newName = propertiesToPut.name();
        if (newName != null && !newName.isBlank()) {
            space.setName(newName);
        }
        var newIsActive = propertiesToPut.isActive();
        if (newIsActive != null) {
            space.setActive(newIsActive);
        }

        var updatedSpace = spaceRepository.save(space);
        return new SpaceInfoDto(updatedSpace);
    }

    public void assignNewTagsToSpace(Set<EcapsTag> tags, Space space) {
        var tagsToRemove =
                space.getAllowedTags().stream()
                        .filter(at -> tags.stream().noneMatch(t -> t.getName().equals(at.getName())));

        Set<EcapsTag> newTags = new HashSet<>(tags);
        newTags.addAll(
                tagsToRemove
                        .filter(t -> doesAnyPostWithSpaceAndTagNameExist(space, t.getName()))
                        .collect(Collectors.toSet()));

        space.setAllowedTags(newTags);
    }

    public PostDto addPost(CreatePostDto postToCreate, String authorEmail)
            throws UserNotFoundException, SpaceNotFoundException, DisallowedTagsException, InactiveSpaceException, UserIsNotMemberOfSpaceException {
        var author = userService.getUser(authorEmail);
        var space = checkIfSpaceIsActiveByIdAndGet(postToCreate.getSpaceId());
        if (!author.getSpaces().contains(space)) {
            throw new UserIsNotMemberOfSpaceException("User is not member of space. Post not added.");
        }

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

    public Post getPostById(Long postId) throws PostNotFoundException {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " not found."));
    }

    public PostDto addPostAttachment(Long postId, String authorEmail, MultipartFile file) throws PostNotFoundException, UserIsNotPostAuthorException, SpaceHasNoGoogleDriveAccountConfiguredException, IOException, GoogleFileUploadException {
        var post = getPostById(postId);
        var space = post.getSpace();
        if (!post.getAuthor().getEmail().equals(authorEmail))
            throw new UserIsNotPostAuthorException("User " + authorEmail + " is not author of the post and can't add attachment to it.");

        Credential credential = getSpaceCredential(space);

        var uploadedFile = googleApiService.uploadFileToFolder(StringUtils.toEcapsSpaceFolderName(space.getName()), file.getOriginalFilename(), file.getContentType(), file.getInputStream(), credential);
        if (uploadedFile == null)
            throw new GoogleFileUploadException("Error occurred when uploading file " + file.getOriginalFilename() + " to Google Drive.");

        post.getGoogleAttachments().add(new GoogleAttachment().setGoogleDriveId(uploadedFile.getId()).setFileName(uploadedFile.getName()));
        post = postRepository.save(post);

        return new PostDto(post);
    }

    private Credential getSpaceCredential(Space space) throws SpaceHasNoGoogleDriveAccountConfiguredException, IOException {
        if (!space.isGoogleDriveConfigured())
            throw new SpaceHasNoGoogleDriveAccountConfiguredException("Space " + space.getName() + " has no google drive account configured and you can't upload or download attachments.");

        var credential = googleApiService.getCredential(space.getGoogleDriveAccountEmail());

        if (credential == null) {
            space.setGoogleDriveConfigured(false);
            spaceRepository.save(space);
            throw new SpaceHasNoGoogleDriveAccountConfiguredException("Space " + space.getName() + " has no google drive account configured and you can't upload or download attachments.");
        }
        return credential;
    }

    public List<PostDto> getSpacesPosts(GetSpacesPostsDto spacesPosts, String askingUserEmail) throws SpaceNotFoundException, InactiveSpaceException {
        final Optional<Space> space = getUserSpaces(askingUserEmail).stream()
                .filter(s -> Objects.equals(s.getId(), spacesPosts.getSpaceId()))
                .findFirst();

        if (space.isEmpty())
            throw new SpaceNotFoundException("User is not member of space with id " + spacesPosts.getSpaceId());

        checkIfSpaceIsActive(space.get());

        final PageRequest page = PageRequest.of(spacesPosts.getPageNumber(), spacesPosts.getPageSize(),
                Sort.by("createdOn").descending());

        return postRepository.findBySpace(space.get(), page).stream()
                .map(PostDto::new)
                .toList();
    }

    public CommentDto addComment(NewCommentDto comment, String authorEmail) throws UserNotFoundException, PostNotFoundException, UserIsNotManagerOfSpaceException {
        final EcapsUser user = userService.getUser(authorEmail);
        final Post post = getPostIfUserIsMemberOfSpace(comment.getPostId(), user);
        final Comment commentToAdd =
                new Comment()
                        .setId(null)
                        .setPost(post)
                        .setAuthor(user)
                        .setCreatedOn(LocalDateTime.now())
                        .setContent(comment.getContent());

        post.addComment(commentToAdd);
        postRepository.save(post);
        return new CommentDto(commentToAdd);
    }

    public List<CommentDto> getPostComments(GetPostCommentsDto postComments, String askingUserEmail) throws PostNotFoundException, UserNotFoundException, UserIsNotManagerOfSpaceException {
        final Post post = getPostIfUserIsMemberOfSpace(postComments.getPostId(), askingUserEmail);

        final PageRequest page = PageRequest.of(postComments.getPageNumber(), postComments.getPageSize(),
                Sort.by("createdOn").descending());
        return commentRepository.findByPost(post, page).stream()
                .map(CommentDto::new)
                .toList();
    }

    private Post getPostIfUserIsMemberOfSpace(Long postId, String askingUserEmail) throws PostNotFoundException, UserNotFoundException, UserIsNotManagerOfSpaceException {
        return getPostIfUserIsMemberOfSpace(postId, userService.getUser(askingUserEmail));
    }

    private Post getPostIfUserIsMemberOfSpace(Long postId, EcapsUser user) throws PostNotFoundException, UserIsNotManagerOfSpaceException {
        final Post post = getPostById(postId);
        final Space space = post.getSpace();
        final var isUserSpaceMember = user.getSpaces().contains(space);
        if (!isUserSpaceMember)
            throw new UserIsNotManagerOfSpaceException("User " + user.getEmail() + " is not member of space " + space.getName());
        return post;
    }


    public boolean doesAnyPostWithSpaceAndTagNameExist(Space space, String tagName) {
        return postRepository.existsBySpaceAndTags_Name(space, tagName);
    }

    public void authorizeAndSetSpaceDriveAccount(final SpaceGoogleAuthorizationCodeDto authorizationCode, final String requestOrigin) throws BadArgumentException, GeneralSecurityException, IOException {
        final Space space = checkIfSpaceIsActiveByIdAndGet(authorizationCode.spaceId());

        var driveAccountEmail = googleApiService.authorizeGoogleApiAuthorizationCodeAndGetDriveAccountEmail(authorizationCode, requestOrigin);

        Credential credential = googleApiService.getCredential(driveAccountEmail);

        googleApiService.createFolderOrGetIfAlreadyExists(credential, toEcapsSpaceFolderName(space.getName()));

        if (space.isGoogleDriveConfigured() && !space.getGoogleDriveAccountEmail().equals(driveAccountEmail))
            throw new SpaceHasAnotherGoogleDriveAccountConfiguredException("Space has another account already configured (" +
                    space.getGoogleDriveAccountEmail() + "). You can reauthorize space only with this account.");

        space.setGoogleDriveConfigured(true)
                .setGoogleDriveAccountEmail(driveAccountEmail);

        spaceRepository.save(space);
    }

    public File downloadPostAttachment(String askingUserEmail, Long postId, String fileId, OutputStream outputStream)
            throws UserNotFoundException, PostNotFoundException, UserIsNotManagerOfSpaceException, IOException, SpaceHasNoGoogleDriveAccountConfiguredException {

        final var space = getPostIfUserIsMemberOfSpace(postId, askingUserEmail).getSpace();
        final Credential spaceCredential = getSpaceCredential(space);
        return googleApiService.downloadFileToOutputStreamAndGetMetadata(fileId, spaceCredential, outputStream);
    }
}
