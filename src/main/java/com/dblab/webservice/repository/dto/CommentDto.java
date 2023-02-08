package com.dblab.webservice.repository.dto;

import com.dblab.webservice.model.comment.CommentEntity;
import com.dblab.webservice.model.posts.Posts;
import com.dblab.webservice.model.user.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentDto {
    private Long commentId;

    private Posts posts;

    private UserEntity user;

    private String commentText;

    private Long commentLikeCount;

    private CommentDto parent;

    private int depth;

    public CommentDto SaveRequestCommentDto(Posts posts, UserEntity user, String commentText, CommentDto parent) {
        this.posts = posts;
        this.user = user;
        this.commentText = commentText;
        this.parent = parent;
        return new CommentDto();
    }

    public CommentEntity toEntity() {
        return CommentEntity.builder()
                .commentId(commentId)
                .posts(posts)
                .user(user)
                .commentText(commentText)
                .commentLikeCount(commentLikeCount)
                .parent(parent.toEntity())
                .depth(depth)
                .build();
    }
}
