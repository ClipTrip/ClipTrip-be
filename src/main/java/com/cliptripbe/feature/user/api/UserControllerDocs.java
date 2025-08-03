package com.cliptripbe.feature.user.api;

import com.cliptripbe.feature.user.dto.request.UserSignInRequest;
import com.cliptripbe.feature.user.dto.request.UserSignUpRequest;
import com.cliptripbe.feature.user.dto.response.UserLoginResponse;
import com.cliptripbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "User 관련 API")
public interface UserControllerDocs {

    @Operation(summary = "회원가입 API")
    ApiResponse<?> signUp(UserSignUpRequest signUpDto);

    @Operation(summary = "로그인 API")
    ApiResponse<UserLoginResponse> signIn(
        UserSignInRequest userSignInRequest,
        HttpServletResponse response
    );

    @Operation(summary = "로그아웃 API")
    ApiResponse<?> logout(HttpServletResponse response);


    @Operation(summary = "유저 전체 조회 API")
    ApiResponse<?> retrieveAllStudent();
}
