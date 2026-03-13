package ru.job4j.media.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import ru.job4j.media.entity.Post;
import ru.job4j.media.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends ListCrudRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {
    List<Post> findByUser(User user);

    List<Post> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("""
        UPDATE Post post SET post.summary = :summary, post.text = :text
        WHERE post.id = :id
        """)
    int updateTextAndSummary(@Param("summary") String summary, @Param("text") String text, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = """
        DELETE FROM post_files
                WHERE post_id = :post_id
        """, nativeQuery = true)
    int deleteAttachedFilesFromPost(@Param("post_id") Long postId);

    @Modifying(clearAutomatically = true)
    @Query(value = """
        DELETE FROM Post post where post.id = :post_id
        """)
    int deletePostByPostId(@Param("post_id") Long postId);

    @Query(value = """
        SELECT post from Post as post
        WHERE post.user in (
        SELECT ur.fromUser
               from UserRelation ur
               where ur.toUser.id = :userId
        )
        ORDER BY post.createdAt DESC
        """)
    Page<Post> findPostsOfSubscribers(@Param("userId") Long userId, Pageable pageable);
}
