package ru.job4j.media.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.media.entity.Message;

public interface MessageRepository extends ListCrudRepository<Message, Long> {
}
