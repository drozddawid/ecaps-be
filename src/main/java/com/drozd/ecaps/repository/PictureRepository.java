package com.drozd.ecaps.repository;

import com.drozd.ecaps.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, Long> {
}