package ru.job4j.media.controller;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.media.entity.Post;
import ru.job4j.media.entity.dto.UserPostDto;
import ru.job4j.media.service.PostService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/post")
public class PostController {

    private final PostService postService;

    @GetMapping("/{postId}")
    public ResponseEntity<Post> get(@PathVariable @Nonnull Long postId) {
        return postService.findById(postId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> save(@RequestBody Post post) {
        postService.save(post);
        var uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(post.getId())
            .toUri();
        return ResponseEntity.status(HttpStatus.CREATED)
            .location(uri)
            .body(post);
    }

    @PutMapping
    public ResponseEntity<Post> update(@RequestBody Post post) {
        if (postService.update(post)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> change(@RequestBody Post post) {
        if (postService.update(post)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> removeById(@PathVariable long postId) {
        if (postService.deleteById(postId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/posts/byuser")
    public List<UserPostDto> getPostsByUsersId(@RequestParam List<Long> userIds) {
        return postService.getPostsByUsersId(userIds);
    }
}
