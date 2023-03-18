package com.dblab.webservice.global.dto;

import com.dblab.webservice.global.message.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    T data;
    String message;
    String code;

    public static <G> ApiResponse<G> createResponseWithMessage(G data, ResponseMessage responseMessage) {
        return new ApiResponse<>(data, responseMessage.getMessage(), responseMessage.toString());
    }
}
