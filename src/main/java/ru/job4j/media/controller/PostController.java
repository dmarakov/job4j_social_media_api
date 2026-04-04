package ru.job4j.media.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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

@Tag(name = "PostController", description = "PostController management APIs")
@AllArgsConstructor
@RestController
@Validated
@RequestMapping("api/post")
public class PostController {

    private final PostService postService;

    @Operation(
        summary = "Retrieve a Post by postId",
        description = "Get a Post object by specifying its postId. The response is Post object with postId, text, summary, user, files and date of created.",
        tags = { "User", "get" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Post.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{postId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Post> get(@PathVariable @Nonnull Long postId) {
        return postService.findById(postId)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Create a new Post",
        description = "Create a new Post object. Returns the created Post with generated id and location header.",
        tags = { "Post", "POST" })
    @ApiResponses({
        @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = Post.class), mediaType = "application/json")
        }),
        @ApiResponse(responseCode = "400", content = { @Content })
    })
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
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

    @Operation(
        summary = "Update an existing Post",
        description = "Update a Post object completely. The request must contain full Post data.",
        tags = { "Post", "PUT" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Post updated successfully"),
        @ApiResponse(responseCode = "404", description = "Post not found"),
        @ApiResponse(responseCode = "400", content = { @Content })
    })
    @PutMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Post> update(@RequestBody Post post) {
        if (postService.update(post)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(
        summary = "Partially update a Post",
        description = "Update selected fields of a Post object.",
        tags = { "Post", "PATCH" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Post updated successfully"),
        @ApiResponse(responseCode = "404", description = "Post not found"),
        @ApiResponse(responseCode = "400", content = { @Content })
    })
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> change(@RequestBody Post post) {
        if (postService.update(post)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(
        summary = "Delete a Post by postId",
        description = "Delete a Post by specifying its postId.",
        tags = { "Post", "DELETE" })
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Post not found"),
        @ApiResponse(responseCode = "400", content = { @Content })
    })
    @DeleteMapping("/{postId}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> removeById(@PathVariable long postId) {
        if (postService.deleteById(postId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(
        summary = "Get posts grouped by userIds",
        description = "Retrieve a list of users with their posts by providing a list of userIds. Returns UserPostDto containing userId, username and list of posts.",
        tags = { "Post", "GET" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = UserPostDto.class), mediaType = "application/json")
        }),
        @ApiResponse(responseCode = "400", content = { @Content })
    })
    @GetMapping("/posts/byuser")
    @PostAuthorize("hasRole('ADMIN')")
    public List<UserPostDto> getPostsByUsersId(
        @RequestParam
        @NotEmpty(message = "Список userIds не должен быть пустым")
        List<Long> userIds) {
        return postService.getPostsByUsersId(userIds);
    }
}
