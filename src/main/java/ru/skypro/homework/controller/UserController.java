package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDTO;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;

import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @Operation(
            tags = "Пользователи",
            summary = "Обновление пароля автоматизированного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пароль одновлён",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Доступ запрещен",
                            content = @Content()
                    )
            }
    )
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword (@RequestBody NewPasswordDTO newPasswordDTO, Authentication authentication){
        try {
            userService.setPassword(newPasswordDTO, authentication);
            return ResponseEntity.ok().build();
        }catch (HttpClientErrorException.Forbidden e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    @Operation(
            tags = "Пользователи",
            summary = "Получение информации об авторизированном пользователе",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Информация об авторизированном пользователе",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDto.class)
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content()
                    )
            )
    )
    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(Authentication authentication){
        return ResponseEntity.ok(userService.getUserInfo(authentication));
    }
    @Operation(
            tags = "Пользователи",
            summary = "Обновление информации об авторизированном пользователе",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация об авторизированном пользователе",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content()
                    )
            }
    )
    @PatchMapping(value = "/me/image",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> updateUserImage(@RequestBody MultipartFile image,
                                                Authentication authentication) throws IOException{
        userService.updateUserImage(image, authentication);
        return ResponseEntity.ok().build();
    }
}
