package com.cptkagan.socialmediabackend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cptkagan.socialmediabackend.DTOs.FollowersDTO;
import com.cptkagan.socialmediabackend.DTOs.PostDTO;
import com.cptkagan.socialmediabackend.DTOs.PostRequest;
import com.cptkagan.socialmediabackend.models.Account;
import com.cptkagan.socialmediabackend.models.Likes;
import com.cptkagan.socialmediabackend.models.Post;
import com.cptkagan.socialmediabackend.repositories.AccountRepository;
import com.cptkagan.socialmediabackend.repositories.CommentRepository;
import com.cptkagan.socialmediabackend.repositories.LikeRepository;
import com.cptkagan.socialmediabackend.repositories.PostRepository;

import org.springframework.security.core.Authentication;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    public ResponseEntity<?> save(PostRequest postRequest, Authentication authentication){
        Post post = new Post(postRequest);
        String userName = authentication.getName();
        Optional<Account> accounts = accountRepository.findByUserName(userName);
        if(!accounts.isPresent()){
            return ResponseEntity.badRequest().body("Account not found / Token Invalid!");
        }
        Account account = accounts.get();
        post.setAccount(account);
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
        return ResponseEntity.ok("Post Saved Successfully!");
    }

    public ResponseEntity<List<PostDTO>> getAllPosts(){
        List<Post> posts = postRepository.findAll();
        for(int i = 0; i<posts.size(); i++){
            posts.get(i).setComments(commentRepository.findByPostIdOrderByCreatedAtAsc(posts.get(i).getId()));
        }
        List<PostDTO> postDTOs = posts.stream().map(PostDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }

    public ResponseEntity<?> edit(PostRequest postRequest, Authentication authentication, Long id) {
        Optional<Post> posts = postRepository.findById(id);
        if(!posts.isPresent()){
            return ResponseEntity.badRequest().body("Post Not Found!");
        }
        Post post = posts.get();
        if(!post.getAccount().getUserName().equals(authentication.getName())){
            return ResponseEntity.badRequest().body("You are not the owner of this post! You can not edit this post!");
        }
        post.setContent(postRequest.getContent());
        post.setEditedAt(LocalDateTime.now());
        postRepository.save(post);
        return ResponseEntity.ok("Post Edited Successfully!");
    }

    public ResponseEntity<?> getSinglePost(Long id) {
        Optional<Post> posts = postRepository.findById(id);
        if(!posts.isPresent()){
            return ResponseEntity.badRequest().body("Post not found!");
        }
        posts.get().setComments(commentRepository.findByPostIdOrderByCreatedAtAsc(id));
        PostDTO post = new PostDTO(posts.get());
        return ResponseEntity.ok(post);
    }

    public ResponseEntity<?> postLike(Long id, Authentication authentication) {
        Optional<Account> accounts = accountRepository.findByUserName(authentication.getName());
        if(!accounts.isPresent()){
            return ResponseEntity.badRequest().body("Invalid Token!");
        }
        Optional<Post> posts = postRepository.findById(id);
        if(!posts.isPresent()){
            return ResponseEntity.badRequest().body("Post not found!");
        }
        Post post = posts.get();
        Account account = accounts.get();
        Optional<Likes> likes = likeRepository.findByAccountIdAndPostId(account.getId(), id);
        if(likes.isPresent()){
            likeRepository.delete(likes.get());
            return ResponseEntity.ok("Post unliked successfully!");
        }
        Likes like = new Likes();
        like.setAccount(account);
        like.setPost(post);
        likeRepository.save(like);
        return ResponseEntity.ok("Post liked successfully!");
    }

    public ResponseEntity<?> getLikes(Long id) {
        Optional<Post> posts = postRepository.findById(id);
        if(!posts.isPresent()){
            return ResponseEntity.badRequest().body("Post not found!");
        }
        Post post = posts.get();
        List<FollowersDTO> likedAccounts = post.getLikes().stream().map(FollowersDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(likedAccounts);
    }
}
