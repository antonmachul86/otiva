package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.config.UserDetailsManagerImpl;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.AuthService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManagerImpl manager;
    private final PasswordEncoder encoder;

    /**
     * Метод производит авторизацию пользователя по переданным логину и паролю. <br>
     * {@link UserDetailsManagerImpl#userExists(String)}
     * {@link PasswordEncoder#matches(CharSequence, String)}
     *
     * @param userName логин
     * @param password пароль
     * @return true - в случае успешной авторнизации,
     * false - в случае отсутствия пользователя с переданным логином в базе данных.
     */
    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    /**
     * Метод регистрирует нового пользователя на основе переданных данных и сохраняет его в базе данных. <br>
     * {@link UserDetailsManagerImpl#userExists(String)}
     * {@link UserDetailsManagerImpl#createUser(RegisterDto)}
     * @param registerDto данные для создания пользователя
     * @return true - в случае успешной регистрации,
     * false - в случае если в базе данных уже существует пользователь с переданным логином.
     */
    @Override
    public boolean register(RegisterDto registerDto) {
        if (manager.userExists(registerDto.getUsername())) {
            return false;
        }
        registerDto.setPassword(registerDto.getPassword());
        manager.createUser(registerDto);
        return true;
    }
}