package com.CBConverter.controller;

import com.CBConverter.entities.User;
import com.CBConverter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user) {
        userService.addUser(user);
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            log.info("=== Пользователь активирован ===");
        } else {
            log.error("=== Активация пользователя НЕ была пройдена ===");
        }
        return "login";
    }
}
