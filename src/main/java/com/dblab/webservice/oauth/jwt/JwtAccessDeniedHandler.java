package com.dblab.webservice.oauth.jwt;

import com.dblab.webservice.global.dto.ApiResponse;
import com.dblab.webservice.global.message.DefaultMessage;
import com.dblab.webservice.global.message.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        // 필요한 권한이 없이 접근하려 할때 403
        ResponseMessage errorMessage = DefaultMessage.FORBIDDEN;

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(ApiResponse.createResponseWithMessage(null, errorMessage));

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorMessage.getStatus().value());
        response.getWriter().write(json);
    }
}
