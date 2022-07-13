package com.egs.core.bank.exception;


import com.egs.core.bank.exception.error.ApiError;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.rmi.ServerException;
import java.util.Optional;

import static javax.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static javax.servlet.RequestDispatcher.ERROR_MESSAGE;
import static javax.servlet.RequestDispatcher.ERROR_REQUEST_URI;
import static javax.servlet.RequestDispatcher.ERROR_STATUS_CODE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.resolve;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;
import static org.springframework.web.servlet.HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE;

@Slf4j
@RestControllerAdvice
public final class ApiErrorExceptionHandler extends ResponseEntityExceptionHandler {

    private static String extractMessage(final Throwable exception) {
        if (exception.getCause() == null) {
            return exception.getMessage();
        } else {
            return extractMessage(exception.getCause());
        }
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        var apiError = buildApiError(status, "Validation Error", request);
        apiError.addFieldErrors(ex.getBindingResult().getFieldErrors());
        apiError.addGlobalErrors(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        return handleExceptionInternal(ex, null, headers, BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return buildResponseEntity(buildApiError(status, ex, request));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return handleConstraintViolation((ConstraintViolationException) ex.getCause(), request);
        }
        return buildResponseEntity(buildApiError(INTERNAL_SERVER_ERROR, ex, request));
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Object> handleServerException(ServerException ex, WebRequest request) {
        if (ex.getCause() instanceof ServerException) {
            return handleGenericException((ServerException) ex.getCause(), request);
        }
        return buildResponseEntity(buildApiError(INTERNAL_SERVER_ERROR, ex, request));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        var apiError = buildApiError(BAD_REQUEST, "Validation Error", request);
        apiError.addConstraintViolations(ex.getConstraintViolations());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        return buildResponseEntity(buildApiError(FORBIDDEN, ex, request));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MaxUploadSizeExceededException.class
    })
    public ResponseEntity<Object> handleIllegalArgument(RuntimeException ex, WebRequest request) {
        return buildResponseEntity(buildApiError(BAD_REQUEST, ex, request));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalState(IllegalStateException ex, WebRequest request) {
        return buildResponseEntity(buildApiError(CONFLICT, ex, request));
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<Object> handleGenericException(GenericException ex, WebRequest request) {
        var exceptionEnum = ex.getReason();
        return buildResponseEntity(buildApiError(ex.getStatus(), exceptionEnum.getMessage(), extractMessage(ex), request));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
        return buildResponseEntity(buildApiError(NOT_FOUND, ex, request));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException ex, WebRequest request) {
        return buildResponseEntity(buildApiError(HttpStatus.valueOf(ex.status()), ex, request));
    }

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<Object> handleResponseException(ResponseException ex, WebRequest request) {
        return buildResponseEntity(buildApiError(HttpStatus.valueOf(ex.getStatus()), ex, request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        return buildResponseEntity(buildApiError(INTERNAL_SERVER_ERROR, ex, request));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getHttpStatus());
    }

    public ResponseEntity<Object> buildResponseEntity(WebRequest request) {
        var statusCode = (Integer) request.getAttribute(ERROR_STATUS_CODE, SCOPE_REQUEST);
        var status = statusCode != null ? resolve(statusCode) : INTERNAL_SERVER_ERROR;
        return buildResponseEntity(buildApiError(status, getErrorMessage(request), request));
    }

    private ApiError buildApiError(@Nullable HttpStatus status, String message, WebRequest request) {
        log.error(message);
        return ApiError.from(status != null ? status : INTERNAL_SERVER_ERROR, message, getUrlPath(request));
    }

    private ApiError buildApiError(@Nullable HttpStatus status, String message, String description, WebRequest request) {
        log.error(message);
        return ApiError.from(status != null ? status : INTERNAL_SERVER_ERROR, message, description, getUrlPath(request));
    }

    private ApiError buildApiError(HttpStatus status, Exception ex, WebRequest request) {
        log.error("Exception occurred", ex);
        return ApiError.from(status, extractMessage(ex), getUrlPath(request));
    }

    @Nullable
    private String getUrlPath(WebRequest request) {
        var pathFromHandler = (String) request.getAttribute(PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, SCOPE_REQUEST);
        var pathFromServlet = (String) request.getAttribute(ERROR_REQUEST_URI, SCOPE_REQUEST);
        return StringUtils.hasText(pathFromServlet) ? pathFromServlet : pathFromHandler;
    }

    private String getErrorMessage(WebRequest request) {
        var exception = (Exception) request.getAttribute(ERROR_EXCEPTION, SCOPE_REQUEST);
        var message = (String) request.getAttribute(ERROR_MESSAGE, SCOPE_REQUEST);
        return StringUtils.hasText(message) ? message : Optional.ofNullable(exception).map(ApiErrorExceptionHandler::extractMessage).orElse("");
    }
}
