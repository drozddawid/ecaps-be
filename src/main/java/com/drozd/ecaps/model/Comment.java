package com.drozd.ecaps.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;


@Entity
@Data
@Table(indexes = @Index(name = "post_index", columnList = "post_id"))
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private EcapsUser author;
    @ManyToOne
    private Post post;
    @Column(length = 65535, columnDefinition = "TEXT")
    private String content;
    @OneToMany()
    private List<Attachment> attachments;
}
