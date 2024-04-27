package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.User;

import java.io.IOException;

public interface UserService {
    void setPassword(NewPasswordDto newPasswordDTO, Authentication authentication);
    UserDto getUserInfo(Authentication authentication);
    UpdateUserDto updateUser(UpdateUserDto updateUserDto, Authentication authentication);
    void updateUserImage(MultipartFile image, Authentication authentication) throws IOException;
    User findByEmail(String email);
    User createUser(User user);
}
