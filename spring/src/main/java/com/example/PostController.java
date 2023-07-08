package com.example;

import org.jboss.resteasy.spi.ResteasyUriBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final static Logger LOG = Logger.getLogger(PostController.class.getName());
    private PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

  /*  @GetMapping()
    public ResponseEntity getAllPosts() {
        List<Post> posts = this.postRepository.findAll();
        return ok(posts);
    }*/

    @GetMapping()
    public ResponseEntity getAllPosts(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        Pageable pageRequest= PageRequest.of(page, size );
        Page<Post> posts = this.postRepository.findAll(pageRequest);
        return ok(posts);
    }

    @GetMapping("search")
    public ResponseEntity searchByKeyword(
            @RequestParam(value = "q", required = false) String keyword,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit
    ) {

        List<Post> posts = this.postRepository.findByKeyword(keyword, offset, limit);
        LOG.log(Level.INFO, "post search by keyword:" + posts);
        return ok(posts);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Post> getPost(@PathVariable("id") UUID id) {

        Post post = this.postRepository.findById(id).orElseThrow(
                () -> new PostNotFoundException(id)
        );

        return ok(post);
    }

    @PostMapping()
    public ResponseEntity<Void> createPost(@RequestBody @Valid CreatePostCommand post) {
        Post data = Post.of(post.getTitle(), post.getContent());
        Post saved = this.postRepository.save(data);
//        URI createdUri = UriComponentsBuilder.fromPath("/posts/{id}")
//                .buildAndExpand(saved.getId())
//                .toUri();

        return created(ResteasyUriBuilder.fromTemplate("/posts/{id}").build(saved.getId()).normalize()).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable("id") UUID id, @RequestBody @Valid CreatePostCommand form) {
        Post post = this.postRepository.findById(id).orElseThrow(
                () -> new PostNotFoundException(id)
        );
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        this.postRepository.save(post);

        return noContent().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable("id") UUID id) {
        this.postRepository.deleteById(id);
        return noContent().build();
    }

}
