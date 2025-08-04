package com.cliptripbe.feature.auth.api;

import com.cliptripbe.feature.user.dto.request.UserSignInRequest;
import com.cliptripbe.feature.user.dto.request.UserSignUpRequest;
import com.cliptripbe.feature.user.dto.response.UserLoginResponse;
import com.cliptripbe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Tag(name = "인증 관련 API")
public interface AuthControllerDocs {

    @Operation(summary = "회원가입 API")
    ApiResponse<?> signUp(UserSignUpRequest signUpDto);

    @Operation(summary = "로그인 API")
    ApiResponse<UserLoginResponse> signIn(
        UserSignInRequest userSignInRequest,
        HttpServletResponse response
    );

    @Operation(summary = "로그아웃 API")
    ApiResponse<?> logout(HttpServletResponse response);

    @Operation(summary = "리프레쉬 토큰을 통한 엑세스 토큰 재 발행")
    ApiResponse<?> renewTheToken(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse);

}
