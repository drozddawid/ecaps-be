package com.drozd.ecaps.repository;

import com.drozd.ecaps.model.user.dto.EcapsUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EcapsUserRepository extends JpaRepository<EcapsUser, Long> {
    Optional<EcapsUser> findByEmail(String email);
}