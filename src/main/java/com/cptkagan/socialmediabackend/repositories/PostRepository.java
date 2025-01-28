package com.cptkagan.socialmediabackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cptkagan.socialmediabackend.models.Post;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByAccountId(Long accountId);
    Optional<Post> findById(Long id);
}
