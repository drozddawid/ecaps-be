package com.drozd.ecaps.model.attachment;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class GoogleAttachment {

    @Id
    private String googleDriveId;

    private String fileName;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoogleAttachment that = (GoogleAttachment) o;
        return googleDriveId.equals(that.googleDriveId) && Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
