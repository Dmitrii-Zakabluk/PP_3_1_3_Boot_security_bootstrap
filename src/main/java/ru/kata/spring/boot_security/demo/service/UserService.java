package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void saveUser(User user);

    void updateUser(User user);

    void deleteUserById(Long id);

    User findUserByUsername(String username);

    boolean existsById(Long id);

    User getUser(Long id);

    List<User> getAllUsers();
}
