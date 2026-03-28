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
public class UserTest {

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
    public void whenSaveUser_thenFindById() {
        List<Post> emptyPosts = new ArrayList<>();
        User user = new User(null, "User", "user@email.com", "password", emptyPosts);

        userRepository.save(user);

        var foundedUser = userRepository.findById(user.getId());

        assertThat(foundedUser).isPresent();
    }

    @Test
    public void whenFindAll_thenReturnAllUsers() {
        List<Post> emptyPosts = new ArrayList<>();
        User user1 = new User(null, "User1", "user1@email.com", "password", emptyPosts);
        User user2 = new User(null, "User2", "user2@email.com", "password", emptyPosts);

        userRepository.save(user1);
        userRepository.save(user2);

        var users = userRepository.findAll();

        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getName).contains("User1", "User2");
    }

    @Test
    public void whenFindAllByUser_thenReturnAllUserPosts() {
        List<Post> emptyPosts = new ArrayList<>();

        Post post1 = new Post(null, "Text1", "Summary1", LocalDateTime.now(), null);
        Post post2 = new Post(null, "Text2", "Summary2", LocalDateTime.now(), null);

        postRepository.save(post1);
        postRepository.save(post2);

        List<Post> userPosts = List.of(post1, post2);

        User user = new User(null, "User", "user@email.com", "password", userPosts);
        User user2 = new User(null, "User2", "user2@email.com", "password", emptyPosts);
        userRepository.save(user);
        userRepository.save(user2);

        List<User> posts = userRepository.findUserWithPosts(List.of(user.getId()));

        assertThat(posts.get(0).getUserPosts()).hasSize(2);
        assertThat(posts.get(0).getUserPosts()).extracting(Post::getText).contains("Text1");
    }
}