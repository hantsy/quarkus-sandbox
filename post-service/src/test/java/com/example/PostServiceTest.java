package com.example;

import com.example.domain.Comment;
import com.example.domain.Post;
import com.example.domain.PostId;
import com.example.repository.CommentRepository;
import com.example.repository.PostRepository;
import com.example.service.PostService;
import com.example.web.CreateCommentCommand;
import com.example.web.PostNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.enterprise.event.Event;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PostServiceTest {
    
    @ExtendWith(MockitoExtension.class)
    @Nested
    class NestedTest1 {
        
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
            when(postRepository.findByIdOptional(anyString()))
                    .thenReturn(Optional.of(Post.builder().id("testid").title("test").content("test content").build()));
            when(commentRepository.save(any(Comment.class)))
                    .thenReturn(Comment.builder().post(new PostId("testid")).content("test comment").createdAt(LocalDateTime.now()).build());
            doNothing().when(commentEvent).fire(any(Comment.class));
            
            service.addComment("testid", CreateCommentCommand.of("test comment"));
            verify(postRepository, times(1)).findByIdOptional(anyString());
            verify(commentRepository, times(1)).save(any(Comment.class));
            verify(commentEvent, times(1)).fire(any(Comment.class));
            
            verifyNoMoreInteractions(postRepository, commentRepository, commentEvent);
        }
        
        @Test
        public void testCommentAddedPostNotExisted() {
            when(postRepository.findByIdOptional(anyString()))
                    .thenReturn(Optional.ofNullable(null));
            
            assertThrows(PostNotFoundException.class, () -> {
                service.addComment("testid", CreateCommentCommand.of("test comment"));
            });
            
            verify(postRepository, times(1)).findByIdOptional(anyString());
            
            verifyNoInteractions(commentEvent, commentRepository);
            verifyNoMoreInteractions(postRepository);
        }
    }
    
    @Nested
    class NestedTest2 {
        
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
            when(postRepository.findByIdOptional(anyString()))
                    .thenReturn(Optional.of(Post.builder().id("testid").title("test").content("test content").build()));
            when(commentRepository.save(any(Comment.class)))
                    .thenReturn(Comment.builder().post(new PostId("testid")).content("test comment").createdAt(LocalDateTime.now()).build());
            doNothing().when(commentEvent).fire(any(Comment.class));
            
            service.addComment("testid", CreateCommentCommand.of("test comment"));
            verify(postRepository, times(1)).findByIdOptional(anyString());
            verify(commentRepository, times(1)).save(any(Comment.class));
            verify(commentEvent, times(1)).fire(any(Comment.class));
            
            verifyNoMoreInteractions(postRepository, commentRepository, commentEvent);
        }
        
        @Test
        public void testCommentAddedPostNotExisted() {
            when(postRepository.findByIdOptional(anyString()))
                    .thenReturn(Optional.ofNullable(null));
            
            assertThrows(PostNotFoundException.class, () -> {
                service.addComment("testid", CreateCommentCommand.of("test comment"));
            });
            
            verify(postRepository, times(1)).findByIdOptional(anyString());
            
            verifyNoInteractions(commentEvent, commentRepository);
            verifyNoMoreInteractions(postRepository);
        }
    }
}