package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.service.ImageService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getById(@PathVariable("id") Integer id) {
        Image image = imageService.getById(id);
        byte[] data = image.getData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getMediaType()));
        headers.setContentLength(image.getFileSize());
        return ResponseEntity.status(200).headers(headers).body(data);
    }
}