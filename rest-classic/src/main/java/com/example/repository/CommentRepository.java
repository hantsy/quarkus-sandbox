package com.example.repository;

import com.example.domain.Comment;
import com.example.domain.PostId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CommentRepository implements PanacheRepositoryBase<Comment, UUID> {


    @Transactional
    public int update(Comment comment) {
        return this.update("content =?1 where id=?2", comment.getContent(), comment.getId());
    }

    public List<Comment> allByPostId(UUID id) {
        return this.list("post", new PostId(id));
    }
}
