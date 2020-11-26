package com.CBConverter.controller;

import com.CBConverter.entities.User;
import com.CBConverter.entities.UserRole;
import com.CBConverter.repository.UserRepository;
import com.CBConverter.service.MailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private final static String message = "Пожалуйста, пройдите по ссылке для активации аккаунта:" +
            " http://localhost:8082/activate/%s";

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user) {
        User userFromDB = userRepository.findByName(user.getName());
        if (userFromDB == null && !user.getEmail().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setActive(false);
            user.setName(user.getName());
            user.setRole(UserRole.USER);
            user.setActivationCode(UUID.randomUUID().toString());
            userRepository.save(user);

            mailSender.send(user.getEmail(), "ActivationCode", format(message, user.getActivationCode()));
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code) {
        boolean isActivated = activateUser(code);

        if (isActivated) {
            log.info("=== Пользователь активирован ===");
        } else {
            log.info("=== Активация пользователя НЕ была пройдена ===");
        }
        return "login";
    }

    private boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) return false;

        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);

        return true;
    }
}
