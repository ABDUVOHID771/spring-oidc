package com.example.springoidc;

import com.example.springoidc.dao.User;
import com.example.springoidc.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        dataLoader();
    }

    public void dataLoader() {

        User user = new User();
        user.setAuthorities("USER");
        user.setEmail("user@email.com");
        user.setEnable(true);
        user.setUsername("user");
        user.setPhone("1234567");
        user.setPassword(passwordEncoder.encode("user"));

        userService.save(user);

    }
}
