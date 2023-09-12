package com.springboot.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

// switch to setter and getter annotation, mapper doesn't allow for data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name= "posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "content", nullable = false)
    private String content;


    // set post as parent entity, cascade means whatever happens to parent happens to child
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL , orphanRemoval = true)

    // use set, it doesn't allow for duplicated data to be stored
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
