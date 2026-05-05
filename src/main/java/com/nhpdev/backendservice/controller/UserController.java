package com.nhpdev.backendservice.controller;

import com.nhpdev.backendservice.dto.request.UserChangePasswordRequest;
import com.nhpdev.backendservice.dto.request.UserCreateRequest;
import com.nhpdev.backendservice.dto.request.UserUpdateRequest;
import com.nhpdev.backendservice.dto.response.ApiResponse;
import com.nhpdev.backendservice.dto.response.UserChangePasswordResponse;
import com.nhpdev.backendservice.dto.response.UserDetailResponse;
import com.nhpdev.backendservice.dto.response.UserUpdateResponse;
import com.nhpdev.backendservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping
    public ApiResponse<UserDetailResponse> createUser(@RequestBody UserCreateRequest request) {
        return ApiResponse.<UserDetailResponse>builder()
                .ok(true)
                .data(userService.createUser(request))
                .code(HttpStatus.CREATED.value())
                .build();
    }

    @GetMapping
    public ApiResponse<List<UserDetailResponse>> getAllUser() {
        return ApiResponse.<List<UserDetailResponse>>success(userService.getAllUser());
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDetailResponse> getUserById(@PathVariable String id) {
        return ApiResponse.<UserDetailResponse>success(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<UserUpdateResponse> updateUser(@PathVariable String id,
                                                      @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserUpdateResponse>success(userService.updateUser(id, request));
    }

    @PutMapping("/forgot-password/{id}")
    public ApiResponse<UserChangePasswordResponse> changePassword(@PathVariable String id,
                                                                  @RequestBody UserChangePasswordRequest request) {
        return ApiResponse.<UserChangePasswordResponse>success(userService.changePassword(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .ok(true)
                .message("User is deleted")
                .code(HttpStatus.NO_CONTENT.value())
                .build();
    }

}
