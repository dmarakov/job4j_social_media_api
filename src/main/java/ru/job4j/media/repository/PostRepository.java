package ru.job4j.media.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.job4j.media.entity.Post;
import ru.job4j.media.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends ListCrudRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {
    List<Post> findByUser(User user);

    List<Post> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
