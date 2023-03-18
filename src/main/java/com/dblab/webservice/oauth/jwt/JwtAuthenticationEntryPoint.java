package com.dblab.webservice.oauth.jwt;

import com.dblab.webservice.global.dto.ApiResponse;
import com.dblab.webservice.global.message.DefaultMessage;
import com.dblab.webservice.global.message.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        ResponseMessage errorMessage = DefaultMessage.UNAUTHORIZED;
        ApiResponse jmtApiResponse = ApiResponse.createResponseWithMessage(null, errorMessage);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(jmtApiResponse);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorMessage.getStatus().value());
        response.getWriter().write(json);
    }
}
