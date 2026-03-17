package ru.job4j.media.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.media.entity.User;
import ru.job4j.media.entity.UserRelation;

import java.util.List;
import java.util.Optional;

public interface UserRelationRepository extends ListCrudRepository<UserRelation, Long> {
    @Query("""
        SELECT ur.fromUser FROM UserRelation ur
        WHERE ur.toUser = :user
        """)
    List<User> findSubscribers(@Param("user") User user);

    @Query("""
        SELECT ur.fromUser FROM UserRelation ur
        WHERE ur.toUser = :user and ur.friend = true
        """)
    List<User> findUserRelationFriendsByFromUser(@Param("user") User user);

    Optional<UserRelation> findByFromUserAndToUser(User fromUser, User toUser);

}
