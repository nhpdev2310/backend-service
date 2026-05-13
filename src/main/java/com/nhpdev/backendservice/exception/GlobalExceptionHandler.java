package com.nhpdev.backendservice.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // -----------------------------------------------------------------------
    // 1. App-specific exceptions
    // -----------------------------------------------------------------------

    @ExceptionHandler(BackendServiceException.class)
    public ResponseEntity<ErrorResponse> handlerBackendServiceException(
            BackendServiceException exception, WebRequest wRequest) {

        ErrorCode errorCode = exception.getErrorCode();
        String path = extractPath(wRequest);

        log.warn("[BackendServiceException] path={} code={} message={}",
                path, errorCode.getCode(), errorCode.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .error(errorCode.getStatus().getReasonPhrase())
                .timestamp(System.currentTimeMillis())
                .path(path)
                .build();
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    // -----------------------------------------------------------------------
    // 2. Validation: @Valid trên @RequestBody
    // -----------------------------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerValidationException(
            MethodArgumentNotValidException exception, WebRequest wRequest) {

        String path = extractPath(wRequest);
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        // Thêm tên field vào message để dễ debug: "email: must be valid, name: must not be blank"
        List<String> errors = fieldErrors.stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.toList());
        String errorMessage = String.join(", ", errors);

        log.warn("[ValidationException] path={} errors={}", path, errorMessage);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(System.currentTimeMillis())
                .path(path)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // -----------------------------------------------------------------------
    // 3. Validation: @Validated trên @PathVariable / @RequestParam
    // -----------------------------------------------------------------------

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException exception, WebRequest wRequest) {

        String path = extractPath(wRequest);

        String errorMessage = exception.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.joining(", "));

        log.warn("[ConstraintViolationException] path={} errors={}", path, errorMessage);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(System.currentTimeMillis())
                .path(path)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // -----------------------------------------------------------------------
    // 4. Malformed JSON / sai kiểu dữ liệu trong body
    // -----------------------------------------------------------------------

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadableException(
            HttpMessageNotReadableException ex, WebRequest wRequest) {  // Fix: thêm tham số ex để có thể log

        String path = extractPath(wRequest);
        log.warn("[HttpMessageNotReadableException] path={} cause={}", path, ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Malformed JSON request or invalid data format")
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(System.currentTimeMillis())
                .path(path)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // -----------------------------------------------------------------------
    // 5. Sai kiểu tham số (@PathVariable, @RequestParam)
    // -----------------------------------------------------------------------

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest wRequest) {

        String path = extractPath(wRequest);

        // Fix: getRequiredType() có thể null → NPE
        String requiredType = ex.getRequiredType() != null
                ? ex.getRequiredType().getSimpleName()
                : "unknown";

        String message = String.format(
                "Parameter '%s' with value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), requiredType);

        log.warn("[MethodArgumentTypeMismatchException] path={} {}", path, message);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(System.currentTimeMillis())
                .path(path)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // -----------------------------------------------------------------------
    // 6. Sai HTTP method (POST thay vì GET, v.v.)
    // -----------------------------------------------------------------------

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, WebRequest wRequest) {

        String path = extractPath(wRequest);
        log.warn("[HttpRequestMethodNotSupportedException] path={} method={}", path, ex.getMethod());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                .message(ex.getMessage())
                .error(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
                .timestamp(System.currentTimeMillis())
                .path(path)
                .build();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    // -----------------------------------------------------------------------
    // 7. Endpoint không tồn tại (404)
    // -----------------------------------------------------------------------

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
            NoResourceFoundException ex, WebRequest wRequest) {

        String path = extractPath(wRequest);
        log.warn("[NoResourceFoundException] path={}", path);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message("The requested resource was not found: " + path)
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .timestamp(System.currentTimeMillis())
                .path(path)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // -----------------------------------------------------------------------
    // 8. Sai Content-Type (gửi XML thay vì JSON, v.v.)
    // -----------------------------------------------------------------------

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex, WebRequest wRequest) {

        String path = extractPath(wRequest);
        log.warn("[HttpMediaTypeNotSupportedException] path={} contentType={}", path, ex.getContentType());

        String message = String.format("Content-Type '%s' is not supported. Supported types: %s",
                ex.getContentType(), ex.getSupportedMediaTypes());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .message(message)
                .error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase())
                .timestamp(System.currentTimeMillis())
                .path(path)
                .build();
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    // -----------------------------------------------------------------------
    // 9. Fallback — mọi exception chưa được xử lý ở trên
    // -----------------------------------------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUncaughtException(
            Exception exception, WebRequest wRequest) {

        String path = extractPath(wRequest);

        // KHÔNG expose exception.getMessage() ra client, chỉ log server-side
        log.error("[UnhandledException] path={} exception={}", path, exception.getMessage(), exception);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred. Please try again later.")
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .timestamp(System.currentTimeMillis())
                .path(path)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // -----------------------------------------------------------------------
    // Helper
    // -----------------------------------------------------------------------

    private String extractPath(WebRequest wRequest) {
        return wRequest.getDescription(false).replace("uri=", "");
    }
}