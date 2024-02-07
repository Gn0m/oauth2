package com.example.oauth2.repository;

import com.example.oauth2.entity.UserAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAttemptsRepo extends JpaRepository<UserAttempts, Long> {

    Optional<UserAttempts> findByLogin(String login);
}
