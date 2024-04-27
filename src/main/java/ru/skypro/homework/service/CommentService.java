package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

public interface CommentService {
    CommentsDto getCommentsByAdId(Integer id, Authentication authentication);

    CommentDto addComment(Integer id, Authentication authentication, CreateOrUpdateCommentDto newComment);

    void deleteComment(Integer id, Integer commentId, Authentication authentication);

    CommentDto updateComment(Integer adId, Integer commentId,
                             CreateOrUpdateCommentDto createOrUpdateAdDto, Authentication authentication);
}
