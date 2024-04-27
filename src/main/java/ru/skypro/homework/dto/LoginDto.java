package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @Size(min = 8, max = 16)
    private String password;
    @Size(min = 4, max = 32)
    private String username;
}