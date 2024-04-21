package ru.skypro.homework.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserDto {
    @Size(min = 3, max = 10)
    String firstName;
    @Size(min = 3, max = 10)
    String lastName;
    @Pattern(regexp = "'\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}'")
    String phone;
}
