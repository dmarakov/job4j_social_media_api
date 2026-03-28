package ru.job4j.media.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ru.job4j.media.repository.MessageRepository;
import ru.job4j.media.repository.PostRepository;
import ru.job4j.media.repository.UserRelationRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class PostTest {

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
    }

    @Test
    public void whenSavePost_thenFindById() {

        Post post = new Post(null, "Text", "Summary", LocalDateTime.now(), null);
        postRepository.save(post);

        var foundedPost = postRepository.findById(post.getId());
        assertThat(foundedPost).isPresent();
    }

    @Test
    public void whenFindAll_thenReturnAllPosts() {

        Post post1 = new Post(null, "Text1", "Summary1", LocalDateTime.now(), null);
        Post post2 = new Post(null, "Text2", "Summary2", LocalDateTime.now(), null);

        postRepository.save(post1);
        postRepository.save(post2);

        var posts = postRepository.findAll();

        assertThat(posts).hasSize(2);
        assertThat(posts).extracting(Post::getText).contains("Text1", "Text2");
    }


    @Test
    public void whenFindAllBetweenDays_thenReturnAllBetweenDays() {
        LocalDateTime from = LocalDateTime.of(2026, 3, 1, 10, 0, 0);
        LocalDateTime to = from.plusDays(1);

        Post post1 = new Post(null, "Text1", "Summary1", from.plusHours(1), null);
        Post post2 = new Post(null, "Text2", "Summary2", from.plusDays(10), null);

        postRepository.save(post1);
        postRepository.save(post2);

        var posts = postRepository.findByCreatedAtBetween(from, to);

        assertThat(posts).hasSize(1);
        assertThat(posts).extracting(Post::getText).contains("Text1");
    }

    @Test
    public void whenFindAllByOrderByCreatedAtDesc_thenSortedAndPaged() {

        Post post1 = new Post(null, "Text1", "Summary1",
            LocalDateTime.of(2024, 1, 1, 10, 0), null);

        Post post2 = new Post(null, "Text2", "Summary2",
            LocalDateTime.of(2024, 2, 1, 10, 0), null);

        Post post3 = new Post(null, "Text3", "Summary3",
            LocalDateTime.of(2024, 3, 1, 10, 0), null);

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