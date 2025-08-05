package com.cliptripbe.feature.user.application;

import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.feature.user.domain.service.UserChecker;
import com.cliptripbe.feature.user.domain.service.UserLoader;
import com.cliptripbe.feature.user.dto.request.UserSignUpRequest;
import com.cliptripbe.feature.user.dto.response.UserInfoResponse;
import com.cliptripbe.feature.user.infrastructure.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserLoader userLoader;
    private final PasswordEncoder passwordEncoder;
    private final UserChecker userChecker;
    private final UserRepository userRepository;

    @Transactional
    public UserInfoResponse signUp(UserSignUpRequest signUpDto) {
        userChecker.checkExistUserEmail(signUpDto.email());
        String encodePassword = passwordEncoder.encode(signUpDto.password());
        User user = signUpDto.toEntity(encodePassword);
        userRepository.save(user);
        return UserInfoResponse.from(user);
    }

    public List<UserInfoResponse> getAllUserInfo() {
        return userLoader.getAllUser()
            .stream()
            .map(UserInfoResponse::from)
            .collect(Collectors.toList());
    }

}
