package com.cliptripbe.global.response.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum SuccessType {

    OK(HttpStatus.OK, "요청에 성공하였습니다."),
    CREATED(HttpStatus.CREATED, "성공적으로 생성하였습니다."),
    NO_CONTENT(HttpStatus.NO_CONTENT, "요청에 성공하였습니다.");

    private final HttpStatusCode httpStatusCode;
    private final String message;
}
