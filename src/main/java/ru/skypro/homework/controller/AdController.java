package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdController {
    private final AdService adService;
    private final CommentService commentService;

    @Operation(
            tags = "Объявления",
            summary = "Получение всех объявлений, находящихся в базе данных",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Коллекция всех объявлений, находящихся в базе данных",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsDto.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<AdsDto> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @Operation(
            tags = "Объявления",
            summary = "Создание объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Объявление успешно создано",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content()
                    )
            }
    )
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AdDto> addAd(@RequestPart(value = "properties") CreateOrUpdateAdDto properties,
                                       @RequestPart("image") MultipartFile image,
                                       Authentication authentication) throws IOException {
        adService.addAd(properties, image, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}