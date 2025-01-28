package com.cptkagan.socialmediabackend.models;

import java.time.LocalDateTime;
import java.util.List;

import com.cptkagan.socialmediabackend.DTOs.CommentRequest;
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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(columnDefinition = "TEXT", length = 350)
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime editedAt;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    @JsonBackReference("account-comments")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonBackReference("post-comments")
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Likes> likes;

    public Comment(CommentRequest commentRequest){
        this.content = commentRequest.getContent();
    }
}