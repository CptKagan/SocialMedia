package com.cptkagan.socialmediabackend.models;

import java.time.LocalDateTime;
import java.util.List;

import com.cptkagan.socialmediabackend.DTOs.PostRequest;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(columnDefinition = "TEXT", length = 2000)
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime editedAt;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = true)
    @JsonBackReference
    private Account account;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference("post-comments")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference("post-likes")
    private List<Likes> likes;

    public Post(PostRequest postRequest){
        this.content = postRequest.getContent();
    }
}