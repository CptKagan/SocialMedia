package com.cptkagan.socialmediabackend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cptkagan.socialmediabackend.DTOs.FollowingAccountViewDTO;
import com.cptkagan.socialmediabackend.DTOs.BioUpdateRequest;
import com.cptkagan.socialmediabackend.DTOs.FollowersDTO;
import com.cptkagan.socialmediabackend.DTOs.PasswordChangeRequest;
import com.cptkagan.socialmediabackend.DTOs.PublicAccountViewDTO;
import com.cptkagan.socialmediabackend.DTOs.RegisterRequest;
import com.cptkagan.socialmediabackend.models.Account;
import com.cptkagan.socialmediabackend.repositories.AccountRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account getAccount(Authentication authentication){
        String userName = authentication.getName();
        Optional<Account> optionalAccount = accountRepository.findByUserName(userName);
        if(!optionalAccount.isPresent()){
            throw new NoSuchElementException();
        }
        return optionalAccount.get();
    }

    public ResponseEntity<?> save(RegisterRequest registerRequest){
        if(!accountRepository.existsByUserName(registerRequest.getUserName())){
            if(!accountRepository.existsByEmail(registerRequest.getEmail())){
                Account account = new Account(registerRequest);
                account.setPassword(passwordEncoder.encode(account.getPassword()));
                account.setCreatedAt(LocalDateTime.now());
                accountRepository.save(account);
                return ResponseEntity.ok("Account Created Successfully!");
            }
            return ResponseEntity.badRequest().body("Email is already in use!");
        }
        return ResponseEntity.badRequest().body("Username already exists!");
    }

    public Account findByUsername(String username) {
        return accountRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found!"));
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Account account = findByUsername(userName);
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(account.getUserName(), account.getPassword(), authorities);
    }

    public ResponseEntity<?> changePassword(PasswordChangeRequest passwordChangeRequest, Authentication authentication) {
        String userName = authentication.getName();
        Optional<Account> optionalAccount = accountRepository.findByUserName(userName);
        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            if(!passwordEncoder.matches(passwordChangeRequest.getOldPassword(), account.getPassword())){
                return ResponseEntity.badRequest().body("Wrong password!");
            }
            if(!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getNewPasswordConfirm())){ // .equals() FOR STRINGS!!!! NOT == / !=
                return ResponseEntity.badRequest().body("Password and Confirmation are Not Same!");
            }
            if(passwordEncoder.matches(passwordChangeRequest.getNewPassword(), account.getPassword())){
                return ResponseEntity.badRequest().body("Your current and new password cannot be the same!");
            }
            account.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
            accountRepository.save(account);
            return ResponseEntity.ok().body("Password Changed Successfully!");
        }
        return ResponseEntity.badRequest().body("Something unexpected happened. Try again later!");
    }

    @Transactional(rollbackOn = Exception.class, dontRollbackOn = NoSuchElementException.class)
    public ResponseEntity<?> follow(Authentication authentication, Long id) {
        try{
            Account follower = getAccount(authentication);
            Optional<Account> optionalAccount = accountRepository.findById(id);
            if(!optionalAccount.isPresent()){
                return ResponseEntity.badRequest().body("Account not found!");
            }
            Account followed = optionalAccount.get();
            if(follower.getAccountsThatYouFollow().contains(followed)){
                return ResponseEntity.badRequest().body("You already following this account!");
            }
            follower.getAccountsThatYouFollow().add(followed);
            followed.getAccountsThatFollowsYou().add(follower);
            accountRepository.save(follower);
            accountRepository.save(followed);
        } catch(NoSuchElementException e){
            return ResponseEntity.badRequest().body("Invalid Token!");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occured while processing your request.");
        }
        return ResponseEntity.ok("Follow process is completed!");
    }

    public ResponseEntity<?> myFollowers(Authentication authentication) {
        try{
            Account account = getAccount(authentication);
            List<Account> followers = accountRepository.findFollowersById(account.getId());
            List<FollowersDTO> followersDTO = followers.stream().map(FollowersDTO::new).collect(Collectors.toList());
            return ResponseEntity.ok(followersDTO);
        } catch(NoSuchElementException e){
            return ResponseEntity.badRequest().body("Invalid Token!");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occured while processing your request.");
        }
    }

    public ResponseEntity<?> updateBio(BioUpdateRequest bioUpdateRequest, Authentication authentication) {
        try{
            Account account = getAccount(authentication);
            account.setBio(bioUpdateRequest.getBio());
            accountRepository.save(account);
        } catch(NoSuchElementException e){
            return ResponseEntity.badRequest().body("Invalid Token!");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occured while processing your request.");
        }
        return ResponseEntity.ok("Bio updated successfully!");
    }

    public ResponseEntity<?> publicAccountView(Long id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(!optionalAccount.isPresent()){
            return ResponseEntity.badRequest().body("Account not found!");
        }
        PublicAccountViewDTO publicAccountViewDTO = new PublicAccountViewDTO(optionalAccount.get());
        return ResponseEntity.ok(publicAccountViewDTO);
    }

    public ResponseEntity<?> followingAccountView(Authentication authentication, Long id) {
        try{
            Account loggedIn = getAccount(authentication);
            Optional<Account> optionalAccount = accountRepository.findById(id);
            if(!optionalAccount.isPresent()){
                return ResponseEntity.badRequest().body("Account not found!");
            }
            Account targetAccount = optionalAccount.get();
            if(!loggedIn.getAccountsThatYouFollow().contains(targetAccount) && !targetAccount.getAccountsThatFollowsYou().contains(loggedIn)){
                PublicAccountViewDTO publicAccountViewDTO = new PublicAccountViewDTO(targetAccount);
                return ResponseEntity.ok(publicAccountViewDTO);
            }
            FollowingAccountViewDTO followingAccountViewDTO = new FollowingAccountViewDTO(targetAccount);
            return ResponseEntity.ok(followingAccountViewDTO);
        } catch(NoSuchElementException e){
            return ResponseEntity.badRequest().body("Invalid Token!");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occured while processing your request.");
        }
    }
}