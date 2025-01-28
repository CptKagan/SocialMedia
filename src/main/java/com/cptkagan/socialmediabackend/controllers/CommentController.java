package com.cptkagan.socialmediabackend.controllers;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cptkagan.socialmediabackend.DTOs.CommentRequest;
import com.cptkagan.socialmediabackend.services.CommentService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.core.Authentication;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/comment/add/{id}")
    public ResponseEntity<?> addComment(@Valid @RequestBody CommentRequest commentRequest, BindingResult bindingResult, Authentication authentication, @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }

        return commentService.save(commentRequest, authentication, id);
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<?>  getCommentsOfPost(@PathVariable Long id) {
        return commentService.getCommentsOfPost(id);
    }
    
    @PostMapping("/comment/edit/{id}")
    public ResponseEntity<?> editComment(@Valid @RequestBody CommentRequest commentRequest, @PathVariable Long id, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        return commentService.editComment(commentRequest, id, authentication);
    }
    
    @PostMapping("/comment/like/{id}")
    public ResponseEntity<?> likeComment(@PathVariable Long id, Authentication authentication) {
        return commentService.likeComment(id, authentication);
    }
    
    
}
