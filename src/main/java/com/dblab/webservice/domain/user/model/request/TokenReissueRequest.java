package com.dblab.webservice.domain.user.model.request;

import lombok.Getter;

@Getter
public class TokenReissueRequest {
    private String accessToken;
    private String refreshToken;
}
