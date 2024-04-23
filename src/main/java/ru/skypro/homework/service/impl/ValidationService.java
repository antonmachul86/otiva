package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ValidationService {
    /**
     * Метод сравнивает роль авторизированного пользователя с {@link ru.skypro.homework.constant.Role#ADMIN}. <br>
     * {@link Authentication#getAuthorities()}
     *
     * @param authentication
     * @return true - в случае, если авторизированный пользователь имеет роль
     * {@link ru.skypro.homework.constant.Role#ADMIN}
     */
    public static boolean isAdmin (Authentication authentication){
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList())
                .contains("ROLE_ADMIN");
    }

    /**
     * Метод проверяет, является ли авторизированный пользователь автором объявления {@link ru.skypro.homework.model.Ad}
     * или комментария {@link ru.skypro.homework.model.Comment}. <br>
     * {@link Authentication#getName()}
     *
     * @param userName       логин проверяемого пользователя
     * @param authentication
     * @return true - в случае, если пользователь является автором объявления {@link ru.skypro.homework.model.Ad}
     * или комментария {@link ru.skypro.homework.model.Comment}
     */

    public static boolean isOwner(Authentication authentication, String userName){
        return authentication.getName().equals(userName);
    }

    /**
     * Метод проверяет, является ли переданный файл картинкой. <br>
     *
     * @param image проверяемый файл
     * @return true - в случае, если файл является картинкой
     */
public static boolean isImage(MultipartFile image){
    return Objects.requireNonNull(image.getContentType()).startsWith("image/");
}

}
