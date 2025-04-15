package com.hopematch.hopematch_backend.exceptions;

import com.hopematch.hopematch_backend.utils.LogHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex, HttpServletRequest request) {
        LogHelper.error("⚠️ [ERROR 500] " + request.getRequestURI() + " - " + ex.getMessage());
        ex.printStackTrace(); // opcional, para ver en consola
        return new ResponseEntity<>("Ocurrió un error interno.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(org.springframework.web.servlet.NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFound(HttpServletRequest request) {
        LogHelper.warn("❌ [ERROR 404] Ruta no encontrada: " + request.getRequestURI());
        return new ResponseEntity<>("Recurso no encontrado.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        LogHelper.warn("🚫 [ERROR 400] " + request.getRequestURI() + " - " + ex.getMessage());
        return new ResponseEntity<>("Solicitud incorrecta.", HttpStatus.BAD_REQUEST);
    }
}
