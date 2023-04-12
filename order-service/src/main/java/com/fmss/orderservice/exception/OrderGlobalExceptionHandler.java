package com.fmss.orderservice.exception;



import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.el.MethodNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
public class OrderGlobalExceptionHandler {


    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorBody.class),
                            mediaType = "application/json"))})
    @ExceptionHandler(value = {NoHandlerFoundException.class, MethodNotFoundException.class})
    public ResponseEntity<ErrorBody> processNoHandlerFoundException(final Exception exception, final HttpServletRequest request) {
        return responseEntity(ErrorBody.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .errorDescription((HttpStatus.NOT_FOUND.getReasonPhrase()))
                .build());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            schema = @Schema(implementation = ErrorBody.class),
                            mediaType = "application/json"))})
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorBody> processBadRequestException(final Exception exception, final HttpServletRequest request) {
        return responseEntity(ErrorBody.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .errorDescription(exception.getMessage())
                .build());
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorBody.class),
                            mediaType = "application/json"))})
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorBody> processException(final Exception exception, final HttpServletRequest request) {

        return responseEntity(ErrorBody.builder()
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorDescription(exception.getMessage())
                .build());
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "405",
                    description = "Method Not Allowed",
                    content = @Content(
                            schema = @Schema(implementation = ErrorBody.class),
                            mediaType = "application/json"))})
    @ExceptionHandler(value = {MethodNotAllowedException.class, HttpClientErrorException.MethodNotAllowed.class})
    public ResponseEntity<ErrorBody> processMethodNotAllowedException(final Exception exception, final HttpServletRequest request) {
        return responseEntity(ErrorBody.builder()
                .errorCode(HttpStatus.METHOD_NOT_ALLOWED.value())
                .errorDescription(exception.getMessage())
                .build());
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",
                    description = "Method Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorBody.class),
                            mediaType = "application/json"))})
    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorBody> orderNotFoundException( OrderNotFoundException exception) {
        return responseEntity(ErrorBody.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .errorDescription(exception.getMessage())
                .build());
    }




    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorBody> orderNotFoundException( OrderNotFoundException exception) {
        return responseEntity(ErrorBody.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .errorDescription(exception.getMessage())
                .build());
    }




    @ApiResponses(value = {
            @ApiResponse(responseCode = "500",
                    description = "Order payload deserialize exception",
                    content = @Content(
                            schema = @Schema(implementation = ErrorBody.class),
                            mediaType = "application/json"))})
    @ExceptionHandler(OrderPayloadDeserializeException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorBody> orderPayloadDeserializeException( OrderPayloadDeserializeException exception) {
        return responseEntity(ErrorBody.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .errorDescription(exception.getMessage())
                .build());
    }

    private ResponseEntity<ErrorBody> responseEntity(ErrorBody error) {
        return new ResponseEntity(error, HttpStatus.valueOf(error.getErrorCode()));
    }
}
