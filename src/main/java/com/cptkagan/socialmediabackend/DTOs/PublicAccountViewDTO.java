package com.cptkagan.socialmediabackend.DTOs;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.cptkagan.socialmediabackend.models.Account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PublicAccountViewDTO {
    private Long id;
    private String userName;
    private String bio;
    private int followerCount;
    private int followingCount;
    private LocalDateTime createdAt;
    private List<PostDTO> postDTOs;

    public PublicAccountViewDTO (Account account){
        this.id = account.getId();
        this.userName = account.getUserName();
        this.bio = account.getBio();
        this.createdAt = account.getCreatedAt();
        this.followerCount = account.getAccountsThatFollowsYou().size();
        this.followingCount = account.getAccountsThatYouFollow().size();
        this.postDTOs = account.getPosts().stream().map(PostDTO::new).collect(Collectors.toList());
    }
}