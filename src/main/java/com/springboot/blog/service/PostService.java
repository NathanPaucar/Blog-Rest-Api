package com.springboot.blog.service;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostResponse;

import java.util.List;


public interface PostService {
    PostDto createPost(PostDto postDto);
    PostResponse getAllPost(int pageNo, int pageSize, String sortBy, String sortDir);
    PostDto getPostBy(long id);
    PostDto updatePost(PostDto postDto, long id);
    Void deletePostByID(long id);
    List<PostDto> getPostsByCategory(long categoryId);
}
