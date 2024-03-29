package com.slowv.fruit.web.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
  String timestamp;
  int code;
  String message;
  List<ApiSubError> apiSubErrors;

  public ApiError() {
    this.timestamp = Instant.now().toString();
  }

  public ApiError(String message) {
    this();
    this.message = message;
  }

  public ApiError(int code) {
    this();
    this.code = code;
  }

  public ApiError(String message, int code) {
    this(message);
    this.code = code;
  }

  private void addSubError(ApiSubError subError) {
    if (apiSubErrors == null) {
      apiSubErrors = new ArrayList<>();
    }
    apiSubErrors.add(subError);
  }

  private void addValidationError(String object, String field, Object rejectedValue, String message) {
    addSubError(new ApiValidationError<>(object, field, rejectedValue, message));
  }

  private void addValidationError(String object, String message) {
    addSubError(new ApiValidationError<>(object, message));
  }

  private void addValidationError(FieldError fieldError) {
    this.addValidationError(
        fieldError.getObjectName(),
        fieldError.getField(),
        fieldError.getRejectedValue(),
        fieldError.getDefaultMessage());
  }

  public void addValidationErrors(List<FieldError> fieldErrors) {
    fieldErrors.forEach(this::addValidationError);
  }

  private void addValidationError(ObjectError objectError) {
    this.addValidationError(
        objectError.getObjectName(),
        objectError.getDefaultMessage());
  }

  public void addValidationError(List<ObjectError> globalErrors) {
    globalErrors.forEach(this::addValidationError);
  }

  /**
   * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails.
   *
   * @param cv the ConstraintViolation
   */
  private void addValidationError(ConstraintViolation<?> cv) {
    this.addValidationError(
        cv.getRootBeanClass().getSimpleName(),
        ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
        cv.getInvalidValue(),
        cv.getMessage());
  }

  public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
    constraintViolations.forEach(this::addValidationError);
  }

}
