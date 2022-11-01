package com.drozd.ecaps.repository;

import com.drozd.ecaps.model.SpaceManagerRole;
import com.drozd.ecaps.model.space.QSpace;
import com.drozd.ecaps.model.space.Space;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long>,
        QuerydslPredicateExecutor<Space> {
    boolean existsByInvitationHash(String invitationHash);

    boolean existsBySpaceHash(String spaceHash);

    default List<Space> getSpacesOwnedByUser(String email) {
        var list = new ArrayList<Space>();
        findAll(isSpaceManager(email).and(isSpaceOwner())).forEach(list::add);
        return list;
    }

    default BooleanExpression isSpaceManager(String email) {
        return QSpace.space.spaceManagers.any().user.email.eq(email);
    }

    default BooleanExpression isSpaceOwner() {
        return QSpace.space.spaceManagers.any().role.eq(SpaceManagerRole.OWNER);
    }
}