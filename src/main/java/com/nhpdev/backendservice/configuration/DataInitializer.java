package com.nhpdev.backendservice.configuration;


import com.nhpdev.backendservice.common.RoleName;
import com.nhpdev.backendservice.common.UserStatus;
import com.nhpdev.backendservice.entity.Role;
import com.nhpdev.backendservice.entity.User;
import com.nhpdev.backendservice.repository.RoleRepository;
import com.nhpdev.backendservice.repository.UserRepository;
import com.nhpdev.backendservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {
    private final RoleRepository roleRepository;
    @Value("${admin.admin_email}")
    private String adminEmail;
    @Value("${admin.admin_password}")
    private String adminPassword;
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsUserByUsername(adminEmail)) {
                Role role = roleRepository.findRoleByName(RoleName.ADMIN.name())
                        .orElseGet(() -> {
                            Role newRole = Role.builder().name(RoleName.ADMIN.name()).build();
                            return roleRepository.save(newRole);
                        });
                User admin = User.builder().email(adminEmail)
                        .username(adminEmail)
                        .password(passwordEncoder.encode(adminPassword))
                        .status(UserStatus.ACTIVE)
                        .build();
                admin.addRole(role);
                userRepository.save(admin);
                log.info("Admin has been created");
            }
        };
    }
}
