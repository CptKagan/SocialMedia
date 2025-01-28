package com.cptkagan.socialmediabackend.DTOs;

import com.cptkagan.socialmediabackend.models.Account;
import com.cptkagan.socialmediabackend.models.Likes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FollowersDTO {
    private Long id;
    private String userName;

    public FollowersDTO(Account account){
        this.id = account.getId();
        this.userName = account.getUserName();
    }

    public FollowersDTO(Likes likes){
        this.id = likes.getAccount().getId();
        this.userName = likes.getAccount().getUserName();
    }
}