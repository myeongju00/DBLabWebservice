package com.dblab.webservice.repository.dto;


import com.dblab.webservice.model.posts.Posts;
import lombok.Getter;

@Getter
public class PostsResponseDto {
    private Long id;

    private String category;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto (Posts entity) {
        this.id = entity.getPostId();
        this.category = entity.getCategory();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
