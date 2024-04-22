package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;

public interface CommentService {
    CommentsDto getCommentsByAdId(Integer id, Authentication authentication);

    CommentDto addComment(Integer id, Authentication authentication, CreateOrUpdateAdDto newComment);

    void deleteComment(Integer id, Authentication authentication);

    CommentDto updateComment(Integer adId, Integer commentId,
                             CreateOrUpdateAdDto createOrUpdateAdDto, Authentication authentication);
}
