package com.cliptripbe.feature.auth.api;

import static com.cliptripbe.global.constant.Constant.API_VERSION;

import com.cliptripbe.feature.auth.application.AuthService;
import com.cliptripbe.feature.auth.dto.TokenVerifyResponse;
import com.cliptripbe.feature.user.dto.request.UserSignInRequest;
import com.cliptripbe.feature.auth.dto.UserLoginResponse;
import com.cliptripbe.global.response.ApiResponse;
import com.cliptripbe.global.response.type.SuccessType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/auth")
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @Override
    @PostMapping("/sign-in")
    public ApiResponse<UserLoginResponse> signIn(
        @RequestBody UserSignInRequest userSignInRequest,
        HttpServletResponse response
    ) {
        UserLoginResponse userLoginResponse = authService.userSignIn(userSignInRequest, response);
        return ApiResponse.success(SuccessType.OK, userLoginResponse);
    }

    @Override
    @PostMapping("/logout")
    public ApiResponse<?> logout(HttpServletResponse response) {
        authService.logout(response);
        return ApiResponse.success(SuccessType.OK);
    }

    @Override
    @PostMapping("/refresh")
    public ApiResponse<?> renewTheToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        authService.refreshAccessToken(request, response);
        return ApiResponse.success(SuccessType.OK);
    }

    @Override
    @GetMapping("/verify")
    public ApiResponse<TokenVerifyResponse> tokenVerify(HttpServletRequest request) {
        TokenVerifyResponse response = authService.verifyAccessToken(request);
        return ApiResponse.success(SuccessType.OK, response);
    }
}
