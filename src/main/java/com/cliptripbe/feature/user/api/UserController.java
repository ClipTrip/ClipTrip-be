package com.cliptripbe.feature.user.api;

import static com.cliptripbe.global.config.Constant.API_VERSION;

import com.cliptripbe.feature.user.api.dto.request.UserSignInRequestDto;
import com.cliptripbe.feature.user.api.dto.request.UserSignUpRequestDto;
import com.cliptripbe.feature.user.api.dto.response.UserInfoResponse;
import com.cliptripbe.feature.user.application.UserService;
import com.cliptripbe.global.auth.jwt.entity.JwtToken;
import com.cliptripbe.global.response.ApiResponse;
import com.cliptripbe.global.response.type.SuccessType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/users")
public class UserController implements UserControllerDocs {

    private final UserService userService;

    @Override
    @PostMapping("/sign-up")
    public ApiResponse<?> signUp(
        @RequestBody UserSignUpRequestDto signUpDto) {
        UserInfoResponse studentSignUpResponse = userService.signUp(signUpDto);
        return ApiResponse.success(SuccessType.CREATED, studentSignUpResponse);
    }

    @Override
    @PostMapping("/sign-in")
    public ApiResponse<JwtToken> signIn(
        @RequestBody UserSignInRequestDto userSignInRequestDto) {
        JwtToken jwtToken = userService.userSignIn(userSignInRequestDto);
        return ApiResponse.success(SuccessType.SUCCESS, jwtToken);
    }

    @Override
    @GetMapping
    public ApiResponse<List<UserInfoResponse>> retrieveAllStudent() {
        return ApiResponse.success(SuccessType.SUCCESS, userService.getAllUserInfo());
    }
}
