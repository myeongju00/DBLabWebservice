package com.dblab.webservice.domain.user.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    MEMBER("ROLE_MEMBER", "랩원"),
    GUEST("ROLE_GUEST", "게스트");

    private final String key;
    private final String title;
}
