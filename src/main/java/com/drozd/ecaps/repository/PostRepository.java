package com.drozd.ecaps.repository;

import com.drozd.ecaps.model.post.Post;
import com.drozd.ecaps.model.post.QPost;
import com.drozd.ecaps.model.space.Space;
import com.drozd.ecaps.model.tag.EcapsTag;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public interface PostRepository extends JpaRepository<Post, Long>, PagingAndSortingRepository<Post, Long>,
        QuerydslPredicateExecutor<Post> {
    List<Post> findBySpace(Space space, Pageable pageable);

    default List<Post> findBySpaceAndTags(Space space, List<EcapsTag> tags, Integer pageNumber, Integer pageSize, Sort sort){
        var list = new ArrayList<Post>();
        findBySpaceAndTags(space, tags, pageNumber, pageSize, sort);
        return list;
    }

    default Stream<Post> findBySpaceAndTagsAsStream(Space space, List<EcapsTag> tags, Integer pageNumber, Integer pageSize, Sort sort){
        return findAll(hasSpace(space).and(containsAtLeastOneTag(tags)), PageRequest.of(pageNumber, pageSize, sort)).stream();
    }

    default BooleanExpression hasSpace(Space space){
        return QPost.post.space.eq(space);
    }

    default BooleanExpression containsAtLeastOneTag(List<EcapsTag> tags){
        return QPost.post.tags.any().in(tags);
    }

    boolean existsBySpaceAndTags_Name(Space space, String name);

}