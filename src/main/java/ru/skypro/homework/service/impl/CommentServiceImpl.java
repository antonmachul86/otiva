package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.dto.mapper.CommentMapper;
import ru.skypro.homework.exception.AccessDeniedException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.UserUnauthorizedException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.UserService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final AdService adService;

    /**
     * Метод возвращает коллекцию всех комментариев у объявления, найденного по переданному идентификатору. <br>
     * {@link CommentRepository#findByAdId(Integer)}
     * @param id             идентификатор объявления
     * @param authentication
     * @return коллекцию комментариев в формате {@link CommentsDto}
     */
    @Override
    public CommentsDto getCommentsByAdId(Integer id, Authentication authentication) {
        if (authentication.isAuthenticated()) {
            List<CommentDto> allComments = commentRepository.findByAdId(id).stream()
                    .map(CommentMapper::mapIntoCommentDto)
                    .collect(Collectors.toList());
            return new CommentsDto(allComments.size(), allComments);
        } else {
            throw new UserUnauthorizedException();
        }
    }

    /**
     * Метод добавляет комментарий к объявлению, найденному по переданному идентификатору,
     * и сохраняет его в базу данных. <br>
     * {@link CommentRepository#save(Object)}
     * {@link AdService#getById(Integer)}
     * {@link UserService#findByEmail(String)}
     * {@link UserService#createUser(User)}
     * @param id идентификатор объявления
     * @param newComment новый комментарий
     * @param authentication
     * @return комментарий в формате {@link CommentDto}
     */
    @Override
    public CommentDto addComment(Integer id, Authentication authentication, CreateOrUpdateCommentDto newComment) {
        if (authentication.isAuthenticated()) {
            Ad adForComment = adService.getById(id);
            User author = userService.findByEmail(authentication.getName());
            Comment commentToAdd = new Comment();
            commentToAdd.setText(newComment.getText());
            commentToAdd.setCreatedAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000);
            commentToAdd.setAuthor(author);
            commentToAdd.setAd(adForComment);
            commentRepository.save(commentToAdd);
            author.getComments().add(commentToAdd);
            userService.createUser(author);
            return CommentMapper.mapIntoCommentDto(commentToAdd);
        } else {
            throw new UserUnauthorizedException();
        }
    }

    /**
     * Метод удаляет комментарий, найденный по переданному идентификатору,
     * у объявления, найденного по переданному идентификатору. <br>
     * {@link CommentRepository#findById(Object)}
     * {@link CommentRepository#delete(Object)}
     * {@link AdService#getById(Integer)}
     * {@link ValidationService#isAdmin(Authentication)}
     * {@link ValidationService#isOwner(Authentication, String)}
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param authentication
     */
    @Override
    public void deleteComment(Integer adId, Integer commentId, Authentication authentication) {
        Comment commentToDelete = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        String commentAuthorName = commentToDelete.getAuthor().getEmail();
        if (ValidationService.isAdmin(authentication) || ValidationService.isOwner(authentication, commentAuthorName)) {
            adService.getById(adId).getComments().remove(commentToDelete);
            commentRepository.delete(commentToDelete);
        } else {
            throw new AccessDeniedException();
        }
    }

    /**
     * Метод обновляет комментарий, найденный по переданному идентификатору,
     * у объявления, найденного по переданному идентификатору. <br>
     * {@link CommentRepository#findById(Object)}
     * {@link CommentRepository#save(Object)}
     * {@link AdService#getById(Integer)}
     * {@link AdService#createdAd(Ad)}
     * {@link ValidationService#isAdmin(Authentication)}
     * {@link ValidationService#isOwner(Authentication, String)}
     * @param adId идентификатор объявления
     * @param commentId идентификатор комментария
     * @param comment новый комментарий
     * @param authentication
     * @return обновленный комментарий в формате {@link CommentDto}
     */
    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateCommentDto comment,
                                    Authentication authentication) {
        Comment commentToUpdate = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        Ad adToUpdate = adService.getById(adId);
        String commentAuthorName = commentToUpdate.getAuthor().getEmail();
        if (ValidationService.isAdmin(authentication) || ValidationService.isOwner(authentication, commentAuthorName)) {
            adToUpdate.getComments().remove(commentToUpdate);
            Optional.ofNullable(comment.getText()).ifPresent(commentToUpdate::setText);
            adToUpdate.getComments().add(commentToUpdate);
            commentRepository.save(commentToUpdate);
            adService.createdAd(adToUpdate);
            return CommentMapper.mapIntoCommentDto(commentToUpdate);
        } else {
            throw new AccessDeniedException();
        }
    }
}