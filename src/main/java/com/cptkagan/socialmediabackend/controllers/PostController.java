package com.cptkagan.socialmediabackend.controllers;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cptkagan.socialmediabackend.DTOs.PostRequest;
import com.cptkagan.socialmediabackend.services.PostService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/post/add")
    public ResponseEntity<?> addPost(@Valid @RequestBody PostRequest postRequest, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

        return postService.save(postRequest, authentication);
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<?> getSinglePost(@PathVariable Long id) {
        return postService.getSinglePost(id);
    }
    
    @PostMapping("/post/edit/{id}")
    public ResponseEntity<?> postEdit(@Valid @RequestBody PostRequest postRequest, BindingResult bindingResult, Authentication authentication, @PathVariable Long id) {
         return postService.edit(postRequest, authentication, id);
    }

    @PostMapping("/post/like/{id}")
    public ResponseEntity<?> postLike(@PathVariable Long id, Authentication authentication) {
        return postService.postLike(id, authentication);
    }

    @GetMapping("/post/likes/{id}")
    public ResponseEntity<?> getLikes(@PathVariable Long id) {
        return postService.getLikes(id);
    }
    
    
}
