package com.dblab.webservice.domain.user.controller.springdocs.model;

import com.dblab.webservice.global.message.UserMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserNotFoundException {
    @Schema(description = "", nullable = true)
    String data = null;

    @Schema(description = "", example = "유저 정보가 없습니다.")
    String message = UserMessage.USER_NOT_FOUND.getMessage();

    @Schema(description = "", example = "USER_NOT_FOUND")
    String code = UserMessage.USER_NOT_FOUND.toString();
}
