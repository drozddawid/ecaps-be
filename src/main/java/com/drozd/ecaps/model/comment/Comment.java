package com.drozd.ecaps.model.comment;

import com.drozd.ecaps.model.post.Post;
import com.drozd.ecaps.model.user.EcapsUser;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(indexes = @Index(name = "post_index", columnList = "post_id"))
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private EcapsUser author;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @Column(length = 65535, columnDefinition = "TEXT")
    private String content;

//    @OneToMany(cascade = CascadeType.ALL)
//    @ToString.Exclude
//    private Set<GoogleAttachment> googleAttachments = new HashSet<>();
//
//    public List<GoogleAttachmentDto> getGoogleAttachmentsDto() {
//        return this.getGoogleAttachments().stream()
//                .map(GoogleAttachmentDto::new)
//                .toList();
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Comment comment = (Comment) o;
        return id != null && Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
