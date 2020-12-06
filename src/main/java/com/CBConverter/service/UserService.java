package com.CBConverter.service;

import com.CBConverter.entities.User;

public interface UserService {

    /**
     * <p>Добавить нового пользователя.</p>
     * @param user пользователь, которого нужно сохранить
     **/
    void addUser(User user);

    /**
     * <p>Активировать пользователя.</p>
     * @param code код активации
     **/
    void activateUser(String code);

    /**
     * <p>Получить текущего пользователя.</p>
     * @return текущий пользователь
     **/
    User getCurrentUser();
}
