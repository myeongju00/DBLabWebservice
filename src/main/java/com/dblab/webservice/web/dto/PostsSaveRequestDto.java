package com.dblab.webservice.web.dto;

import com.dblab.webservice.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

    private String title;
    private String category;

    private String content;
    private String author;

    @Builder
    public PostsSaveRequestDto(String category, String title, String content, String author) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Posts toEntity() {
        return Posts.builder()
                .category(category)
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
