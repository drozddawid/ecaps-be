package com.drozd.ecaps.repository;

import com.drozd.ecaps.model.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {
    boolean existsByInvitationHash(String invitationHash);
    boolean existsBySpaceHash(String spaceHash);


}