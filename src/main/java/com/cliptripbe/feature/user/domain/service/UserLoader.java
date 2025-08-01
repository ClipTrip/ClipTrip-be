package com.cliptripbe.feature.user.domain.service;

import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.feature.user.infrastructure.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserLoader {

    final UserRepository userRepository;


    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
            () -> new EntityNotFoundException("없는 아이디입니다.")
        );
    }
}
