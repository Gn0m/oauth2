package com.example.oauth2.service;

import com.example.oauth2.entity.User;
import com.example.oauth2.entity.UserAttempts;
import com.example.oauth2.repository.UserAttemptsRepo;
import com.example.oauth2.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserAttemptsService {

    private final UserAttemptsRepo repo;
    private final UserRepository userRepo;
    private static final int MAX_ATTEMPT = 3;

    public UserAttemptsService(UserAttemptsRepo repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public void create(String login) {
        Optional<UserAttempts> byLogin = repo.findByLogin(login);

        if (byLogin.isEmpty()) {
            UserAttempts userAttempt = UserAttempts
                    .builder()
                    .count(MAX_ATTEMPT)
                    .lastModifier(new Date())
                    .login(login)
                    .build();
            repo.save(userAttempt);
        }
    }

    public void resetFailAttempts(String login) {
        Optional<UserAttempts> byLogin = repo.findByLogin(login);
        if (byLogin.isPresent()) {
            UserAttempts userAttempts = byLogin.get();
            userAttempts.setCount(MAX_ATTEMPT);
            userAttempts.setLastModifier(new Date());
            repo.save(userAttempts);
        }
    }

    public void updateFailAttempts(String login) {
        Optional<UserAttempts> byLogin = repo.findByLogin(login);

        if (byLogin.isPresent()) {
            UserAttempts userAttempt = byLogin.get();

            if (userAttempt.getCount() - 1 == 0) {
                User user = userRepo.byUserName(login);

                userAttempt.setCount(MAX_ATTEMPT);
                user.setAccountNonLocked(false);

                userRepo.save(user);
            } else {
                userAttempt.setCount(userAttempt.getCount() - 1);
            }
            userAttempt.setLastModifier(new Date());
            repo.save(userAttempt);
        }
    }

    public void delete(String userName) {
        User user = userRepo.findByUserName(userName).orElseThrow(() ->
                new UsernameNotFoundException("Пользователь не найден"));
        userRepo.delete(user);
    }
}

