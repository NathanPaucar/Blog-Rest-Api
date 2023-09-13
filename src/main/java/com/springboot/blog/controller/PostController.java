package com.springboot.blog.controller;


import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "CRUD REST APIs for Post Resource")
public class PostController {

    //we are injecting an interface not a class to create loose coupling
    private PostService postService;

    // one constructor means no autowire is needed
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(
            summary = "Create Post Rest API",
            description = "Create Post Rest API is used to save post into database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED"
    )
    // lock the createPost method in swagger until Bearer Key is inserted
    @SecurityRequirement(name = "Bearer Authentication")
    //check role field for "ADMIN"
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){//@Valid makes sure the requirements are met
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get All Posts Rest API",
            description = "Get Post Rest API is used to fetch all the posts from database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED"
    )
    @GetMapping
    public PostResponse getAllPost(
            // the annotation below will be responsible for paging support
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false)int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false)String sortBY,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
            ){
        return postService.getAllPost(pageNo, pageSize, sortBY, sortDir);
    }

    @Operation(
            summary = "Get Post By Id Rest API",
            description = "Get Post By Id Rest API is used to get single post from database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getById(@PathVariable(name = "id") long id){
        return ResponseEntity.ok(postService.getPostBy(id));
    }

    @Operation(
            summary = "Updated Post Rest API",
            description = "Put Post Rest API is used to update existing post from database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http status 200 OK"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable(name = "id") long id){
        PostDto postResponse = postService.updatePost(postDto, id);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete Post Rest API",
            description = "Delete Post Rest API is used to delete post from database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http status 201 CREATED"
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id){
        postService.deletePostByID(id);
        return new ResponseEntity<>("Post has been deleted", HttpStatus.OK);
    }

    // Get all posts belonging to CategoryId
    // http://localhost:8080/api/posts/category/8
    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDto>> getPostsBYCategorry(@PathVariable("id") Long categoryId){
        List<PostDto> postDtos = postService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(postDtos);
    }
}
