package com.springboot.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String email;
    private String body;

    // establish many-to-one relation
    @ManyToOne(fetch = FetchType.LAZY)

    // link comment(many) to post(one) in column "post_id"
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

}
