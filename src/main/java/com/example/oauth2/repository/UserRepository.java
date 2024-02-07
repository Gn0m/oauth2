package com.example.oauth2.repository;

import com.example.oauth2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u where u.userName=:username")
    User byUserName(String username);


    Optional<User> findByUserName(String userName);
}
