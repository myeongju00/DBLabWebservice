package com.dblab.webservice.domain.user.controller.springdocs;

import com.dblab.webservice.domain.user.controller.springdocs.model.UnauthorizedException;
import com.dblab.webservice.domain.user.controller.springdocs.model.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "로그인한 사용자의 프로필 상세정보 조회 API", description = "로그인한 사용자 자기자신의 프로필을 조회합니다.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", useReturnTypeSchema = true),
        @ApiResponse(responseCode = "404", description = "등록된 유저가 없습니다.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserNotFoundException.class))),
        @ApiResponse(responseCode = "401", description = "인증이 필요합니다.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UnauthorizedException.class)))

})
public @interface GetUserInfoSpringDocs {
}