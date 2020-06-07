package com.example.springoidc.controller;

import com.example.springoidc.dao.User;
import com.example.springoidc.dao.UserPrincipal;
import com.example.springoidc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/")
public class MainController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public MainController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "loginHtml";
    }

    @RequestMapping("/")
    public String oauthUserInfo(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                                @AuthenticationPrincipal OAuth2User oauth2User, Model model) {
        Map<String, Object> attributes = new HashMap<>();
        Object value;
        for (String key : oauth2User.getAttributes().keySet()) {
            if (oauth2User.getAttributes().get(key) == null) {
                value = " ";
            } else {
                value = oauth2User.getAttributes().get(key);
            }
            attributes.put(key, value.toString());
        }
        log.info("USER INFO : {} ", oauth2User.toString());
        model.addAttribute("userAttributes", attributes);
        model.addAttribute("username", oauth2User.getName());
        model.addAttribute("authorities", oauth2User.getAuthorities());
        model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
        return "index";
    }

    @RequestMapping(value = "/user")
    public String userInfo(@AuthenticationPrincipal UserPrincipal userPrincipal, Model model) {
        User user = userService.findByUsername(userPrincipal.getUsername());
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("password", user.getPassword());
        attributes.put("email", user.getEmail());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("authorities", user.getAuthorities());
        model.addAttribute("clientName", "Login");
        model.addAttribute("userAttributes", attributes);
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("not_available", false);
        return "register";
    }

    @PostMapping("/register")
    public String registration(@ModelAttribute("user") User user, Model model) {
        User availableUser = userService.findByUsername(user.getUsername());
        if (availableUser == null) {
            user.setAuthorities("USER");
            user.setEnable(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.save(user);
        } else {
            model.addAttribute("not_available", true);
            return "register";
        }
        return "redirect:/loginPage";
    }

    @GetMapping("/forget")
    public String forget(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "forget";
    }

    @PostMapping("/forget")
    public String forgetConf(@RequestParam("email") String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return "redirect:/loginPage";
        } else {
            userService.delete(user);
            return "redirect:/register";
        }
    }

}
