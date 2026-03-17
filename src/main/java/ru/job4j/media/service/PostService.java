package ru.job4j.media.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.media.entity.File;
import ru.job4j.media.entity.Post;
import ru.job4j.media.entity.User;
import ru.job4j.media.repository.FileRepository;
import ru.job4j.media.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FileRepository fileRepository;

    @Transactional
    public Post createNewPost(User user, String text, String summary, List<File> files) {

        List<File> savedFile = files == null
            ? new ArrayList<>()
            : fileRepository.saveAll(files);

        Post post = Post.builder()
            .text(text)
            .summary(summary)
            .createdAt(LocalDateTime.now())
            .user(user)
            .files(new ArrayList<>(savedFile))
            .build();
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Post newPost) {
        Post post = postRepository.findById(newPost.getId())
            .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        post.setText(newPost.getText());
        post.setSummary(newPost.getSummary());
        post.setUser(newPost.getUser());

        List<File> newPostFiles = newPost.getFiles();
        List<File> savedFile = newPostFiles == null
            ? new ArrayList<>()
            : fileRepository.saveAll(newPostFiles);

        post.setFiles(new ArrayList<>(savedFile));

        return post;
    }
}
