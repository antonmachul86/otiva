package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdateAdDto {
    @Size(min = 4, max = 32)
    private String title;
    @Size(max = 10_000_000)
    private Integer price;
    @Size(min = 8, max = 64)
    private String description;
}