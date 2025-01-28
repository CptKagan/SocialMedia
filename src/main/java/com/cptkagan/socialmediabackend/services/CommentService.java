package com.cptkagan.socialmediabackend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.cptkagan.socialmediabackend.DTOs.CommentRequest;
import com.cptkagan.socialmediabackend.models.Account;
import com.cptkagan.socialmediabackend.models.Comment;
import com.cptkagan.socialmediabackend.models.Likes;
import com.cptkagan.socialmediabackend.models.Post;
import com.cptkagan.socialmediabackend.repositories.AccountRepository;
import com.cptkagan.socialmediabackend.repositories.CommentRepository;
import com.cptkagan.socialmediabackend.repositories.LikeRepository;
import com.cptkagan.socialmediabackend.repositories.PostRepository;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LikeRepository likeRepository;

    public ResponseEntity<?> save(CommentRequest commentRequest, Authentication authentication, Long id) {
        Comment comment = new Comment(commentRequest);
        Optional<Post> posts = postRepository.findById(id);
        if(!posts.isPresent()){
            return ResponseEntity.badRequest().body("Post not found!");
        }
        String userName = authentication.getName();
        Optional<Account> accounts = accountRepository.findByUserName(userName);
        if(!accounts.isPresent()){
            return ResponseEntity.badRequest().body("Account not found / Invalid Token1");
        }
        Account account = accounts.get();
        Post post = posts.get();
        comment.setAccount(account);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
        return ResponseEntity.ok("Comment Added Successfully!");
    }

    public ResponseEntity<?> getCommentsOfPost(Long id) {
        List<Comment> comments = commentRepository.findByPostId(id);
        return ResponseEntity.ok(comments);
    }

    public ResponseEntity<?> editComment(CommentRequest commentRequest, Long id, Authentication authentication) {
        Optional<Comment> comments = commentRepository.findById(id);
        if(!comments.isPresent()){
            return ResponseEntity.badRequest().body("Comment not found!");
        }
        Comment comment = comments.get();
        if(!comment.getAccount().getUserName().equals(authentication.getName())){
            return ResponseEntity.badRequest().body("You are not the owner of the comment! You can not edit this comment!");
        }
        comment.setContent(commentRequest.getContent());
        comment.setEditedAt(LocalDateTime.now());

        commentRepository.save(comment);
        return ResponseEntity.ok("Comment Edited Successfully!");
    }

    public ResponseEntity<?> likeComment(Long id, Authentication authentication) {
        Optional<Account> accounts = accountRepository.findByUserName(authentication.getName());
        if(!accounts.isPresent()){
            return ResponseEntity.badRequest().body("Invalid Token / Account not found!");
        }
        Optional<Comment> comments = commentRepository.findById(id);
        if(!comments.isPresent()){
            return ResponseEntity.badRequest().body("Commnet not found!");
        }
        Optional<Likes> likes = likeRepository.findByAccountIdAndCommentId(accounts.get().getId(), id);
        if(likes.isPresent()){
            likeRepository.delete(likes.get());
            return ResponseEntity.ok("Comment unliked successfully!");
        }
        Likes like = new Likes();
        like.setAccount(accounts.get());
        like.setComment(comments.get());
        likeRepository.save(like);
        return ResponseEntity.ok("Comment liked successfully");
    }
}
