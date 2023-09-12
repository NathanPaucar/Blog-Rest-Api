package com.springboot.blog.dto;

import com.springboot.blog.entity.Post;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Lombok;

@Data
public class CommentDto {

    private long id;

    @NotEmpty(message = "Name should not be null or empty")
    private String name;

    @NotEmpty(message = "Email should not be empty")
    private String email;

    @NotEmpty(message = "Body should not be empty")
    @Size(min = 10, message = "Comment body must be minimum 10 characters")
    private String body;
    //leave Post out to not create a circular reference
}
