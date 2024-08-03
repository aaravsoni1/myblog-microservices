package com.microservices.comment.controller;

import com.microservices.comment.entity.Comment;
import com.microservices.comment.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
   public  ResponseEntity<Comment> saveComment(@RequestBody Comment comment) {
        UUID commentId = UUID.fromString(UUID.randomUUID().toString());
        comment.setCommentId(String.valueOf(commentId));
        Comment c =commentService.saveComment(comment);
        return new ResponseEntity<>(c, HttpStatus.CREATED);
    }

    @GetMapping("{postId}")
    public List<Comment> getAllCommentsBypostId(@PathVariable String postId){
       List<Comment> comments = commentService.getAllCommentsBypostId(postId);
       return comments;
    }
}
