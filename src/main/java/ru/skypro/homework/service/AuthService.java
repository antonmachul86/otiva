package ru.skypro.homework.service;

import ru.skypro.homework.dto.RegisterDto;

public interface AuthService {
    boolean login(String username, String password);

    boolean register(RegisterDto register);
}
