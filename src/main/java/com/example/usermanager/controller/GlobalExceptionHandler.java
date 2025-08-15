package com.example.usermanager.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.usermanager.persistence.exception.ExistingUserConflict;
import com.example.usermanager.persistence.exception.NotFoundException;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(Exception ex, WebRequest request) {
        final String parameters = request.getParameterMap().entrySet().stream().map(x -> x.getKey() + "=" +
                Arrays.toString(x.getValue())).collect(Collectors.joining(";"));
        var exception = new NoResourceFoundException(((ServletWebRequest) request).getHttpMethod(), parameters);
        return handleNoResourceFoundException(exception, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ExistingUserConflict.class)
    public ResponseEntity<Object> handleConflictException(Exception ex, WebRequest request) {
        final ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Conflict with existing user");
        return handleExceptionInternal(ex, detail, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnknownException(Exception ex, WebRequest request) {
        final ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        return handleExceptionInternal(ex, detail, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            HttpServletResponse response = servletWebRequest.getResponse();
            if (response != null && response.isCommitted()) {
                if (this.logger.isWarnEnabled()) {
                    this.logger.warn("Response already committed. Ignoring: " + String.valueOf(ex));
                }

                return null;
            }
        }

        if (body == null && ex instanceof org.springframework.web.ErrorResponse errorResponse) {
            body = errorResponse.updateAndGetBody(getMessageSource(), LocaleContextHolder.getLocale());
        }

        if (body instanceof ProblemDetail detail){
            String exceptionMessage = getExceptionMessage(ex);
            if (exceptionMessage!= null && !exceptionMessage.isBlank()) {
                detail.setProperty("exception", ex.getClass().getSimpleName());
                detail.setProperty("message", exceptionMessage);
            }
        }
        return this.createResponseEntity(body, headers, statusCode, request);
    }

    public static String getExceptionMessage(Exception ex){
        return switch (ex) {
            case NoResourceFoundException e -> "";
            case ExistingUserConflict e -> e.getMessage().contains("Detail") ? e.getMessage()
                    .substring(e.getMessage().indexOf("Detail")) : "";
            case HandlerMethodValidationException e -> {
                var errors = e.getParameterValidationResults().stream()
                        .map(error->error.getMethodParameter().getParameterName()+":"+ error.getResolvableErrors().getFirst()
                                .getDefaultMessage()).toList();
                yield errors.toString();
            }
            case MethodArgumentNotValidException e -> {
                var errors = e.getBindingResult().getFieldErrors().stream().map(
                        error -> error.getRejectedValue()+":"+error.getDefaultMessage()).toList();
                yield errors.toString();
            }
            case Exception e -> e.getMessage();
        };
    }

    @Override
    protected ResponseEntity<Object> createResponseEntity(@Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        if (body instanceof ProblemDetail detail){
            detail.setType(URI.create("UserApi-V1"));
        }
        return new ResponseEntity(body, headers, statusCode);
    }

}

