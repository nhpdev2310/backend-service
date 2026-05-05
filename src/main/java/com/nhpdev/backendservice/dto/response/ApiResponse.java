package com.nhpdev.backendservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T> {
    private boolean ok;
    private T data;
    private String message;
    private int code;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .ok(true)
                .code(HttpStatus.OK.value())
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                .ok(true)
                .code(HttpStatus.OK.value())
                .message("Success")
                .build();
    }
}
