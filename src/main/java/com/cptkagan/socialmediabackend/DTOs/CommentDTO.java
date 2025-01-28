package com.cptkagan.socialmediabackend.DTOs;

import java.time.LocalDateTime;

import com.cptkagan.socialmediabackend.models.Comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;
    private String userName;
    private int likes;

    public CommentDTO(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.editedAt = comment.getEditedAt();
        this.userName = comment.getAccount().getUserName();
        this.likes = comment.getLikes().size();
    }
}