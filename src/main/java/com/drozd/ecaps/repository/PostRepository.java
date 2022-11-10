package com.drozd.ecaps.repository;

import com.drozd.ecaps.model.post.Post;
import com.drozd.ecaps.model.space.Space;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {
    List<Post> findBySpace(Space space, Pageable pageable);
}