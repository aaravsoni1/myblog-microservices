package com.microservices.comment.service;

import com.microservices.comment.config.RestTemplateConfig;
import com.microservices.comment.entity.Comment;
import com.microservices.comment.payload.Post;
import com.microservices.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    private RestTemplateConfig restTemplate;

    public CommentService(CommentRepository commentRepository, RestTemplateConfig restTemplate) {
        this.commentRepository = commentRepository;
        this.restTemplate = restTemplate;
    }

    public Comment saveComment(Comment comment){
        Post post =restTemplate.getRestTemplate().getForObject("http://POST-SERVICE/api/v1/posts/"+comment.getPostId(), Post.class);

        if(post!=null){
            Comment SavedComment= commentRepository.save(comment);
            return  SavedComment;
         }else{
            return null;
        }

    }


    public List<Comment> getAllCommentsBypostId(String postId) {
         List<Comment> comments = commentRepository.findByPostId(postId);
         return comments;
     }
}
