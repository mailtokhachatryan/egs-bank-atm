package com.egs.core.bank.exception.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
public class ApiError {

    private final int status;
    private final UUID reference = UUID.randomUUID();
    private final Set<ErrorItem> errorItems = new HashSet<>();
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime timestamp = LocalDateTime.now();
    @JsonIgnore
    private final HttpStatus httpStatus;
    private String description;
    private String message = "No message available";
    private String href = "No URL";

    private ApiError(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        this.description = httpStatus.getReasonPhrase();
    }

    public static ApiError from(HttpStatus httpStatus, @Nullable String message, @Nullable String path) {
        ApiError apiError = new ApiError(httpStatus);
        apiError.setMessage(message);
        apiError.setHref(path);
        return apiError;
    }

    public static ApiError from(HttpStatus httpStatus, @Nullable String message, String description, @Nullable String path) {
        ApiError apiError = new ApiError(httpStatus);
        apiError.setMessage(message);
        apiError.setDescription(description);
        apiError.setHref(path);
        return apiError;
    }

    private void setMessage(String message) {
        if (StringUtils.hasText(message)) {
            this.message = message;
        }
    }

    private void setHref(String href) {
        if (StringUtils.hasText(href)) {
            this.href = href;
        }
    }

    private void setDescription(String description) {
        if (StringUtils.hasText(description)) {
            this.description = description;
        }
    }

    public void addFieldErrors(List<FieldError> fieldErrors) {
        fieldErrors.stream()
                .map(ErrorItem::from)
                .forEach(errorItems::add);
    }

    public void addGlobalErrors(List<ObjectError> objectErrors) {
        objectErrors.stream()
                .map(ErrorItem::from)
                .forEach(errorItems::add);
    }

    public void addConstraintViolations(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.stream()
                .map(ErrorItem::from)
                .forEach(errorItems::add);
    }
}
