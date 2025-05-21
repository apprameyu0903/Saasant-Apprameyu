package com.thirdspring1905.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataNotFoundException.class)
    public String onNotFound(DataNotFoundException ex, RedirectAttributes attrs) {
        log.error("Data not found", ex);
        attrs.addFlashAttribute("error", ex.getMessage());
        return "redirect:/employees";
    }

    @ExceptionHandler(BusinessException.class)
    public String onBusinessError(BusinessException ex, RedirectAttributes attrs) {
        log.error("Business error", ex);
        attrs.addFlashAttribute("error", ex.getMessage());
        return "redirect:/employees/add";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> onAnyError(Exception ex) {
        log.error("Unexpected error", ex);
        Map<String, Object> body = Map.of(
            "message", "An unexpected error occurred",
            "status", HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body);
    }
}
