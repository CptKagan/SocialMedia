package com.cptkagan.socialmediabackend.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordChangeRequest {

    @NotBlank(message = "Old password must not be blank!")
    @Size(min = 6, message = "Old password must atleast 6 characters long!")
    private String oldPassword;

    @NotBlank(message = "New password must not be blank!")
    @Size(min = 6, message = "New password must atleast 6 characters long!")
    private String newPassword;

    @NotBlank(message = "Password confirmation must not be blank!")
    @Size(min = 6, message = "Password confirmation must atleast 6 characters long!")
    private String newPasswordConfirm;
}