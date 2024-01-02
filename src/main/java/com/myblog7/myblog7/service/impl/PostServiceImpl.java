package com.myblog7.myblog7.service.impl;

import com.myblog7.myblog7.entity.Post;
import com.myblog7.myblog7.exception.ResourceNotFoundException;
import com.myblog7.myblog7.payload.PostDto;
import com.myblog7.myblog7.payload.PostResponse;
import com.myblog7.myblog7.repository.PostRepository;
import com.myblog7.myblog7.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;


@Service
public class PostServiceImpl  implements PostService {

    private PostRepository postRepository;
    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository,ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper=modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        // convert DTO to entity
        Post post = mapToEntity(postDto);
        Post newPost = postRepository.save(post);

        // convert entity to DTO
        PostDto postResponse = mapToDto(newPost);
        return postResponse;
    }

    @Override
    public void deletePost(long id) {
        postRepository.deleteById(id);

    }

    @Override
    public PostDto updatePost(long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post not found with id:" + id)
        );


        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost= postRepository.save(post);
        return mapToDto(updatedPost);
    }



    @Override
    public PostResponse getAllPost(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Default direction if the provided direction is invalid
        Sort.Direction direction = Sort.Direction.ASC;

        // Validate the provided sort direction
        if ("desc".equalsIgnoreCase(sortDir)) {
            direction = Sort.Direction.DESC;
        } else if (!"asc".equalsIgnoreCase(sortDir)) {
            // Invalid sort direction provided, defaulting to ascending
            System.out.println("Invalid sort direction provided. Defaulting to ascending order.");
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, direction, sortBy);
        Page<Post> pagePosts = postRepository.findAll(pageable);
        List<Post> posts = pagePosts.getContent();
       List<PostDto> postDtos= posts.stream().map(this::mapToDto).collect(Collectors.toList());

        PostResponse postResponse=new PostResponse();
        postResponse.setPostDto(postDtos);
        postResponse.setPageNo(pagePosts.getNumber());
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setLast(pagePosts.isLast());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        return postResponse;
    }



    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post not found with id:" + id)
        );


        return mapToDto(post);
    }

    // convert Entity into DTO
    private PostDto mapToDto(Post post){
        PostDto postDto = modelMapper.map(post, PostDto.class);
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return postDto;
    }

    // convert DTO to entity
    private Post mapToEntity(PostDto postDto){
        Post post = modelMapper.map(postDto,Post.class);
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }
}

