package com.cptkagan.socialmediabackend.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {
    
    @NotBlank(message = "Username must not be blank!")
    @Size(min = 4, message = "Username must atleast 4 characters long!")
    private String userName;

    @NotBlank(message = "Email must not be blank!")
    @Email(message = "Please enter a valid email!")
    private String email;

    @NotBlank(message = "Password must not be blank!")
    @Size(min = 6, message = "Password must atleast 6 characters long!")
    private String password;
}