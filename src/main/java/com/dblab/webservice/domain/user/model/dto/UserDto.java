package com.dblab.webservice.domain.user.model.dto;

import com.dblab.webservice.domain.user.model.entity.ProviderType;
import com.dblab.webservice.domain.user.model.entity.Role;
import com.dblab.webservice.domain.user.model.entity.UserEntity;
import com.dblab.webservice.domain.user.model.response.UserInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String socialId;
    private String name;
    private String email;
    private String picture;
    private Role role;
    private ProviderType providerType;

    public UserDto(UserEntity entity) {
        this.userId = entity.getUserId();
        this.socialId = entity.getSocialId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.picture = entity.getPicture();
        this.role = entity.getRole();
        this.providerType = entity.getProviderType();
    }

    public UserInfoResponse toUserInfoResponse() {
        return new UserInfoResponse(this.userId, this.name, this.email, this.picture, this.role.getKey());
    }

    public UserEntity toEntity(){
        return UserEntity.builder()
                .userId(userId)
                .name(name)
                .socialId(socialId)
                .email(email)
                .picture(picture)
                .role(Role.MEMBER)
                .providerType(providerType)
                .build();
    }
}
