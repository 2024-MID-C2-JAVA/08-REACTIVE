package co.sofka.middleware;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
@ComponentScan(basePackages = "co.com.sofka")
public class ServiceErrorHandler {

    private static final Logger logger_ = LoggerFactory.getLogger(ServiceErrorHandler.class);


    @ExceptionHandler(value = {IllegalStateException.class})
    public Mono<Object> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getMessage());
        return Mono.just(errorMessage);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public Mono<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), HttpStatus.BAD_REQUEST.toString(), ex.getMessage());
        return Mono.just(errorMessage);      }



    @ExceptionHandler(value = {Exception.class})
    public Mono<Object> handleException(Exception ex, WebRequest request) {
        logger_.error("Error ExceptionHandler.: {}", ex);
        var message = "Error desconocido. Por favor, contactarse con el Administrador del Sistema";
        ErrorMessage errorMessage = new ErrorMessage(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), message);
        return Mono.just(errorMessage);
    }



    @ExceptionHandler(BindException.class)
    public Mono<?> handleBindException(BindException bindException) {

        Map<String, String> errorMap = new HashMap<>();

        bindException.getAllErrors().
                forEach(objectError -> {
                    errorMap.put(
                            ((FieldError) objectError).getField(),
                            objectError.getDefaultMessage());
                });

        return Mono.just(errorMap);
    }


}

