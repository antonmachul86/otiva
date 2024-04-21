package ru.skypro.homework.dto;

import javax.validation.constraints.Size;

public class NewPasswordDTO {
    @Size(min = 8, max = 16)
    private String currentPassword;
    @Size(min = 8, max = 16)
    private String newPassword;
}
