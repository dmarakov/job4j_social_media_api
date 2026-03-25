package ru.job4j.media.entity.dto;

import ru.job4j.media.entity.Post;

import java.util.List;

public record UserPostDto(Long userId, String username, List<Post> userPostsList) {
}
