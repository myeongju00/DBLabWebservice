package com.dblab.webservice.global.exception;

import com.dblab.webservice.global.message.ResponseMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException{
    private final ResponseMessage responseMessage;
}
