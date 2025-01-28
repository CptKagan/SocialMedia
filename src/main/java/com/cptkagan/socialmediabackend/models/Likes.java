package com.cptkagan.socialmediabackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Account account;
    
    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    @JsonBackReference
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id", referencedColumnName = "id")
    @JsonBackReference
    private Comment comment;
}
