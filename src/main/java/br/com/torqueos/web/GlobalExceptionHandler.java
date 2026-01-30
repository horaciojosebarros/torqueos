package br.com.torqueos.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handle404(NoHandlerFoundException ex) {
    log.error("ROTA NÃO ENCONTRADA (404): {}", ex.getRequestURL(), ex);
    return "error/404"; // ou "error/generic" se você tiver
  }
}
