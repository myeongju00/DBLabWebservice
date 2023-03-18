package com.dblab.webservice.domain.user.model.entity;

import com.dblab.webservice.model.BaseTimeEntity;
import com.dblab.webservice.domain.user.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "TB_User")
public class UserEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String socialId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_provider_type")
    private ProviderType providerType;

    public UserEntity update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    public String getRoleKey() {
        return  this.role.getKey();
    }

    /* 소셜로그인시 이미 등록된 회원이라면 수정날짜만 업데이트하고
     * 기존 데이터는 그대로 보존하도록 예외처리 */
    public UserEntity updateModifiedDate() {
        this.onPreUpdate();
        return this;
    }

    public UserDto toDto() {
        return UserDto.builder()
                .userId(userId)
                .socialId(socialId)
                .name(name)
                .email(email)
                .picture(picture)
                .role(role)
                .providerType(providerType)
                .build();
    }

}
