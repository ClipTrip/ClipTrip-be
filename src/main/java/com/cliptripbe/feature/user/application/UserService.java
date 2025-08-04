package com.cliptripbe.feature.user.application;

import com.cliptripbe.feature.user.domain.service.UserLoader;
import com.cliptripbe.feature.user.dto.response.UserInfoResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserLoader userLoader;


    public List<UserInfoResponse> getAllUserInfo() {
        return userLoader.getAllUser()
            .stream()
            .map(UserInfoResponse::from)
            .collect(Collectors.toList());
    }

}
