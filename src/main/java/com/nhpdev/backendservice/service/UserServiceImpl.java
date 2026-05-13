package com.nhpdev.backendservice.service;

import com.nhpdev.backendservice.dto.request.AssignRoleRequest;
import com.nhpdev.backendservice.dto.request.UserChangePasswordRequest;
import com.nhpdev.backendservice.dto.request.UserCreateRequest;
import com.nhpdev.backendservice.dto.request.UserUpdateRequest;
import com.nhpdev.backendservice.dto.response.AssignRoleResponse;
import com.nhpdev.backendservice.dto.response.UserChangePasswordResponse;
import com.nhpdev.backendservice.dto.response.UserDetailResponse;
import com.nhpdev.backendservice.dto.response.UserUpdateResponse;
import com.nhpdev.backendservice.entity.Role;
import com.nhpdev.backendservice.entity.User;
import com.nhpdev.backendservice.entity.UserHasRole;
import com.nhpdev.backendservice.exception.BackendServiceException;
import com.nhpdev.backendservice.exception.ErrorCode;
import com.nhpdev.backendservice.repository.RoleRepository;
import com.nhpdev.backendservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetailResponse createUser(UserCreateRequest request) {
        if(userRepository.existsUserByEmail(request.email()))
            throw new BackendServiceException(ErrorCode.USER_EXISTED);
        if(userRepository.existsUserByUsername(request.username()))
            throw new BackendServiceException(ErrorCode.USER_EXISTED);
        User newUser = User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .build();
        Role role = roleRepository.findRoleByName("USER")
                .orElseGet(() -> Role.builder()
                        .name("USER")
                        .build());
        newUser.addRole(role);
        User savedUser = userRepository.save(newUser);
        return UserDetailResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .status(savedUser.getStatus().name())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDetailResponse> getAllUser() {
        return userRepository.findAll().stream()
                .map(this::mapToUserDetailResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailResponse getUserById(String id) {
        return userRepository.findById(id).map(this::mapToUserDetailResponse)
                .orElseThrow(() -> new BackendServiceException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public UserUpdateResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BackendServiceException(ErrorCode.USER_NOT_EXISTED));
        user.setUsername(request.username());
        User updatedUser = userRepository.save(user);
        return UserUpdateResponse.builder()
                .username(updatedUser.getUsername())
                .build();
    }

    @Override
    public UserChangePasswordResponse changePassword(String id, UserChangePasswordRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BackendServiceException(ErrorCode.USER_NOT_EXISTED));
        if(!passwordEncoder.matches(request.oldPassword(), user.getPassword()))
            throw new BackendServiceException(ErrorCode.UNAUTHORIZED);
        if(!request.newPassword().equals(request.newPassword2()))
            throw new BackendServiceException(ErrorCode.UNAUTHORIZED);
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        User updatedUser = userRepository.save(user);
        return UserChangePasswordResponse.builder()
                .id(updatedUser.getId())
                .username(updatedUser.getUsername())
                .build();
    }

    @Override
    public void deleteUser(String id) {
        if (!userRepository.existsById(id))
            throw new BackendServiceException(ErrorCode.USER_NOT_EXISTED);
        userRepository.deleteById(id);
    }

    @Override
    public AssignRoleResponse assignRole(String userId, AssignRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BackendServiceException(ErrorCode.USER_NOT_EXISTED));
        Role role = roleRepository.findRoleByName(request.roleName())
                .orElseThrow(() -> new BackendServiceException(ErrorCode.ROLE_NOT_EXISTED));
        user.addRole(role);
        User savedUser = userRepository.save(user);
        return AssignRoleResponse.builder()
                .userId(savedUser.getId())
                .roles(savedUser.getUserHasRoles().stream()
                        .map(UserHasRole::getRole)
                        .map(Role::getName).toList())
                .build();
    }

    @Override
    public UserDetailResponse showMyInfo(String userId) {
        return userRepository.findById(userId)
                .map(user -> UserDetailResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .status(user.getStatus().name())
                        .build())
                .orElseThrow(() -> new BackendServiceException(ErrorCode.USER_NOT_EXISTED));
    }

    private UserDetailResponse mapToUserDetailResponse(User user) {
        return UserDetailResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .status(user.getStatus().name())
                .build();
    }
}
