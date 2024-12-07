package com.katabank.exception.config;

import com.katabank.exception.BasicApiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.katabank.dto.Error;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionHandlerConfig  extends ResponseEntityExceptionHandler {

    private static final String FIELD_NAME_MESSAGE = "message";
    private static final String FIELD_NAME_ERROR = "error";

    @ExceptionHandler(value = HttpClientErrorException.class)
    public ResponseEntity<Object> handleAllUncaughtException(
            HttpClientErrorException ex,
            WebRequest request) {

        logger.error(ex.getMessage());
        return generateBody(ex, ex.getStatusCode());
    }

    @ExceptionHandler(value = HttpClientErrorException.NotFound.class)
    public ResponseEntity<Object> handleNotFoundClientException(
            HttpClientErrorException ex,
            WebRequest request) {

        logger.error(ex.getMessage());
        return generateBody(ex, ex.getStatusCode(), extractErrorMessage(ex.getResponseBodyAsString()));
    }

    @ExceptionHandler(value = BasicApiException.class)
    public ResponseEntity<Object> handleAllUncaughtException(
            BasicApiException ex,
            WebRequest request) {

        logger.error(ex.getMessage());
        return generateBody(ex, ex.getStatus());
    }

    @ExceptionHandler(value = HttpServerErrorException.class)
    public ResponseEntity<Object> handlerHttpServerErrorException(
            HttpServerErrorException ex, WebRequest request) {

        logger.error(ex.getMessage());
        return generateBody(ex, ex.getStatusCode());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> handleException(ConstraintViolationException ex, WebRequest request) {

        logger.error(ex.getMessage());
        return generateBody(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MissingRequestHeaderException.class)
    public ResponseEntity<Object> handleException(MissingRequestHeaderException ex, WebRequest request) {

        logger.error(ex.getMessage());
        return generateBody(ex, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return generateBody(new Exception(ex.getMessage() + ". SupportedMediaTypes: " + ex.getSupportedMediaTypes()), status);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return generateBody(ex, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return generateBody(ex, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return generateBody(ex, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return generateBody(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return generateBody(ex, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return generateBody(ex, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body,
            HttpHeaders headers, HttpStatus status,
            WebRequest request
    ) {
        return generateBody(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> generateBody(Exception ex, HttpStatus statusCode) {
        return generateBody(ex, statusCode, null);
    }

    private ResponseEntity<Object> generateBody(Exception ex, HttpStatus statusCode, String message) {
        return new ResponseEntity<>(
                Error.builder()
                        .message(message==null?ex.getMessage():message)
                        .build(),
                statusCode
        );
    }

    private String extractErrorMessage(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            if (jsonNode.has(FIELD_NAME_MESSAGE)) {
                return jsonNode.get(FIELD_NAME_MESSAGE).asText();
            }else if (jsonNode.has(FIELD_NAME_ERROR) && jsonNode.get(FIELD_NAME_ERROR).has(FIELD_NAME_MESSAGE)) {
                return jsonNode.get(FIELD_NAME_ERROR).get(FIELD_NAME_MESSAGE).asText();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return "Unknown error";
    }
}
