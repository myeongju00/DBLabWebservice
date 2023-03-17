package com.dblab.webservice.domain.user.controller;

import com.dblab.webservice.domain.user.controller.springdocs.GetUserInfoSpringDocs;
import com.dblab.webservice.domain.user.model.dto.UserDto;
import com.dblab.webservice.domain.user.model.response.UserInfoResponse;
import com.dblab.webservice.domain.user.service.UserService;
import com.dblab.webservice.domain.user.service.CustomOAuth2UserService;
import com.dblab.webservice.global.controller.FirstVersionRestController;
import com.dblab.webservice.global.dto.ApiResponse;
import com.dblab.webservice.global.message.UserMessage;
import com.dblab.webservice.oauth.SecurityUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "사용자 인증 관련 컨트롤러")
@FirstVersionRestController
@RequiredArgsConstructor
public class userController {
    private final UserService userService;

    private UserDto getUserEntity() {
        Long userId = SecurityUtil.getCurrentUserId();

        if(userId != null)
            return userService.findUserEntity(userId);

        return null;
    }

    @GetUserInfoSpringDocs
    @GetMapping("/userInfo")
    public ApiResponse<UserInfoResponse> getUserInfo() {
        UserDto userDto = getUserEntity();
        if (userDto != null) {
            return ApiResponse.createResponseWithMessage(userDto.toUserInfoResponse(), UserMessage.READ_USER_SUCCESS);
        } else {
            return ApiResponse.createResponseWithMessage(null, UserMessage.USER_NOT_FOUND);
        }
    }

    //TODO : 이거 필요한가요?
    @GetMapping("/userInfo/{id}")
    public ApiResponse<UserInfoResponse> getOtherUserInfo(@PathVariable Long id) {
        UserDto userDto= userService.findUserEntity(id);
        return ApiResponse.createResponseWithMessage(userDto.toUserInfoResponse(),UserMessage.READ_USER_SUCCESS);
    }
}
