package com.drozd.ecaps.model.post;

import com.drozd.ecaps.model.attachment.GoogleAttachment;
import com.drozd.ecaps.model.attachment.dto.GoogleAttachmentDto;
import com.drozd.ecaps.model.comment.Comment;
import com.drozd.ecaps.model.space.Space;
import com.drozd.ecaps.model.tag.EcapsTag;
import com.drozd.ecaps.model.user.EcapsUser;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(indexes = {
        @Index(name = "space_createdon_index", columnList = "space_id, created_on")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 65535, columnDefinition = "TEXT")
    private String content;
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @ManyToOne()
    @JoinColumn(name = "space_id")
    private Space space;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private EcapsUser author;
    @ManyToMany
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"),
            indexes = {@Index(name = "tag_id_index", columnList = "tag_id")}
    )
    @ToString.Exclude
    private Set<EcapsTag> tags = new HashSet<>();
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<GoogleAttachment> googleAttachments = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Post post = (Post) o;
        return id != null && Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Set<GoogleAttachmentDto> getGoogleAttachmentsDto() {
        return this.getGoogleAttachments().stream()
                .map(GoogleAttachmentDto::new)
                .collect(Collectors.toSet());
    }

    public boolean addComment(Comment comment){
        comment.setPost(this);
        return comments.add(comment);
    }
}
