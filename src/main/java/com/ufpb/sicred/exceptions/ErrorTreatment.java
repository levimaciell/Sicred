package com.ufpb.sicred.exceptions;

import com.ufpb.sicred.dto.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ErrorTreatment {

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorDto> serverError(HttpServletRequest request){
//        ErrorDto dto = new ErrorDto(
//                LocalDateTime.now(),
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                List.of("Ocorreu um erro interno no servidor."),
//                request.getServletPath()
//        );
//
//        return ResponseEntity.internalServerError().body(dto);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> methodArgumentNotValidException
            (MethodArgumentNotValidException exception, HttpServletRequest request){
        ErrorDto dto = new ErrorDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                exception.getFieldErrors().stream()
                        .map(x -> {
                            return x.getField() + ": " + x.getDefaultMessage();
                        }).toList(),
                request.getServletPath()
        );

        return ResponseEntity.badRequest().body(dto);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> userNotFoundException
            (UserNotFoundException exception, HttpServletRequest request){
        ErrorDto dto = new ErrorDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                List.of(exception.getMessage()),
                request.getServletPath()
        );

        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidUserCreationException.class)
    public ResponseEntity<ErrorDto> InvalidUserCreationException
            (InvalidUserCreationException exception, HttpServletRequest request){

        ErrorDto dto = new ErrorDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT,
                List.of(exception.getMessage()),
                request.getServletPath()
        );

        return new ResponseEntity<>(dto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorDto> eventNotFoundException
            (EventNotFoundException exception, HttpServletRequest request){

        ErrorDto dto = new ErrorDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                List.of(exception.getMessage()),
                request.getServletPath()
        );

        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EventCreationException.class)
    public ResponseEntity<ErrorDto> eventCreationException
            (EventCreationException exception, HttpServletRequest request){

        ErrorDto dto = new ErrorDto(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED,
                List.of(exception.getMessage()),
                request.getServletPath()
        );

        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InscricaoNotFoundException.class)
    public ResponseEntity<ErrorDto> inscricaoNotFoundException
            (InscricaoNotFoundException exception, HttpServletRequest request){

        ErrorDto dto = new ErrorDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND,
                List.of(exception.getMessage()),
                request.getServletPath()
        );

        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> httpMessageNotReadable
            (HttpMessageNotReadableException exception, HttpServletRequest request) {

        ErrorDto dto = new ErrorDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST,
                List.of(exception.getMessage()),
                request.getServletPath()
        );

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

}
