package com.nhpdev.backendservice.controller;

import com.nhpdev.backendservice.dto.request.AssignRoleRequest;
import com.nhpdev.backendservice.dto.request.UserChangePasswordRequest;
import com.nhpdev.backendservice.dto.request.UserCreateRequest;
import com.nhpdev.backendservice.dto.request.UserUpdateRequest;
import com.nhpdev.backendservice.dto.response.*;
import com.nhpdev.backendservice.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "USER APIs")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ApiResponse<UserDetailResponse> createUser(@RequestBody UserCreateRequest request) {
        return ApiResponse.<UserDetailResponse>builder()
                .ok(true)
                .data(userService.createUser(request))
                .code(HttpStatus.CREATED.value())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ROLE:ASSIGN')")
    @PutMapping("/{id}/roles")
    public ApiResponse<AssignRoleResponse> assignRoleForUser(@PathVariable("id") String userId,
                                                             @RequestBody AssignRoleRequest request) {
        return ApiResponse.<AssignRoleResponse>builder()
                .ok(true)
                .data(userService.assignRole(userId, request))
                .code(HttpStatus.ACCEPTED.value())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('USER:READ')")
    @GetMapping
    public ApiResponse<List<UserDetailResponse>> getAllUser() {
        return ApiResponse.<List<UserDetailResponse>>success(userService.getAllUser());
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id or hasAuthority('USER:READ')")
    @GetMapping("/{id}")
    public ApiResponse<UserDetailResponse> getUserById(@PathVariable String id) {
        return ApiResponse.<UserDetailResponse>success(userService.getUserById(id));
    }

    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/me")
    public ApiResponse<UserDetailResponse> showMyInfo(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return ApiResponse.<UserDetailResponse>builder()
                .ok(true)
                .data(userService.showMyInfo(userId))
                .code(HttpStatus.OK.value())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PutMapping("/{id}")
    public ApiResponse<UserUpdateResponse> updateUser(@PathVariable String id,
                                                      @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserUpdateResponse>success(userService.updateUser(id, request));
    }

    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping("{id}/password")
    public ApiResponse<UserChangePasswordResponse> changePassword(@PathVariable String id,
                                                                  @RequestBody UserChangePasswordRequest request) {
        return ApiResponse.<UserChangePasswordResponse>success(userService.changePassword(id, request));
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('USER:DELETE')")
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
