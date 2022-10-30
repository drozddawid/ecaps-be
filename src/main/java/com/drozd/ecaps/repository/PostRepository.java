package com.drozd.ecaps.repository;

import com.drozd.ecaps.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}