package ru.job4j.media.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.media.entity.Post;

public interface PostRepository extends ListCrudRepository<Post, Long> {
}
