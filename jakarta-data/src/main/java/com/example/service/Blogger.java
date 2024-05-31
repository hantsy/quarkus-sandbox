package com.example.service;

import com.example.domain.Comment;
import com.example.domain.Post;
import com.example.domain.PostSummary;
import com.example.domain.Status;
import com.example.web.PostNotFoundException;
import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import jakarta.data.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.StatelessSession;
import org.hibernate.annotations.processing.Pattern;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface Blogger {

    StatelessSession session();

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
    Page<Post> byTitle(@Pattern String title, PageRequest page);

    @Find
    @OrderBy("createdAt")
    List<Post> byStatus(Status status);

    @Find
    Optional<Post> byId(UUID id);

    @Insert
    Post insert(Post post);

    @Update
    Post update(Post post);

    @Delete
    void delete(Post post);

    // see: https://hibernate.zulipchat.com/#narrow/stream/132096-hibernate-user/topic/Jakarta.20Data.20cascade.20does.20not.20work.20in.20custom.20deletion.20Query/near/441874793
    @Query("delete from Post")
    long deleteAllPosts();

    default List<Comment> getCommentsOfPost(@NotEmpty UUID postId) {
        var post = this.byId(postId).orElseThrow(() -> new PostNotFoundException(postId));
        session().fetch(post.getComments());
        return post.getComments();
    }
}
