package com.cptkagan.socialmediabackend.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostRequest {
    
    @NotBlank(message = "Content must not be blank!")
    @Size(min = 50, max = 2000, message = "Post must be between 50 and 2000 characters long!")
    private String content;
}