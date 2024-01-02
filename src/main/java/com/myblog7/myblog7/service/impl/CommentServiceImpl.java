package com.myblog7.myblog7.service.impl;

import com.myblog7.myblog7.entity.Comment;
import com.myblog7.myblog7.entity.Post;
import com.myblog7.myblog7.exception.ResourceNotFoundException;
import com.myblog7.myblog7.payload.CommentDto;
import com.myblog7.myblog7.repository.CommentRepository;
import com.myblog7.myblog7.repository.PostRepository;
import com.myblog7.myblog7.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(PostRepository postRepository, CommentRepository commentRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);
        return modelMapper.map(savedComment, CommentDto.class);
    }

    @Override
    @Transactional
    public CommentDto updateComment(long postId, long commentId, CommentDto commentDto) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (existingComment.getPost().getId() != postId) {
            throw new IllegalArgumentException("Comment with id " + commentId + " does not belong to post with id " + postId);
        }

        existingComment.setName(commentDto.getName());
        existingComment.setEmail(commentDto.getEmail());
        existingComment.setBody(commentDto.getBody());

        Comment updatedComment = commentRepository.save(existingComment);
        return modelMapper.map(updatedComment, CommentDto.class);
    }

    @Override
    @Transactional
    public void deleteComment(long postId, long commentId) {
        Comment commentToDelete = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (commentToDelete.getPost().getId() != postId) {
            throw new IllegalArgumentException("Comment with id " + commentId + " does not belong to post with id " + postId);
        }

        commentRepository.delete(commentToDelete);
    }

    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (comment.getPost().getId() != postId) {
            throw new IllegalArgumentException("Comment with id " + commentId + " does not belong to post with id " + postId);
        }

        return modelMapper.map(comment, CommentDto.class);
    }
    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        List<Comment> comments = commentRepository.findByPost(post);

        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getAllComments() {
        List<Comment> comments = commentRepository.findAll();

        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .collect(Collectors.toList());
    }
}

