package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.UserDetailsManagerImpl;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.dto.mapper.UserMapper;
import ru.skypro.homework.exception.InvalidMediaTypeException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final UserDetailsManagerImpl manager;

    /**
     * Метод устанавливает новый пароль авторизированному пользователю. <br>
     * {@link UserRepository#findById(Object)}
     * {@link UserDetailsManagerImpl#changePassword(String, String, User)}
     *
     * @param newPasswordDto новый пароль
     * @param authentication
     */
    @Override
    public void setPassword(NewPasswordDto newPasswordDTO, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);
        String oldPassword = newPasswordDTO.getCurrentPassword();
        String newPassword = newPasswordDTO.getNewPassword();
        manager.changePassword(oldPassword, newPassword, user);
    }

    /**
     * Метод возвращает информацию об авторизированном пользователе. <br>
     * {@link UserRepository#findByEmail(String)}
     *
     * @param authentication
     * @return сущность пользователя в формате {@link UserDto}
     */
    @Override
    public UserDto getUserInfo(Authentication authentication) {
        return UserMapper.mapIntoUserDto(userRepository.findByEmail(authentication.getName()).
                orElseThrow(UserNotFoundException::new));
    }

    /**
     * Метод обновляет информацию об авторизированном пользователе. <br>
     * {@link UserRepository#findByEmail(String)}
     * {@link UserRepository#save(Object)}
     *
     * @param newProperties  новые данные для авторизированного пользователя
     * @param authentication
     * @return обновленного пользователя в формате {@link UserDto}
     */
    @Override
    public UpdateUserDto updateUser(UpdateUserDto newProperties, Authentication authentication) {
        User updatedUser = userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);
        Optional.ofNullable(newProperties.getFirstName()).ifPresent(updatedUser::setFirstName);
        Optional.ofNullable(newProperties.getLastName()).ifPresent(updatedUser::setLastName);
        Optional.ofNullable(newProperties.getPhone()).ifPresent(updatedUser::setPhoneNumber);
        return UserMapper.mapIntoUpdateUserDto(updatedUser);
    }

    /**
     * Метод обновляет картинку авторизированного пользователя. <br>
     * {@link UserRepository#findByEmail(String)}
     * {@link UserRepository#save(Object)}
     * {@link ImageService#saveToDataBase(MultipartFile)}
     * {@link ImageService#deleteImage(Image)}
     *
     * @param image          новая картинка
     * @param authentication
     */
    @Override
    public void updateUserImage(MultipartFile image, Authentication authentication) throws IOException {
        if (!ValidationService.isImage(image)) {
            throw new InvalidMediaTypeException();
        }
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFoundException::new);
        Image userImage = user.getImage();
        Image newImage = imageService.saveToDataBase(image);
        if(user.getImage()!=null){
            imageService.deleteImage(userImage);
        }
        user.setImage(newImage);
        user.setImageUrl("/images"+newImage.getId());
        userRepository.save(user);
    }

    /**
     * Метод возвращает пользователя, найденного по переданному логину. <br>
     * {@link UserRepository#findByEmail(String)}
     * @param email логин для поиска
     * @return найденного пользователя {@link User}
     */
    @Override
    public User findByEmail(String email) {
       return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Метод создает полученного пользователя в базе данных. <br>
     * {@link UserRepository#save(Object)}
     * @param user пользователь, которого необходимо сохранить
     * @return сохраненного пользователя {@link User}
     */
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
