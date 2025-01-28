package com.cptkagan.socialmediabackend.models;

import java.time.LocalDateTime;
import java.util.List;

import com.cptkagan.socialmediabackend.DTOs.RegisterRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Email
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String userName;

    private String password;

    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT", length = 300)
    private String bio;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Post> posts;

    @ManyToMany
    @JoinTable(name = "account_following", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "followed_id"))
    @JsonManagedReference
    private List<Account> accountsThatYouFollow;

    @ManyToMany(mappedBy = "accountsThatYouFollow")
    @JsonIgnore
    private List<Account> accountsThatFollowsYou;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonManagedReference("account-comments")
    private List<Comment> comments;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonManagedReference("account-likes")
    private List<Likes> likes;

    public Account(RegisterRequest registerRequest){
        this.userName = registerRequest.getUserName();
        this.password = registerRequest.getPassword();
        this.email = registerRequest.getEmail();
    }

}