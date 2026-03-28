package ru.job4j.media.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.media.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends ListCrudRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {

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
    @Transactional
    @Query(value = """
        DELETE FROM Post post where post.id = :post_id
        """)
    int deletePostByPostId(@Param("post_id") Long postId);

    @Query("""
    SELECT post
    FROM UserRelation ur
    JOIN ur.fromUser u
    JOIN u.userPosts post
    WHERE ur.toUser.id = :userId
    ORDER BY post.createdAt DESC
""")
    Page<Post> findPostsOfSubscribers(@Param("userId") Long userId, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("""
    UPDATE Post p
    SET p.text = :#{#post.text},
        p.summary = :#{#post.summary},
        p.createdAt = :#{#post.createdAt}
    WHERE p.id = :#{#post.id}
    """)
    int update(@Param("post") Post post);
}
