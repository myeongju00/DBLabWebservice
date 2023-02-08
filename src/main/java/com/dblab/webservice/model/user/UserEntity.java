package com.dblab.webservice.model.user;

import com.dblab.webservice.model.BaseTimeEntity;
import com.dblab.webservice.oauth.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public UserEntity(String name, String email, String picture, Role role) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public UserEntity update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public String getRoleKey() {
        return  this.role.getKey();
    }

    public UserDto toDto() {
        return UserDto.builder()
                .id(id)
                .name(name)
                .email(email)
                .picture(picture)
                .role(role)
                .providerType(providerType)
                .build();
    }

}
