package com.myblog7.myblog7.service;


import com.myblog7.myblog7.payload.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long postId, CommentDto commentDto);

    CommentDto updateComment(long postId, long commentId, CommentDto commentDto);

    void deleteComment(long postId, long commentId);

    CommentDto getCommentById(long postId, long commentId);

    List<CommentDto> getCommentsByPostId(long postId);
    List<CommentDto> getAllComments();
}

