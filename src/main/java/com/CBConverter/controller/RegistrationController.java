package com.CBConverter.controller;

import com.CBConverter.entities.User;
import com.CBConverter.entities.UserRole;
import com.CBConverter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user) {
        User userFromDB = userRepository.findByName(user.getName());

        if (userFromDB == null){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setActive(true);
            user.setName(user.getName());
            user.setRole(UserRole.USER);
            userRepository.save(user);
        }
        return "redirect:/login";
    }
}
