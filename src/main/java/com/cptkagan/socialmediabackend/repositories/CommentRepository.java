package com.cptkagan.socialmediabackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cptkagan.socialmediabackend.models.Comment;

public interface CommentRepository extends JpaRepository<Comment,Long> {
        List<Comment> findByAccountId(Long accountId);
        List<Comment> findByPostId(Long postId);
        Optional<Comment> findById(Long id);
        
        @Query("SELECT c FROM Comment c WHERE c.post.id = :postId ORDER BY c.createdAt ASC")
        List<Comment> findByPostIdOrderByCreatedAtAsc(@Param("postId") Long postId);
}
