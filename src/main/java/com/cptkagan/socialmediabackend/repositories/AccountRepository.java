package com.cptkagan.socialmediabackend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cptkagan.socialmediabackend.models.Account;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByUserName(String userName);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);

    @Query("SELECT a FROM Account a JOIN a.accountsThatYouFollow f WHERE f.id = :accountId")
    List<Account> findFollowersById(@Param("accountId") Long accountId);
}