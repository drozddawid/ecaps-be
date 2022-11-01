package com.drozd.ecaps.model;

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
public class EcapsTag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EcapsTag ecapsTag = (EcapsTag) o;
        return id != null && Objects.equals(id, ecapsTag.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
