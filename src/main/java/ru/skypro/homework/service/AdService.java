package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.model.Ad;

import java.io.IOException;

public interface AdService {
    AdsDto getAllAds();
    AdDto addAd(CreateOrUpdateAdDto ad, MultipartFile imageBytes, Authentication authentication) throws IOException;
    ExtendedAdDto getAds(Integer id, Authentication authentication);
    void removeAd(Integer id, Authentication authentication);
    AdDto updateAds(Integer id, CreateOrUpdateAdDto ad, Authentication authentication);
    AdsDto getMyAds(Authentication authentication);
    void updateImage(Integer id, MultipartFile image, Authentication authentication);
    Ad getById(Integer id);
    Ad createdAd(Ad ad);
}
