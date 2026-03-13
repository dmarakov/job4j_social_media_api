package ru.job4j.media.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.media.entity.User;

import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, Long> {

    @Query("""
        SELECT user FROM User AS user
        WHERE user.name = :title and user.passwordHash = :password
        """)
    Optional<User> findUserByNameAndPassword(@Param("name") String title, @Param("passwordHash") String password);
}
