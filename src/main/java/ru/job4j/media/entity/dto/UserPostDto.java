package ru.job4j.media.entity.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import ru.job4j.media.entity.Post;

import java.util.List;

public record UserPostDto(
    Long userId,
    @NotBlank(message = "username не может быть пустым")
    @Length(min = 6,
        max = 10,
        message = "username должен быть не менее 6 и не более 10 символов")
    String username,
    List<Post> userPostsList) {
}
