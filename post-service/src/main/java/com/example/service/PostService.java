package com.example.service;

import com.example.domain.Comment;
import com.example.domain.PostId;
import com.example.repository.CommentRepository;
import com.example.repository.PostRepository;
import com.example.web.CreateCommentCommand;
import com.example.web.PostNotFoundException;
import lombok.RequiredArgsConstructor;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@ApplicationScoped
@RequiredArgsConstructor
public class PostService {
    final PostRepository postRepository;
    final CommentRepository commentRepository;
    final Event<Comment> commentEvent;
    
    public void addComment(@NotEmpty String postId, @Valid CreateCommentCommand commentForm) {
        this.postRepository.findByIdOptional(postId)
                .ifPresentOrElse(
                        post -> {
                            var comment = Comment.builder().post(new PostId(postId))
                                    .content(commentForm.content())
                                    .build();
                            var saved  = commentRepository.save(comment);
                            commentEvent.fire(saved);
                        },
                        () -> {
                            throw new PostNotFoundException(postId);
                        }
                );
        
    }
}
