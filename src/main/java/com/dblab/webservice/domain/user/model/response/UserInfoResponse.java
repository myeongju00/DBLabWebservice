package com.dblab.webservice.domain.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private Long userId;
    private String name;
    private String email;
    private String picture;

    private String role;

}
