package com.cliptripbe.feature.user.domain.service;

import com.cliptripbe.feature.user.infrastructure.UserRepository;
import com.cliptripbe.global.response.exception.CustomException;
import com.cliptripbe.global.response.type.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserChecker {

    final UserRepository userRepository;

    public void checkExistUserEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(ErrorType.DUPLICATE_EMAIL_RESOURCE);
        }
    }
}
