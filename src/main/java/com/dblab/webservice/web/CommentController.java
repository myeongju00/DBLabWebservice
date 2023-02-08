package com.dblab.webservice.web;

import com.dblab.webservice.config.auth.CustomOAuth2UserService;
import com.dblab.webservice.config.auth.dto.SessionUser;
import com.dblab.webservice.service.CommentService;
import com.dblab.webservice.service.PostsService;
import com.dblab.webservice.repository.dto.CommentDto;
import com.dblab.webservice.repository.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;
    private final PostsService postsService;
    private final CustomOAuth2UserService userService;

    private SessionUser getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;

        String username = ((UserDetails) principal).getUsername();
        if(username != null) {
            return userService.
        }
    }

    /**
    GET comments/{postNumber} : comment list 조회

    POST comments/{postNumber} : comment 등록
    PATCH comments/{postNumber}/{commentNumber} : comment 수정

    DELETE comments/{postNumber}/{commentNumber} : comment 삭제
    */
    @PostMapping("/api/v1/post/{postId}/comment")
    public Long save(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        PostsResponseDto postsResponseDto = postsService.findById(id);

        return id;
    }
}
