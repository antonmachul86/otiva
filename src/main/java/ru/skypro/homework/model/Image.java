package ru.skypro.homework.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Lob
    private byte[] data;
    private long fileSize;
    private String mediaType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return fileSize == image.fileSize && Objects.equals(id, image.id) && Arrays.equals(data, image.data) && Objects.equals(mediaType, image.mediaType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, fileSize, mediaType);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", data=" + Arrays.toString(data) +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType + '\'' +
                '}';
    }
}