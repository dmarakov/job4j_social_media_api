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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostTest {

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
    public void whenSavePost_thenFindById() {
        User user = new User(null, "User", "user@email.com", "password");
        userRepository.save(user);

        Post post = new Post(null, "Text", "Summary", LocalDateTime.now(), user, null);
        postRepository.save(post);

        var foundedPost = postRepository.findById(post.getId());
        assertThat(foundedPost).isPresent();
    }

    @Test
    public void whenFindAll_thenReturnAllPosts() {
        User user = new User(null, "User", "user@email.com", "password");
        userRepository.save(user);

        Post post1 = new Post(null, "Text1", "Summary1", LocalDateTime.now(), user, null);
        Post post2 = new Post(null, "Text2", "Summary2", LocalDateTime.now(), user, null);

        postRepository.save(post1);
        postRepository.save(post2);

        var posts = postRepository.findAll();

        assertThat(posts).hasSize(2);
        assertThat(posts).extracting(Post::getText).contains("Text1", "Text2");
    }
}