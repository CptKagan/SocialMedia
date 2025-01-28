package com.cptkagan.socialmediabackend.DTOs;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.cptkagan.socialmediabackend.models.Post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO {
    private long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    private String userName;
    private int likes;
    private List<CommentDTO> comments;

    public PostDTO (Post post){
        this.id = post.getId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this. editedAt = post.getEditedAt();
        this.userName = post.getAccount().getUserName();
        this.likes = post.getLikes().size();
        this.comments = post.getComments().stream().map(CommentDTO::new).collect(Collectors.toList());
    }
}