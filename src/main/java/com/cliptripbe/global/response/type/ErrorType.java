package com.cliptripbe.global.response.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    //common
    ENUM_RESOURCE_NOT_FOUND(404, "해당 이넘 값을 찾을 수 없습니다."),

    BAD_REQUEST(400, "잘못된 요청입니다."),
    ENTITY_NOT_FOUND(400, "요청한 정보로 엔터티를 찾을 수 없습니다."),
    DUPLICATE_EMAIL_RESOURCE(409, "이미 존재하는 아이디입니다."),
    FAIL_AUTHENTICATION(401, "아이디와 비밀번호가 틀렸습니다."),
    EXPIRED_ACCESS_TOKEN(400, "만료된 엑세스 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(400, "만료된 리프레쉬 토큰입니다."),
    FAIL_SEND_EMAIL(400, "이메일 전송에 실패하였습니다."),
    FAIL_CREATE_AUTH_CODE(400, "인증 코드 생성에 실패하였습니다."),
    EXPIRED_AUTH_TIME(401, "이메일 인증 코드가 만료되었습니다."),
    MISMATCH_VERIFIED_CODE(401, "이메일 인증 코드가 일치하지 않습니다."),
    NOT_VALID_TOKEN(401, "유효한 토큰이 아닙니다."),
    INVALID_REQUEST(400, "형식에 맞지 않는 요청입니다."),
    DUPLICATE_SEND(409, "인증 코드가 발송되어 있습니다."),
    ACCESS_DENIED_EXCEPTION(401, "접근 권한이 없습니다."),

    // video
    CHATGPT_NO_RESPONSE(502, "ChatGPT로부터 응답을 받지 못했습니다."),
    KAKAO_MAP_NO_RESPONSE(502, "kakaoMap으로부터 응답을 받지 못했습니다."),

    // google api
    GOOGLE_PLACES_EMPTY_RESPONSE(404, "해당 주소로 검색된 장소 또는 사진이 없습니다."),
    GOOGLE_PLACES_NO_RESPONSE(502, "google places로부터 응답을 받지 못했습니다."),

    ;

    private final int httpStatusCode;
    private final String message;
}
