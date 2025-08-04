package com.cliptripbe.feature.user.api;

import static com.cliptripbe.global.constant.Constant.API_VERSION;

import com.cliptripbe.feature.user.application.UserService;
import com.cliptripbe.feature.user.dto.response.UserInfoResponse;
import com.cliptripbe.global.response.ApiResponse;
import com.cliptripbe.global.response.type.SuccessType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/users")
public class UserController implements UserControllerDocs {

    private final UserService userService;

    @Override
    @GetMapping
    public ApiResponse<List<UserInfoResponse>> retrieveAllStudent() {
        return ApiResponse.success(SuccessType.OK, userService.getAllUserInfo());
    }
}
