package com.myblog7.myblog7.controller;


import com.myblog7.myblog7.payload.PostDto;
import com.myblog7.myblog7.payload.PostResponse;
import com.myblog7.myblog7.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //http://localhost:8080/api/post
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postDto, BindingResult result) {
        if(result.hasErrors()){
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //  PostDto dto=postService.createPost(postDto);
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);

    }

    //http://localhost:8080/api/post/1
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable("id") long id) {
        postService.deletePost(id);
        return new ResponseEntity<>("Post is deleted", HttpStatus.OK);
    }

    //http://localhost:8080/api/post/1
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable("id") long id, @RequestBody PostDto postDto) {
        PostDto dto =postService.updatePost(id, postDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);


    }
    //http://localhost:8080/api/post?pageNo=1&pageSize=3&sortBy=title&sortDir=desc
    @GetMapping
    public PostResponse getAllPost(@RequestParam(value="pageNo", defaultValue="0", required=false)int pageNo,
                                   @RequestParam(value="pageSize", defaultValue="5", required=false)int pageSize,
                                   @RequestParam(value="sortBy", defaultValue="id", required=false)String sortBy,
                                   @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
                                    ){
         PostResponse postResponse = postService.getAllPost(pageNo, pageSize, sortBy, sortDir);
        return postResponse;
    }
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") long id){
       PostDto dto= postService.getPostById(id);
       return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}

