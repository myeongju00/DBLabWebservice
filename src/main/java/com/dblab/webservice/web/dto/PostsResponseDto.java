package com.dblab.webservice.web.dto;


import com.dblab.webservice.domain.posts.Posts;
import lombok.Getter;

@Getter
public class PostsResponseDto {
    private Long id;

    private String category;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto (Posts entity) {
        this.id = entity.getId();
        this.category = entity.getCategory();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
