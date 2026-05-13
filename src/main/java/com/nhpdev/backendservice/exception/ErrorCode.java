package com.nhpdev.backendservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNAUTHORIZED(401, "Token invalid", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "Access denied", HttpStatus.FORBIDDEN),

    REFRESH_TOKEN_IS_BLANK(400, "Refresh token is blank", HttpStatus.BAD_REQUEST),
    GENERATE_TOKEN_FAILED(500, "Generate token failed", HttpStatus.INTERNAL_SERVER_ERROR),
    TOKEN_EXPIRED(401, "Token expired", HttpStatus.UNAUTHORIZED),

    USER_EXISTED(409, "User already existed", HttpStatus.CONFLICT),
    USER_NOT_EXISTED(404, "User not existed", HttpStatus.NOT_FOUND),

    ROLE_EXISTED(409, "Role already exist", HttpStatus.CONFLICT),
    ROLE_NOT_EXISTED(409, "Role not existed", HttpStatus.NOT_FOUND),

    PERMISSION_EXISTED(409, "Permission already exist", HttpStatus.CONFLICT),
    PERMISSION_NOT_EXISTED(409, "Permission not existed", HttpStatus.NOT_FOUND),

    USERNAME_EXISTED(409, "This username is being used", HttpStatus.CONFLICT)
    ;
    private final int code;
    private final String message;
    private final HttpStatus status;
}
