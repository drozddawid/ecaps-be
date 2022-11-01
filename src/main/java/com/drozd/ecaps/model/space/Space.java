package com.drozd.ecaps.model.space;

import com.drozd.ecaps.model.*;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity()
@Table(
        indexes = {
                @Index(name = "spaceHash_index", columnList = "spaceHash"),
                @Index(name = "invitationHash_index", columnList = "invitationHash")
        }
)
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Space {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private boolean isActive = true;

    private LocalDate createdOn;

    private boolean googleDriveConfigured = false;

    private String googleDriveApiKey = null;

    private String invitationHash;

    private String spaceHash;
    @ManyToMany()
    @JoinTable(
            name = "space_user",
            joinColumns = @JoinColumn(name = "space_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Set<EcapsUser> users = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL})
    @ToString.Exclude
    private Set<SpaceManager> spaceManagers = new HashSet<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "space_tags",
            joinColumns = @JoinColumn(name = "space_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    @ToString.Exclude
    private Set<EcapsTag> allowedTags = new HashSet<>();

    @OneToMany(mappedBy = "space")
    @ToString.Exclude
    private Set<Post> posts = new HashSet<>();

    public Space(String name) {
        this.name = name;
        this.createdOn = LocalDate.now();
    }

    public void addSpaceManager(EcapsUser user, SpaceManagerRole role) {
        spaceManagers.add(
                new SpaceManager()
                        .setSpace(this)
                        .setUser(user)
                        .setRole(role));
    }

    public void addUser(EcapsUser ecapsUser) {
        users.add(ecapsUser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Space space = (Space) o;
        return id != null && Objects.equals(id, space.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}