package com.example;

import lombok.RequiredArgsConstructor;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.validation.Valid;

@ApplicationScoped
@RequiredArgsConstructor
public class PostService {
    final PostRepository postRepository;
    final CommentRepository commentRepository;
    final Event<Comment> commentEvent;
    
    public void addComment(@Valid PostId postId, @Valid CommentForm commentForm) {
        this.postRepository.findByIdOptional(postId.getId())
                .ifPresentOrElse(
                        post -> {
                            var comment = Comment.builder().post(postId)
                                    .content(commentForm.getContent())
                                    .build();
                            var saved  = commentRepository.save(comment);
                            commentEvent.fire(saved);
                        },
                        () -> {
                            throw new PostNotFoundException(postId.getId());
                        }
                );
        
    }
}
