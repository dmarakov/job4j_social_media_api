package ru.job4j.media.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.media.entity.File;

public interface FileRepository extends ListCrudRepository<File, Long> {
}
