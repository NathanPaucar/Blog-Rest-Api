package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

// pass the class type and the id type when extending to Repository
public interface PostRepository extends JpaRepository<Post, Long>{

}