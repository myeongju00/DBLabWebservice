package com.dblab.webservice.global.message;

import org.springframework.http.HttpStatus;


public interface ResponseMessage {
    String getMessage();
    HttpStatus getStatus();
}
