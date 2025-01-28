package com.cptkagan.socialmediabackend.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BioUpdateRequest {
    @NotBlank(message = "Bio must not be blank!")
    @Size(min = 10, max = 300, message = "Bio must be between 10 and 300 characters long!")
    private String bio;
}