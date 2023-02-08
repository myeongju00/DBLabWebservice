package com.dblab.webservice.repository.dto;

import com.dblab.webservice.model.comment.CommentEntity;
import com.dblab.webservice.model.posts.Posts;
import com.dblab.webservice.model.user.UserEntity;

public class CommentResponseDto {
    private Long commentId;

    private Posts posts;

    private UserEntity user;

    private String commentText;

    private Long commentLikeCount;

    private CommentDto parent;

    private int depth;

    public CommentResponseDto(CommentEntity entity) {
        this.commentId = entity.getCommentId();
        this.posts = entity.getPosts();
        this.user = entity.getUser();
        this.commentText = entity.getCommentText();
        this.commentLikeCount = entity.getCommentLikeCount();
        this.parent = entity.getParent().toDto();
        this.depth = entity.getDepth();
    }
}
