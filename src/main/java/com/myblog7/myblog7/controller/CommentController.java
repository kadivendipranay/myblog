package com.myblog7.myblog7.controller;

import com.myblog7.myblog7.payload.CommentDto;
import com.myblog7.myblog7.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
//localhost:8080/api/posts/1/1
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable long postId, @RequestBody CommentDto commentDto) {
        CommentDto createdComment = commentService.createComment(postId, commentDto);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }
//localhost:8080/api/posts/1/comments/1
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable long postId,
            @PathVariable long commentId,
            @RequestBody CommentDto commentDto
    ) {
        CommentDto updatedComment = commentService.updateComment(postId, commentId, commentDto);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }
//localhost:8080/api/posts/1/comments/3
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable long postId,
            @PathVariable long commentId
    ) {
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("Comment is deleted",HttpStatus.OK);
    }
//localhost:8080/api/posts/1/comments/1
    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(
            @PathVariable long postId,
            @PathVariable long commentId
    ) {
        CommentDto comment = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }
    //localhost:8080/api/posts/comments
    @GetMapping("/comments")
    public ResponseEntity<List<CommentDto>> getAllComments() {
        List<CommentDto> comments = commentService.getAllComments();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable long postId) {
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
