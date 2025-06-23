package com.cliptripbe.feature.user.application;

import com.cliptripbe.feature.user.api.dto.request.UserSignInRequestDto;
import com.cliptripbe.feature.user.api.dto.request.UserSignUpRequestDto;
import com.cliptripbe.feature.user.api.dto.response.UserInfoResponse;
import com.cliptripbe.feature.user.domain.User;
import com.cliptripbe.feature.user.infrastructure.UserRepository;
import com.cliptripbe.global.auth.AuthService;
import com.cliptripbe.global.auth.jwt.entity.JwtToken;
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

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UserChecker userChecker;
    private final UserRepository userRepository;
    private final UserLoader userLoader;

    @Transactional
    public UserInfoResponse signUp(UserSignUpRequestDto signUpDto) {
        userChecker.checkExistUserEmail(signUpDto.userId());
        String encodePassword = passwordEncoder.encode(signUpDto.password());
        User user = signUpDto.toEntity(encodePassword);
        userRepository.save(user);
        return UserInfoResponse.from(user);
    }
    
    public JwtToken userSignIn(UserSignInRequestDto userSignInRequestDto) {
        return authService.createAuthenticationToken(userSignInRequestDto.userId(),
            userSignInRequestDto.password());
    }

    public List<UserInfoResponse> getAllUserInfo() {
        return userLoader.getAllUser()
            .stream()
            .map(UserInfoResponse::from)
            .collect(Collectors.toList());
    }
}
