package com.cptkagan.socialmediabackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.cptkagan.socialmediabackend.models.Likes;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {
    List<Likes> findByAccountId(Long accountId);
    List<Likes> findByPostId(Long postId);
    List<Likes> findByCommentId(Long commentId);
    Optional<Likes> findByAccountIdAndPostId(Long accountId, Long postId);
    Optional<Likes> findByAccountIdAndCommentId(Long accountId, Long commentId);

}
