package com.cliptripbe.feature.user.api;

import com.cliptripbe.feature.user.api.dto.request.UserSignInRequestDto;
import com.cliptripbe.feature.user.api.dto.request.UserSignUpRequestDto;
import com.cliptripbe.feature.user.api.dto.response.UserLoginResponse;
import com.cliptripbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User 관련 API")
public interface UserControllerDocs {

    @Operation(summary = "회원가입 API")
    ApiResponse<?> signUp(UserSignUpRequestDto signUpDto);

    @Operation(summary = "로그인 API")
    ApiResponse<UserLoginResponse> signIn(UserSignInRequestDto userSignInRequestDto);

    @Operation(summary = "유저 전체 조회 API")
    ApiResponse<?> retrieveAllStudent();
}
