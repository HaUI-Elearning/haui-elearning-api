package com.elearning.haui.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.elearning.haui.domain.response.RestResponse;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {
            Exception.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            IdInvalidException.class,
            RuntimeException.class,
            IllegalArgumentException.class 
    })
    public ResponseEntity<RestResponse<Object>> handleIdException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("Exception occurs... ");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();

    // Gom lỗi dưới dạng key: message (tuỳ bạn muốn list hay string)
    List<String> errors = fieldErrors.stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());

    RestResponse<Object> res = new RestResponse<>();
    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    res.setError("Validation error");
    res.setMessage(errors.size() == 1 ? errors.get(0) : errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
}


}
