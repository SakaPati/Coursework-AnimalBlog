package server.io.github.fozeton.blog.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import server.io.github.fozeton.blog.exceptions.*;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        FieldError error = ex.getBindingResult().getFieldError();
        if (error != null) {
            return ResponseEntity.badRequest().body(new ErrorMessage(error.getDefaultMessage()));
        }
        return ResponseEntity.badRequest().body(new ErrorMessage("Unknown validation error"));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleUserExists(UserAlreadyExistsException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<ErrorMessage> handleAuthErrors(UserAuthenticationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler(UserAvatarFailedChangeException.class)
    public ResponseEntity<ErrorMessage> handleSaveFileErrors(UserAuthenticationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler({UserNotFoundException.class, PostNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleUserNotFoundErrors(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler(PostCreationException.class)
    public ResponseEntity<ErrorMessage> handlePostCreatingErrors(PostCreationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.badRequest().body(new ErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorMessage> handleInvalidTokenErrors(InvalidTokenException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessage("Unauthorized user"));
    }
}
