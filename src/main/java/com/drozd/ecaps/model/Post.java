package com.drozd.ecaps.model;

import com.drozd.ecaps.model.space.Space;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    private LocalDate createdOn;

    @ManyToOne()
    @JoinColumn(name = "space_id")
    private Space space;
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private EcapsUser author;
    @ManyToMany
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            indexes = {@Index(name = "tag_id_index", columnList = "tag_id")}
    )
    @ToString.Exclude
    private Set<EcapsTag> tags = new HashSet<>();
    @OneToMany()
    @JoinColumn(name = "comment_id")
    @ToString.Exclude
    private Set<Comment> comments = new HashSet<>();



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
}
