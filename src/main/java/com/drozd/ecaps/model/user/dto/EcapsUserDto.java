package com.drozd.ecaps.model.user.dto;

import com.drozd.ecaps.model.user.EcapsUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EcapsUserDto {
    private Long id;
    private String name;
    private String email;
    private String pictureURL;
    private LocalDate createdOn;

    public EcapsUserDto(EcapsUser user) {
        this(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPictureURL(),
                user.getCreatedOn());
    }
}
