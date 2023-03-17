package com.dblab.webservice.global.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserMessage implements ResponseMessage{
    USER_NOT_FOUND("해당 유저는 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    LOGIN_CONFLICT("이미 로그인이 되어있습니다.", HttpStatus.CONFLICT),
    LOGIN_SUCCESS("로그인에 성공했습니다.", HttpStatus.OK),
    LOGOUT_SUCCESS("로그아웃에 성공했습니다.", HttpStatus.OK),
    REISSUE_SUCCESS("토큰 재발급에 성공하였습니다.", HttpStatus.CREATED),
    REFRESH_TOKEN_INVALID("RefreshToken이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    USER_MISMATCH_PROVIDER_TYPE("ProviderType이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    READ_USER_SUCCESS("유저 정보 조회에 성공하였습니다.", HttpStatus.OK),
    USER_TOKEN_REISSUE("토큰 재발급을 성공하였습니다.", HttpStatus.OK),
    USER_LOGOUT("유저가 로그아웃을 하였습니다.", HttpStatus.OK);

    private final String message;
    private final HttpStatus status;
}
