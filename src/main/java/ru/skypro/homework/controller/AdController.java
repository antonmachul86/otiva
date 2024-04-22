package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import liquibase.pro.packaged.P;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.Implementation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;

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

    @Operation(
            tags = "Комментарии",
            summary = "Получение коммнтариев объявления, найденного по переданному идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Объявления с переданным идентификатором не существует в базе данных",
                            content = @Content()
                    )
            }
    )
    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentDto> addComments(@PathVariable("id") Integer id,
                                                  Authentication authentication) {
        try {
            return ResponseEntity.ok(commentService.getCommentsById(id, authentication));
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
            tags = "Комментарии",
            summary = "Добавление комментария к объявлению, найденного по идентефикатору",
            responses = {
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content()
                    )
            }
    )
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable("id") Integer id,
                                                 Authentication authentication,
                                                 @RequestBody CreateOrUpdateAdDto newComment) {
        try {
            return ResponseEntity.ok(commentService.addComment(id, authentication, newComment));
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
            tags = "Объявления",
            summary = "Получение информации об объявлении по переданному идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация об объявлении",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExtendedAdDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не найден",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Объявления с переданными идентификаторами не существуют в БД",
                            content = @Content()
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdDto> getAds(@PathVariable("id") Integer id, Authentication authentication) {
        return ResponseEntity.ok(adService.getAds(id, authentication));
    }

    @Operation(
            tags = "Объявления",
            summary = "Удаление объявления по переданому идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Объявление успешно удалено",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизирован",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет доступа к операции",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Объявления с переданным идентификатором не существует",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{id}")
    ResponseEntity<Void> removeAd(@PathVariable("id") Integer id, Authentication authentication) {
        try {
            adService.removeAd(id, authentication);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
            tags = "Объявления",
            summary = "Обновлеине информации об объявлении, найденному по переданному идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация об объявлении обновлена",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdDto.class)
                            )
                    )
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<AdDto> updateAds(@PathVariable("id") Integer id,
                                           @RequestBody CreateOrUpdateAdDto ad,
                                           Authentication authentication) {
        try {
            return ResponseEntity.ok(adService.updateAds(id, ad, authentication));
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
            tags = "Комментарии",
            summary = "Удаление комментария (найденного по переданному идентификатору) " +
                    "у объявления, найденного по переданному идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Комментарий удалён",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет доступа к операции",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Объявления/комментария с переданным идентификатором не существует в базе данных",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentDto.class)
                            )
                    )
            }
    )
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("adId") Integer adId,
                                              @PathVariable("commentId") Integer commentId,
                                              Authentication authentication) {
        try {
            commentService.deleteComment(adId, commentId, authentication);
            return ResponseEntity.ok().build();
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



    @Operation(
            tags = "Комментарии",
            summary = "Обновление комментария (найденного по переданному идентификатору) " +
                    "у объявления, найденного по переданному идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Комментарий обновлён",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CommentDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет доступа к операции",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Объявления/комментария с переданным идентификатором не существует в базе данных",
                            content = @Content()
                    )
            }
    )
    @PostMapping("/{adId}/comments{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("adId")Integer adId,
                                                    @PathVariable("commentId")Integer commentId,
                                                    Authentication authentication,
                                                    @RequestBody CreateOrUpdateAdDto comment){
        try {
            return ResponseEntity.ok(commentService.updateComment(adId, commentId, comment,authentication));
        }catch (HttpClientErrorException.NotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @Operation(
            tags = "Объявления",
            summary = "Получение всех объявлений авторизированного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Коллекция объявлений авторизированного пользователя",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdsDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content()
                    )
            }
    )
    @GetMapping("/me")
    public AdsDto getAdsMe(Authentication authentication){
        return adService.getMyAds(authentication);
    }
    @Operation(
            tags = "Объявления",
            summary = "Обновление картинки объявления, найденного по переданому идентификатору",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Картинка обновлена",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет доступа к операции",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Объявления с переданным идентификатором не существует в базе данных",
                            content = @Content()
                    )
            }
    )
    @PatchMapping(value = "/{id}/image",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> updateImage(@PathVariable("id") Integer id,
                                            @RequestBody MultipartFile image,
                                            Authentication authentication)throws IOException{
        try {
            adService.updateImage(id,image,authentication);
            return ResponseEntity.ok().build();
        }catch (HttpClientErrorException.NotFound e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
