package com.cptkagan.socialmediabackend.controllers;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cptkagan.socialmediabackend.DTOs.BioUpdateRequest;
import com.cptkagan.socialmediabackend.DTOs.LoginRequest;
import com.cptkagan.socialmediabackend.DTOs.PasswordChangeRequest;
import com.cptkagan.socialmediabackend.DTOs.RegisterRequest;
import com.cptkagan.socialmediabackend.security.JwtTokenUtil;
import com.cptkagan.socialmediabackend.services.AccountService;
import com.cptkagan.socialmediabackend.services.AuthService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {
    
    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }        
        return accountService.save(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        try{
            authService.authenticate(loginRequest.getUserName(), loginRequest.getPassword()); // Giriş yapan kişinin doğrulanması (içeride AuthenticationManager çağırılıyor)
            String token = jwtTokenUtil.generateToken(loginRequest.getUserName());
            return ResponseEntity.ok(Map.of("token",token));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/changepassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest, Authentication authentication) {
        return accountService.changePassword(passwordChangeRequest, authentication);
    }

    @PostMapping("/follow/account/{id}")
    public ResponseEntity<?> follow(Authentication authentication, @PathVariable Long id) {
        return accountService.follow(authentication, id);
    }

    @GetMapping("/myfollowers")
    public ResponseEntity<?> myFollowers(Authentication authentication) {
        return accountService.myFollowers(authentication);
    }
    
    @PostMapping("/updatebio")
    public ResponseEntity<?> updateBio(@Valid @RequestBody BioUpdateRequest bioUpdateRequest, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            return ResponseEntity.badRequest().body(errors);
        }
        return accountService.updateBio(bioUpdateRequest, authentication);
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<?> accounView(@PathVariable Long id, Authentication authentication) {
        if(authentication == null){
            return accountService.publicAccountView(id);
        }
        return accountService.followingAccountView(authentication, id);
    }
}
