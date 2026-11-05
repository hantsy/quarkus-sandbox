package com.example.service;

import com.example.domain.Comment;
import com.example.domain.Post;
import com.example.domain.PostSummary;
import com.example.domain.Status;
import jakarta.data.Limit;
import jakarta.data.Order;
import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import jakarta.data.repository.*;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.processing.Pattern;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface Blogger {

    @Query("""
            SELECT p.id, p.title, size(c) AS summary FROM Post p LEFT JOIN p.comments c
            WHERE p.title LIKE :title
                OR p.content LIKE :title
                OR c.content LIKE :title
            GROUP BY p
            ORDER BY p.createdAt DESC
            """)
    Page<PostSummary> allPosts(@Param("title") String title, PageRequest page);

    @Find
    @OrderBy("createdAt")
    Page<Post> byTitle(@Pattern String title, PageRequest page);

    @Find
    List<Post> byStatus(Status status, Order<Post> order,  Limit limit);

    @Find
    Optional<Post> byId(UUID id);

    @Insert
    @Transactional
    Post insert(Post post);

    @Insert
    @Transactional
    Comment insert(Comment comment);

    @Update
    @Transactional
    Post update(Post post);

    @Delete
    @Transactional
    void delete(Post post);

    // see: https://hibernate.zulipchat.com/#narrow/stream/132096-hibernate-user/topic/Jakarta.20Data.20cascade.20does.20not.20work.20in.20custom.20deletion.20Query/near/441874793
    @Query("delete from Post")
    @Transactional
    long deleteAllPosts();

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId")
    List<Comment> getCommentsOfPost(UUID postId);
}
