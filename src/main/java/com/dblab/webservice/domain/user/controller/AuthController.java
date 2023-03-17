package com.dblab.webservice.domain.user.controller;

import com.dblab.webservice.domain.user.model.request.TokenReissueRequest;
import com.dblab.webservice.domain.user.service.AuthService;
import com.dblab.webservice.global.controller.FirstVersionRestController;
import com.dblab.webservice.global.dto.ApiResponse;
import com.dblab.webservice.global.message.UserMessage;
import com.dblab.webservice.oauth.jwt.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FirstVersionRestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final String authUrl = "/auth";

    @PostMapping(authUrl + "/reissue")
    public ApiResponse<TokenResponse> reissue(@RequestBody TokenReissueRequest tokenReissueRequest) {
        TokenResponse jwtTokenResponse = authService.reissue(tokenReissueRequest);
        return ApiResponse.createResponseWithMessage(jwtTokenResponse, UserMessage.USER_TOKEN_REISSUE);
    }

//    @ApiOperation(value = "유저 로그아웃", notes = "Access Token, Refresh Token 제거 합니다 (클라이언트 측에서도 헤더를 제거해줘야 합니다.)")
    @DeleteMapping(authUrl + "/logout")
    public ApiResponse<?> logout(@RequestBody TokenReissueRequest tokenReissueRequest) {
        authService.logout(tokenReissueRequest.getAccessToken(), tokenReissueRequest.getRefreshToken());
        return ApiResponse.createResponseWithMessage(null, UserMessage.USER_LOGOUT);
    }
}
