package com.nhpdev.backendservice.service;

import com.nhpdev.backendservice.dto.request.UserChangePasswordRequest;
import com.nhpdev.backendservice.dto.request.UserCreateRequest;
import com.nhpdev.backendservice.dto.request.UserUpdateRequest;
import com.nhpdev.backendservice.dto.response.UserChangePasswordResponse;
import com.nhpdev.backendservice.dto.response.UserDetailResponse;
import com.nhpdev.backendservice.dto.response.UserUpdateResponse;
import com.nhpdev.backendservice.entity.User;

import java.util.List;

public interface UserService {
    //CRUD
    UserDetailResponse createUser(UserCreateRequest request);
    List<UserDetailResponse> getAllUser();
    UserDetailResponse getUserById(String id);
    UserUpdateResponse updateUser(String id ,UserUpdateRequest request);
    UserChangePasswordResponse changePassword (String id, UserChangePasswordRequest request);
    void deleteUser(String id);

}
