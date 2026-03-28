package ru.job4j.media.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.media.repository.MessageRepository;
import ru.job4j.media.repository.PostRepository;
import ru.job4j.media.repository.UserRelationRepository;
import ru.job4j.media.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class MessageTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRelationRepository userRelationRepository;
    List<Post> posts = new ArrayList<>();

    @BeforeEach
    void setup() {
        messageRepository.deleteAll();
        postRepository.deleteAll();
        userRelationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whensSaveFile_thenFindById() {

        User fromUser = new User(null, "FromUser", "fromUser@email.com", "password", posts);
        User toUser = new User(null, "ToUser", "toser@email.com", "password", posts);
        userRepository.save(fromUser);
        userRepository.save(toUser);
        Message message = new Message(null, fromUser, toUser, "Text", LocalDateTime.now());
        messageRepository.save(message);
        var foundedFile = messageRepository.findById(message.getId());
        assertThat(foundedFile).isPresent();
    }

    @Test
    public void whenFindAll_thenReturnAllPersons() {
        User fromUser = new User(null, "FromUser", "fromUser@email.com", "password", posts);
        User toUser = new User(null, "ToUser", "toser@email.com", "password", posts);
        userRepository.save(fromUser);
        userRepository.save(toUser);
        Message message1 = new Message(null, fromUser, toUser, "Text", LocalDateTime.now());
        Message message2 = new Message(null, fromUser, toUser, "Text2", LocalDateTime.now());
        messageRepository.save(message1);
        messageRepository.save(message2);
        var persons = messageRepository.findAll();
        assertThat(persons).hasSize(2);
        assertThat(persons).extracting(Message::getText).contains("Text", "Text2");
    }
}