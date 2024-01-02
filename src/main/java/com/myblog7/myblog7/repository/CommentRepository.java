package com.myblog7.myblog7.repository;

import com.myblog7.myblog7.entity.Comment;
import com.myblog7.myblog7.entity.Post;
import com.myblog7.myblog7.payload.CommentDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<CommentDto> findByPostId(Post postId);


    List<Comment> findByPost(Post post);
}
