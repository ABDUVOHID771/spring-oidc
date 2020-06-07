package com.example.springoidc.service;

import com.example.springoidc.dao.User;
import com.example.springoidc.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public void delete(User user) {
        userRepo.delete(user);
    }

}
