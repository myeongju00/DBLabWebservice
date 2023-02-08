package com.dblab.webservice.repository.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {

//    private String category;
    private String title;
    private String content;

    @Builder
    public PostsUpdateRequestDto( String title, String content) {
        this.title = title;
        this.content = content;
    }
}
