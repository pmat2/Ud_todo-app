package io.github.pmat.todoapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(annotations = IllegalExceptionProcessing.class)
public class GlobalControllerAdvise {

    Logger logger = LoggerFactory.getLogger(GlobalControllerAdvise.class);

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIAE(IllegalArgumentException exception) {
        logger.info("[handleIAE] - caught IllegalArgumentException");
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleISE(IllegalStateException exception) {
        logger.info("[handleISE] - caught IllegalStateException");
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
