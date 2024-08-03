package com.microservices.post.controller;


import com.microservices.post.entity.Post;
import com.microservices.post.payload.PostDto;
import com.microservices.post.service.PostService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }
    @PostMapping("addpost")
    public ResponseEntity<Post> savePost(@RequestBody Post post) {
        Post newPost = postService.savePost(post);
        return  new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @GetMapping("{postId}")
    public Post getPostById(@PathVariable String postId) {
        Post post = postService.findPostById(postId);
        return post;
    }

    @GetMapping("{postId}/comments")
    @CircuitBreaker(name="commentBreaker", fallbackMethod="commentFallback")
    public ResponseEntity<PostDto> getPostWithComments(@PathVariable String postId){
      PostDto postDto = postService.getPostWithComments(postId);
     return new ResponseEntity<>(postDto,HttpStatus.OK);
    }
    public ResponseEntity<?> commentFallback(@PathVariable String postId, Throwable ex){
        System.out.println("Fallback is executed because service is down" + ex.getMessage());
        ex.printStackTrace();
        PostDto dto = new PostDto();
        dto.setPostId("123");
        dto.setTitle("Service down");
        dto.setContent("Service is down, unable to retrieve comments.");
        dto.setDescription("Servie down");
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}
