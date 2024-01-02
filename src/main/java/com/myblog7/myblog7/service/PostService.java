package com.myblog7.myblog7.service;

import com.myblog7.myblog7.payload.PostDto;
import com.myblog7.myblog7.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);

    void deletePost(long id);

    PostDto updatePost(long id, PostDto postDto);



    PostDto getPostById(long id);






    PostResponse getAllPost(int pageNo, int pageSize, String sortBy, String sortDir);
}

