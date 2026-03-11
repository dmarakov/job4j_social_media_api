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
        UserRelation relation2 = new UserRelation(null, toUser, fromUser, false, true);

        userRelationRepository.save(relation1);
        userRelationRepository.save(relation2);

        var relations = userRelationRepository.findAll();

        assertThat(relations).hasSize(2);
    }
}