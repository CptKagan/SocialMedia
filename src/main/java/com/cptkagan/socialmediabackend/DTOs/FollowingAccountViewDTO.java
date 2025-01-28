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
public class FollowingAccountViewDTO {
    private Long id;
    private String userName;
    private String bio;
    private List<FollowingDTO> accountsThatYouFollow;
    private List<FollowersDTO> accountsThatFollowsYou;
    private LocalDateTime createdAt;
    private List<PostDTO> postDTOs;
    
    public FollowingAccountViewDTO (Account account){
        this.id = account.getId();
        this.userName = account.getUserName();
        this.bio = account.getBio();
        this.accountsThatYouFollow = account.getAccountsThatYouFollow().stream().map(FollowingDTO::new).collect(Collectors.toList());
        this.accountsThatFollowsYou = account.getAccountsThatFollowsYou().stream().map(FollowersDTO::new).collect(Collectors.toList());
        this.createdAt = account.getCreatedAt();
        this.postDTOs = account.getPosts().stream().map(PostDTO::new).collect(Collectors.toList());
    }
}