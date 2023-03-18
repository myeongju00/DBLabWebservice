package com.dblab.webservice.oauth;

import com.dblab.webservice.global.exception.ApiException;
import com.dblab.webservice.global.message.DefaultMessage;
import com.dblab.webservice.global.message.UserMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    // SecurityContext 에 유저 정보가 저장되는 시점
    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    public static Long getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new ApiException(DefaultMessage.UNAUTHORIZED);
        }

        String name = authentication.getName();
        if(name.equals("anonymousUser") || name.equals("anonymous"))
            return null;
        return Long.parseLong(name);
    }
}