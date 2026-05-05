package com.nhpdev.backendservice.configuration;


import com.nhpdev.backendservice.common.UserStatus;
import com.nhpdev.backendservice.entity.User;
import com.nhpdev.backendservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsUserByUsername("admin")) {
                User admin = User.builder().email("admin")
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .status(UserStatus.ACTIVE)
                        .build();
                userRepository.save(admin);
                log.info("Admin has been created");
            }
        };
    }
}
