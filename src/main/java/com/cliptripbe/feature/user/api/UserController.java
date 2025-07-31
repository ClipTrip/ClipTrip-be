package com.cliptripbe.feature.user.api;

import static com.cliptripbe.global.constant.Constant.API_VERSION;

import com.cliptripbe.feature.user.dto.request.UserSignInRequest;
import com.cliptripbe.feature.user.dto.request.UserSignUpRequest;
import com.cliptripbe.feature.user.dto.response.UserInfoResponse;
import com.cliptripbe.feature.user.dto.response.UserLoginResponse;
import com.cliptripbe.feature.user.application.UserService;
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
        @RequestBody UserSignUpRequest signUpDto) {
        UserInfoResponse studentSignUpResponse = userService.signUp(signUpDto);
        return ApiResponse.success(SuccessType.CREATED, studentSignUpResponse);
    }

    @Override
    @PostMapping("/sign-in")
    public ApiResponse<UserLoginResponse> signIn(
        @RequestBody UserSignInRequest userSignInRequest
    ) {
        UserLoginResponse userLoginResponse = userService.userSignIn(userSignInRequest);
        return ApiResponse.success(SuccessType.OK, userLoginResponse);
    }

    @Override
    @GetMapping
    public ApiResponse<List<UserInfoResponse>> retrieveAllStudent() {
        return ApiResponse.success(SuccessType.OK, userService.getAllUserInfo());
    }
}
