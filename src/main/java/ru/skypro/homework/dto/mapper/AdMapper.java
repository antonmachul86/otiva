package ru.skypro.homework.dto.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.model.Ad;

@Component
public class AdMapper {

    public static AdDto mapIntoAdDto(Ad entity) {
        AdDto dto = new AdDto();
        dto.setPk(entity.getId());
        dto.setAuthor(entity.getAuthor().getId());
        dto.setImage(entity.getImageUrl());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    public static Ad mapIntoAdEntity(CreateOrUpdateAdDto dto) {
        Ad entity = new Ad();
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        entity.setTitle(dto.getTitle());
        return entity;
    }

    public static ExtendedAdDto mapIntoExtendedAdDto(Ad entity) {
        ExtendedAdDto dto = new ExtendedAdDto();
        dto.setPk(entity.getId());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setAuthorLastName(entity.getAuthor().getLastName());
        dto.setDescription(entity.getDescription());
        dto.setEmail(entity.getAuthor().getEmail());
        dto.setImage(entity.getImageUrl());
        dto.setPhone(entity.getAuthor().getPhoneNumber());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());
        return dto;
    }
}