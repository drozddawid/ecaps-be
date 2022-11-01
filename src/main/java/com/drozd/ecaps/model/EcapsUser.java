package com.drozd.ecaps.model;

import com.drozd.ecaps.model.space.Space;
import com.drozd.ecaps.utils.GoogleIdTokenPayloadUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class EcapsUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @Column(unique = true, nullable = false)
    private String email;

    private String pictureURL;

    private LocalDate createdOn;

    @ManyToMany(mappedBy = "users")
    @ToString.Exclude
    private Set<Space> spaces = new HashSet<>();
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private Set<SpaceManager> managedSpaces = new HashSet<>();
    @OneToMany(mappedBy = "author")
    @ToString.Exclude
    private Set<Post> posts = new HashSet<>();
    @OneToMany(mappedBy = "author")
    @ToString.Exclude
    private Set<Comment> comments = new HashSet<>();

    public EcapsUser(GoogleIdToken.Payload user) throws IllegalArgumentException {
        var payload = new GoogleIdTokenPayloadUtils(user);
        this.name = payload.getName();
        this.email =
                payload.getEmail()
                        .orElseThrow(() -> new IllegalArgumentException("User must have email assigned."));
        this.pictureURL = payload.getPictureUrl().orElse("");
        // TODO: 10/29/22 put default user picture link here, link must depend on env variable (different for local and prod)
        this.createdOn = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EcapsUser ecapsUser = (EcapsUser) o;
        return id != null && Objects.equals(id, ecapsUser.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}