package nl.novi.vinylshop.helpers;

import nl.novi.vinylshop.exceptions.RecordNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<String> handleException(RecordNotFoundException ex){
        return ResponseEntity.notFound().build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> violations = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError:: getDefaultMessage)
                .toList();
        return ResponseEntity.badRequest().body(violations);
    }
}
