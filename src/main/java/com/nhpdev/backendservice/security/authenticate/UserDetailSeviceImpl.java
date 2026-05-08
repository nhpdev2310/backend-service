package com.nhpdev.backendservice.security.authenticate;

import com.nhpdev.backendservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDetailSeviceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUsersByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found with email {}", email);
                   return new UsernameNotFoundException(email + "not found!");
                });
    }
}
