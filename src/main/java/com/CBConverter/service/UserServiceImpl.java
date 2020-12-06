package com.CBConverter.service;

import com.CBConverter.entities.User;
import com.CBConverter.entities.UserRole;
import com.CBConverter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private final static String activationMessage = "Пожалуйста, пройдите по ссылке для активации аккаунта:\n" +
            " http://localhost:%s/activate/%s";
    @Value("${server.port}")
    private String port;

    @Override
    public void addUser(User user) { //todo: написать исключение CantAddUserException
        User userFromDB = userRepository.findByName(user.getName());
        if (userFromDB == null && !user.getEmail().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setActive(false);
            user.setName(user.getName());
            user.setRole(UserRole.USER);
            user.setActivationCode(UUID.randomUUID().toString());
            userRepository.save(user);

            mailSender.send(user.getEmail(), "ActivationCode", format(activationMessage, port, user.getActivationCode()));
        }
    }

    @Override
    public void activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) return;

        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);
        log.debug("Пользователь '{}' был активирован и обновлен: '{}'", user.getId(), user.toString());
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (null == auth) {
            throw new IllegalStateException("Не нашел пользователя!!");
        }

        Object obj = auth.getPrincipal();
        String username;

        if (obj instanceof UserDetails) {
            username = ((UserDetails) obj).getUsername();
        } else {
            username = obj.toString();
        }

        return userRepository.findByName(username);
    }
}
