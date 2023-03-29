package com.drozd.ecaps.repository;

import com.drozd.ecaps.model.comment.Comment;
import com.drozd.ecaps.model.post.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, PagingAndSortingRepository<Comment, Long> {
    List<Comment> findByPost(Post post, Pageable pageable);

}