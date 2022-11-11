package com.drozd.ecaps.model.comment;

import com.drozd.ecaps.model.attachment.GoogleAttachment;
import com.drozd.ecaps.model.attachment.dto.GoogleAttachmentDto;
import com.drozd.ecaps.model.post.Post;
import com.drozd.ecaps.model.user.EcapsUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<GoogleAttachment> googleAttachments;

    public List<GoogleAttachmentDto> getGoogleAttachmentsDto() {
        return this.getGoogleAttachments().stream()
                .map(GoogleAttachmentDto::new)
                .toList();
    }
}
