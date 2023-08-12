package com.example.watchex.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Vanh
 * @version 1.0
 * @date 10/6/2021 1:23 AM
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler({AccessDeniedException.class})
    protected ResponseEntity<MessageEntity> processSecurityError(Exception ex) {
        ex.printStackTrace();
        MessageEntity msg = new MessageEntity(null, "Bạn không có quyền thực hiện chức năng này!", MessageType.ERROR);
        return new ResponseEntity<>(msg, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ValidException.class})
    protected ResponseEntity<MessageEntity> handleValid(final RuntimeException ex) {
        ex.printStackTrace();
        MessageEntity msg = new MessageEntity(null, 400, ex.getMessage(), MessageType.ERROR);
        return ResponseEntity.ok().body(msg);
    }

    @ExceptionHandler({BindException.class})
    protected ResponseEntity<MessageEntity> handleBindException(BindException e, Locale locale) {
        String errorMessage = "Tham số không hợp lệ!";
        if (e.getBindingResult().hasErrors())
            errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.ok().body(new MessageEntity(400, errorMessage));
    }

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<MessageEntity> handleNotFound(final RuntimeException ex) {
        ex.printStackTrace();
        MessageEntity msg = new MessageEntity((String) null, ex.getMessage(), MessageType.ERROR);
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidDataAccessApiUsageException.class, DataAccessException.class})
    protected ResponseEntity<MessageEntity> handleConflict(final RuntimeException ex, Locale locale) {
        ex.printStackTrace();
        MessageEntity msg = new MessageEntity(null, 500, "409 Conflict", MessageType.ERROR);
        return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<MessageEntity> handleHttpRequestNotSupported(RuntimeException ex) {
        ex.printStackTrace();
        MessageEntity msg = new MessageEntity(null, 400, ex.getMessage(), MessageType.ERROR);
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<MessageEntity> handleInternal(final RuntimeException ex, Locale locale) {
        ex.printStackTrace();
        MessageEntity msg = new MessageEntity(null, 500, ex.getMessage(), MessageType.ERROR);
        return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private MessageEntity processFieldError(FieldError error, Locale locale) {
        MessageEntity message = null;
        if (error != null) {
            String msg = error.getDefaultMessage();
            message = new MessageEntity(error.getField(), 400, msg, MessageType.ERROR);
        }

        return message;
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<MessageEntity> handleAllException(Exception ex) {
        ex.printStackTrace();
        // todos: log database
        MessageEntity msg = new MessageEntity(null, "Đã có lỗi xảy ra, lỗi đã được thông báo đến admin, mã lỗi []!", MessageType.ERROR);
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }

}
