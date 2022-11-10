package com.drozd.ecaps.repository;

import com.drozd.ecaps.model.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}