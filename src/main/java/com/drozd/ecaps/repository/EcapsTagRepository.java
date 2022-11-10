package com.drozd.ecaps.repository;

import com.drozd.ecaps.model.tag.EcapsTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EcapsTagRepository extends JpaRepository<EcapsTag, Long> {
    Optional<EcapsTag> findByNameIgnoreCase(String name);
}