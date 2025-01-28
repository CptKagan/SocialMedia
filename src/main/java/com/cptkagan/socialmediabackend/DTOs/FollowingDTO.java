package com.cptkagan.socialmediabackend.DTOs;

import com.cptkagan.socialmediabackend.models.Account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FollowingDTO {
    private Long id;
    private String userName;

    public FollowingDTO(Account account){
        this.id = account.getId();
        this.userName = account.getUserName();
    }
}