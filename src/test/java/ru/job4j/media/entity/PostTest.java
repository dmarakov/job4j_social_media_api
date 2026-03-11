package ru.job4j.media.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
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

    @Test
    public void whenFindAllByUser_thenReturnAllUserPosts() {
        User user = new User(null, "User", "user@email.com", "password");
        User user2 = new User(null, "User2", "user2@email.com", "password");
        userRepository.save(user);
        userRepository.save(user2);

        Post post1 = new Post(null, "Text1", "Summary1", LocalDateTime.now(), user, null);
        Post post2 = new Post(null, "Text2", "Summary2", LocalDateTime.now(), user2, null);

        postRepository.save(post1);
        postRepository.save(post2);

        var posts = postRepository.findByUser(user);

        assertThat(posts).hasSize(1);
        assertThat(posts).extracting(Post::getText).contains("Text1");
    }

    @Test
    public void whenFindAllBetweenDays_thenReturnAllBetweenDays() {
        LocalDateTime from = LocalDateTime.of(2026, 3, 1, 10, 0, 0);
        LocalDateTime to = from.plusDays(1);

        User user = new User(null, "User", "user@email.com", "password");
        User user2 = new User(null, "User2", "user2@email.com", "password");
        user = userRepository.save(user);
        user2 = userRepository.save(user2);

        Post post1 = new Post(null, "Text1", "Summary1", from.plusHours(1), user, null);
        Post post2 = new Post(null, "Text2", "Summary2", from.plusDays(10), user2, null);

        postRepository.save(post1);
        postRepository.save(post2);

        var posts = postRepository.findByCreatedAtBetween(from, to);

        assertThat(posts).hasSize(1);
        assertThat(posts).extracting(Post::getText).contains("Text1");
    }

    @Test
    public void whenFindAllByOrderByCreatedAtDesc_thenSortedAndPaged() {
        User user = new User(null, "User", "user@email.com", "password");
        user = userRepository.save(user);

        Post post1 = new Post(null, "Text1", "Summary1",
            LocalDateTime.of(2024, 1, 1, 10, 0), user, null);

        Post post2 = new Post(null, "Text2", "Summary2",
            LocalDateTime.of(2024, 2, 1, 10, 0), user, null);

        Post post3 = new Post(null, "Text3", "Summary3",
            LocalDateTime.of(2024, 3, 1, 10, 0), user, null);

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        var page = postRepository.findAllByOrderByCreatedAtDesc(
            PageRequest.of(0, 2)
        );

        var posts = page.getContent();

        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getText()).isEqualTo("Text3");
        assertThat(posts.get(1).getText()).isEqualTo("Text2");
    }
}