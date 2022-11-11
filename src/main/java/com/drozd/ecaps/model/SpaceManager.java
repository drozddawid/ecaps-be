package com.drozd.ecaps.model;

import com.drozd.ecaps.model.space.Space;
import com.drozd.ecaps.model.user.EcapsUser;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class SpaceManager {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpaceManagerRole role;
    @ManyToOne()
    @JoinColumn(name = "space_id")
    private Space space;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private EcapsUser user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SpaceManager that = (SpaceManager) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
