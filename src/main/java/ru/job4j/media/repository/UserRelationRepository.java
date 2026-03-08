package ru.job4j.media.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.media.entity.UserRelation;

public interface UserRelationRepository extends ListCrudRepository<UserRelation, Long> {
}
