package ru.job4j.media.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.media.repository.FileRepository;
import ru.job4j.media.repository.MessageRepository;
import ru.job4j.media.repository.PostRepository;
import ru.job4j.media.repository.UserRelationRepository;
import ru.job4j.media.repository.UserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileTest {

    @Autowired
    private FileRepository fileRepository;
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
        fileRepository.deleteAll();
        messageRepository.deleteAll();
        postRepository.deleteAll();
        userRelationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whensSaveFile_thenFindById() {
        File file = new File(null, "test", "/test/test.jpg", LocalDateTime.now());
        fileRepository.save(file);
        var foundedFile = fileRepository.findById(1L);
        assertThat(foundedFile).isPresent();
    }

    @Test
    public void whenFindAll_thenReturnAllPersons() {
        File file1 = new File(null, "test", "/test/test.jpg", LocalDateTime.now());
        file1.setName("Test1");
        File file2 = new File(null, "test", "/test/test.jpg", LocalDateTime.now());
        file2.setName("Test2");
        file2.setPath("/test/test2.jpg");
        fileRepository.save(file1);
        fileRepository.save(file2);
        var persons = fileRepository.findAll();
        assertThat(persons).hasSize(2);
        assertThat(persons).extracting(File::getName).contains("Test1", "Test2");
    }
}