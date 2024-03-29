//package com.dblab.webservice.model.comment;
//
//import com.dblab.webservice.model.BaseTimeEntity;
//import com.dblab.webservice.model.posts.Posts;
//import com.dblab.webservice.domain.user.model.entity.UserEntity;
//import com.dblab.webservice.repository.dto.CommentDto;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.ColumnDefault;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;
//
//@Builder
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//public class CommentEntity extends BaseTimeEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long commentId;
//
//    @ManyToOne(fetch =  FetchType.LAZY)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JoinColumn(name = "post_id", nullable = false)
//    private Posts posts;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JoinColumn(name="user_id", nullable = false)
//    private UserEntity user;
//
//    private String commentText;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_id")
//    private CommentEntity parent;
//
//    @ColumnDefault("0")
//    private int depth;
//
//    private Long commentLikeCount;
//
//    public CommentDto toDto() {
//        return CommentDto.builder()
//                .commentId(commentId)
//                .posts(posts)
//                .user(user)
//                .commentText(commentText)
//                .parent(parent.toDto())
//                .commentLikeCount(commentLikeCount)
//                .depth(depth)
//                .build();
//    }
//
//}
