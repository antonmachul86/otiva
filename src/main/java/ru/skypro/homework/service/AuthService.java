package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;

public interface AuthService {
    boolean login(String username, String password);

    boolean register(Register register);
}
