package ru.job4j.media.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.media.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, Long> {

    @Query("""
        SELECT user FROM User AS user
        WHERE user.name = :title and user.passwordHash = :password
        """)
    Optional<User> findUserByNameAndPassword(@Param("name") String title, @Param("passwordHash") String password);

    @Transactional
    @Modifying
    @Query("delete from User u where u.id=:pId")
    int delete(@Param("pId") Long id);


    @Transactional
    @Modifying
    @Query("""
        update User u
        set u.name = :#{#user.name},
        u.passwordHash = :#{#user.passwordHash},
        u.email = :#{#user.email}
        where u.id=:#{#user.id}
        """)
    int update(@Param("user") User user);

    @Transactional
    @Query("""
        select distinct u from User u left join fetch u.userPosts where u.id in :ids
        """) List<User> findUserWithPosts(@Param("ids") List<Long> ids);
}
