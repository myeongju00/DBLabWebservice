package com.dblab.webservice.oauth.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
         String grantType;
         String accessToken;
         String refreshToken;
         Long accessTokenExpiresIn;
}
