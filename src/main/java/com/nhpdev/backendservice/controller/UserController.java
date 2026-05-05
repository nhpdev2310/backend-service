package com.nhpdev.backendservice.controller;

import com.nhpdev.backendservice.dto.request.UserChangePasswordRequest;
import com.nhpdev.backendservice.dto.request.UserCreateRequest;
import com.nhpdev.backendservice.dto.request.UserUpdateRequest;
import com.nhpdev.backendservice.dto.response.ApiResponse;
import com.nhpdev.backendservice.dto.response.UserChangePasswordResponse;
import com.nhpdev.backendservice.dto.response.UserDetailResponse;
import com.nhpdev.backendservice.dto.response.UserUpdateResponse;
import com.nhpdev.backendservice.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Tag(name = "CREATE USER")
    @PostMapping
    public ApiResponse<UserDetailResponse> createUser(@RequestBody UserCreateRequest request) {
        return ApiResponse.<UserDetailResponse>builder()
                .ok(true)
                .data(userService.createUser(request))
                .code(HttpStatus.CREATED.value())
                .build();
    }

    @Tag(name = "GET ALL USERS")
    @GetMapping
    public ApiResponse<List<UserDetailResponse>> getAllUser() {
        return ApiResponse.<List<UserDetailResponse>>success(userService.getAllUser());
    }

    @Tag(name = "GET ONE USER")
    @GetMapping("/{id}")
    public ApiResponse<UserDetailResponse> getUserById(@PathVariable String id) {
        return ApiResponse.<UserDetailResponse>success(userService.getUserById(id));
    }

    @Tag(name = "UPDATE USER")
    @PutMapping("/{id}")
    public ApiResponse<UserUpdateResponse> updateUser(@PathVariable String id,
                                                      @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserUpdateResponse>success(userService.updateUser(id, request));
    }

    @Tag(name = "FORGOT PASSWORD")
    @PutMapping("/forgot-password/{id}")
    public ApiResponse<UserChangePasswordResponse> changePassword(@PathVariable String id,
                                                                  @RequestBody UserChangePasswordRequest request) {
        return ApiResponse.<UserChangePasswordResponse>success(userService.changePassword(id, request));
    }

    @Tag(name = "DELETE USER")
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
