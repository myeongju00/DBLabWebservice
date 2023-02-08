package com.dblab.webservice.oauth.dto;

import com.dblab.webservice.model.user.ProviderType;
import com.dblab.webservice.model.user.Role;
import com.dblab.webservice.model.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String picture;
    private Role role;
    private ProviderType providerType;

    public UserDto(UserEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.picture = entity.getPicture();
        this.role = entity.getRole();
        this.providerType = entity.getProviderType();
    }

    public UserEntity toEntity(){
        return UserEntity.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.MEMBER)
                .providerType(providerType)
                .build();
    }
}
