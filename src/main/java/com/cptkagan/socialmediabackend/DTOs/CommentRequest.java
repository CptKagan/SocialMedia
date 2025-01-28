package com.cptkagan.socialmediabackend.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {
    @NotBlank(message = "Content must not be blank!")
    @Size(min = 20, max = 350, message = "Comment must be between 20 and 350 characters long!")
    private String content;
}