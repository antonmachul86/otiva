package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.AuthService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            tags = "Авторизация",
            summary = "Авторизация пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь авторизован",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь не авторизован",
                            content = @Content()
                    )
            }
    )

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        if (authService.login(loginDto.getUsername(), loginDto.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @Operation(
            tags = "Регистрация",
            summary =  "Регистрация пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Пользователь создан",
                            content = @Content()
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDTO) {
        if (authService.register(registerDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
