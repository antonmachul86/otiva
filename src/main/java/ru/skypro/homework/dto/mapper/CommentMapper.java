package ru.skypro.homework.dto.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.model.Comment;

@Component
public class CommentMapper {
    public static CommentDto mapIntoCommentDto(Comment entity){
        CommentDto dto = new CommentDto();
        dto.setAuthor(entity.getAuthor().getId());
        if(entity.getAuthor().getImageUrl()!=null){
            dto.setAuthorImage(entity.getAuthor().getImageUrl());
        }
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setPk(entity.getId());
        dto.setText(entity.getText());
        return dto;
    }
}
