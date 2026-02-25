package com.example;

import com.example.domain.Comment;
import com.example.domain.Post;
import com.example.repository.CommentRepository;
import com.example.repository.PostRepository;
import com.example.service.PostService;
import com.example.web.CreateCommentCommand;
import com.example.web.PostNotFoundException;
import io.quarkus.deployment.annotations.Produce;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @ExtendWith(MockitoExtension.class)
    @Nested
    class WithMockAnnotations {

        @InjectMocks
        private PostService service;

        @Mock
        private CommentRepository commentRepository;

        @Mock
        private PostRepository postRepository;

        @Mock
        private Event<Comment> commentEvent;

        @BeforeEach
        public void setUp() {
        }

        @Test
        public void testCommentAdded() {
            UUID id = UUID.randomUUID();
            when(postRepository.findByIdOptional(any(UUID.class)))
                    .thenReturn(Optional.of(Post.builder().id(id).title("test").content("test content").build()));
            doAnswer(invocation -> {
                var p = (Comment) (invocation.getArguments()[0]);
                p.setId(id);
                return null;
            }). when(commentRepository).persist(any(Comment.class));

            service.addComment(UUID.randomUUID(), CreateCommentCommand.of("test comment"));
            verify(postRepository, times(1)).findByIdOptional(any(UUID.class));
            verify(commentRepository, times(1)).persist(any(Comment.class));
            verify(commentEvent, times(1)).fire(any(Comment.class));

            verifyNoMoreInteractions(postRepository, commentRepository, commentEvent);
        }

        @Test
        public void testCommentAddedPostNotExisted() {
            when(postRepository.findByIdOptional(any(UUID.class)))
                    .thenReturn(Optional.ofNullable(null));

            assertThrows(PostNotFoundException.class, () -> {
                service.addComment(UUID.randomUUID(), CreateCommentCommand.of("test comment"));
            });

            verify(postRepository, times(1)).findByIdOptional(any(UUID.class));

            verifyNoInteractions(commentEvent, commentRepository);
            verifyNoMoreInteractions(postRepository);
        }
    }

    @Nested
    class WithManualMocks {

        private PostService service;

        private final CommentRepository commentRepository = mock(CommentRepository.class);

        private final PostRepository postRepository = mock(PostRepository.class);

        private final Event<Comment> commentEvent = (Event<Comment>) mock(Event.class);

        @BeforeEach
        public void setUp() {
            service = new PostService(postRepository, commentRepository, commentEvent);
        }

        @AfterEach
        public void tearDown() {
            Mockito.reset(postRepository, commentRepository, commentEvent);
        }

        @Test
        public void testCommentAdded() {
            UUID id = UUID.randomUUID();
            when(postRepository.findByIdOptional(any(UUID.class)))
                    .thenReturn(Optional.of(Post.builder().id(id).title("test").content("test content").build()));

            doAnswer(invocation -> {
                var p = (Comment) (invocation.getArguments()[0]);
                p.setId(id);
                return null;
            }).when(commentRepository).persist(any(Comment.class));

            doNothing().when(commentEvent).fire(any(Comment.class));

            service.addComment(UUID.randomUUID(), CreateCommentCommand.of("test comment"));
            verify(postRepository, times(1)).findByIdOptional(any(UUID.class));
            verify(commentRepository, times(1)).persist(any(Comment.class));
            verify(commentEvent, times(1)).fire(any(Comment.class));

            verifyNoMoreInteractions(postRepository, commentRepository, commentEvent);
        }

        @Test
        public void testCommentAddedPostNotExisted() {
            when(postRepository.findByIdOptional(any(UUID.class)))
                    .thenReturn(Optional.ofNullable(null));

            assertThrows(PostNotFoundException.class, () -> {
                service.addComment(UUID.randomUUID(), CreateCommentCommand.of("test comment"));
            });

            verify(postRepository, times(1)).findByIdOptional(any(UUID.class));

            verifyNoInteractions(commentEvent, commentRepository);
            verifyNoMoreInteractions(postRepository);
        }
    }
}