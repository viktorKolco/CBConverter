package com.CBConverter.service;

import com.CBConverter.entities.User;
import com.CBConverter.entities.UserRole;
import com.CBConverter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private final static String activationMessage = "Пожалуйста, пройдите по ссылке для активации аккаунта:" +
            " http://localhost:8082/activate/%s";
    private final static String afterActivationMessage = "Спасибо, что вы с нами, %s! Ваш аккаунт был успешно активирован.";

    public void addUser(User user) { //todo: напсать исключение CantAddUserException
        User userFromDB = userRepository.findByName(user.getName());
        if (userFromDB == null && !user.getEmail().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setActive(false);
            user.setName(user.getName());
            user.setRole(UserRole.USER);
            user.setActivationCode(UUID.randomUUID().toString());
            userRepository.save(user);

            mailSender.send(user.getEmail(), "ActivationCode", format(activationMessage, user.getActivationCode()));
        }
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) return false;

        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);
        log.debug("Пользователь '{}' был активирован и обновлен: '{}'", user.getId(), user.toString());
        mailSender.send(user.getEmail(), "Активация прошла успешно", format(afterActivationMessage, user.getName()));
        return true;
    }
}
