package com.nhpdev.backendservice.service;

import com.nhpdev.backendservice.dto.request.UserChangePasswordRequest;
import com.nhpdev.backendservice.dto.request.UserCreateRequest;
import com.nhpdev.backendservice.dto.request.UserUpdateRequest;
import com.nhpdev.backendservice.dto.response.UserChangePasswordResponse;
import com.nhpdev.backendservice.dto.response.UserDetailResponse;
import com.nhpdev.backendservice.dto.response.UserUpdateResponse;
import com.nhpdev.backendservice.entity.User;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetailResponse createUser(UserCreateRequest request) {
        if(userRepository.existsUserByEmail(request.email()))
            throw new RuntimeException("email is already being used");
        if(userRepository.existsUserByUsername(request.username()))
            throw new RuntimeException("username is already being used");
        User newUser = User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .build();
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
                .orElseThrow(() -> new RuntimeException("User is not exist!"));
    }

    @Override
    public UserUpdateResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User is not exist!"));
        user.setUsername(request.username());
        User updatedUser = userRepository.save(user);
        return UserUpdateResponse.builder()
                .username(updatedUser.getUsername())
                .build();
    }

    @Override
    public UserChangePasswordResponse changePassword(String id, UserChangePasswordRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User is not exist!"));
        if(!passwordEncoder.matches(request.oldPassword(), user.getPassword()))
            throw new RuntimeException("Wrong password!");
        if(!request.newPassword().equals(request.newPassword2()))
            throw new RuntimeException("New passwords unmatch");
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
            throw new RuntimeException("User is not exist");
        userRepository.deleteById(id);
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
