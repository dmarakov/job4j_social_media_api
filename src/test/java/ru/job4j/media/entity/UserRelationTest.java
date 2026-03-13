package ru.job4j.media.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.media.repository.MessageRepository;
import ru.job4j.media.repository.PostRepository;
import ru.job4j.media.repository.UserRelationRepository;
import ru.job4j.media.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRelationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRelationRepository userRelationRepository;

    @BeforeEach
    void setup() {
        messageRepository.deleteAll();
        postRepository.deleteAll();
        userRelationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenSaveRelation_thenFindById() {
        User fromUser = new User(null, "FromUser", "from@email.com", "password");
        User toUser = new User(null, "ToUser", "to@email.com", "password");

        userRepository.save(fromUser);
        userRepository.save(toUser);

        UserRelation relation = new UserRelation(null, fromUser, toUser, true, false);

        userRelationRepository.save(relation);

        var foundedRelation = userRelationRepository.findById(relation.getId());

        assertThat(foundedRelation).isPresent();
    }

    @Test
    public void whenFindAll_thenReturnAllRelations() {
        User fromUser = new User(null, "FromUser", "from@email.com", "password");
        User toUser = new User(null, "ToUser", "to@email.com", "password");

        userRepository.save(fromUser);
        userRepository.save(toUser);

        UserRelation relation1 = new UserRelation(null, fromUser, toUser, true, false);
        UserRelation relation2 = new UserRelation(null, toUser, fromUser, true, true);

        userRelationRepository.save(relation1);
        userRelationRepository.save(relation2);

        var relations = userRelationRepository.findAll();

        assertThat(relations).hasSize(2);
    }

    @Test
    public void whenFindAllUserSubscriber_thenReturnListOfSubscribers() {
        User user1 = new User(null, "user1", "from@email.com", "password");
        User user2 = new User(null, "user2", "to1@email.com", "password");
        User user3 = new User(null, "user3", "to2@email.com", "password");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        UserRelation relation1 = new UserRelation(null, user1, user2, true, true);
        UserRelation relation2 = new UserRelation(null, user2, user1, true, true);
        UserRelation relation3 = new UserRelation(null, user3, user1, true, false);

        userRelationRepository.save(relation1);
        userRelationRepository.save(relation2);
        userRelationRepository.save(relation3);

        var fromUserSubscribers = userRelationRepository.findSubscribers(user1);
        assertThat(fromUserSubscribers).hasSize(2);
    }

    @Test
    public void whenFindAllUserFriends_thenReturnListOfFriends() {
        User user1 = new User(null, "user1", "from@email.com", "password");
        User user2 = new User(null, "user2", "to1@email.com", "password");
        User user3 = new User(null, "user3", "to2@email.com", "password");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        UserRelation relation1 = new UserRelation(null, user1, user2, true, true);
        UserRelation relation2 = new UserRelation(null, user2, user1, true, true);
        UserRelation relation3 = new UserRelation(null, user3, user1, true, false);

        userRelationRepository.save(relation1);
        userRelationRepository.save(relation2);
        userRelationRepository.save(relation3);

        var fromUserSubscribers = userRelationRepository.findUserRelationFriendsByFromUser(user1);
        assertThat(fromUserSubscribers).hasSize(1);
    }
}