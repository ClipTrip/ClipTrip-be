package com.cliptripbe.global.auth.security;

import com.cliptripbe.feature.user.domain.entity.User;
import com.cliptripbe.feature.user.domain.service.UserLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerDetailsService implements UserDetailsService {

    private final UserLoader userLoader;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userLoader.findByEmail(email);
        return new CustomerDetails(user);
    }
}
