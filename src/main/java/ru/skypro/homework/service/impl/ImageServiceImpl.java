package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    /**
     * Метод возвращает картинку, найденную по переданному идентификатору. <br>
     * {@link ImageRepository#findById(Object)}
     *
     * @param id идентификатор картинки
     * @return найденную картинку {@link Image}
     */
    @Override
    public Image getById(Integer id) {
        return imageRepository.findById(id).orElseThrow(ImageNotFoundException::new);
    }

    /**
     * Метод сохраняет картинку в базу данных. <br>
     * {@link ImageRepository#save(Object)}
     * @param multipartFile файл для сохранения картинки
     * @return сохраненную картинку {@link Image}
     */
    @Override
    public Image saveToDataBase(MultipartFile multipartFile) throws IOException {
        Image image = new Image();
        image.setData(multipartFile.getBytes());
        image.setFileSize(multipartFile.getSize());
        image.setMediaType(multipartFile.getContentType());
        return imageRepository.save(image);
    }

    /**
     * Метод удаляет картинку из базы данных. <br>
     * {@link ImageRepository#delete(Object)}
     * @param image картинка для удаления
     */
    @Override
    public void deleteImage(Image image) {
        imageRepository.delete(image);
    }
}