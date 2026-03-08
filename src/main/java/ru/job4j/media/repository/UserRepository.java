package ru.job4j.media.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.media.entity.User;

public interface UserRepository extends ListCrudRepository<User, Long> {
}
