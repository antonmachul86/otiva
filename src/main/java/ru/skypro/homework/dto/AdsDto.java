package ru.skypro.homework.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AdsDto {
    private Integer count;
    private List<AdDto> results;

    public AdsDto(List<AdDto> results) {
        this.count = results.size();
        this.results = results;
    }
}