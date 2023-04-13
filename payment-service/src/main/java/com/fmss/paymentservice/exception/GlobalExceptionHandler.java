package com.fmss.paymentservice.exception;

import com.fmss.commondata.model.ErrorBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ApiResponse(responseCode = "400", description = "Bad Request Error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorBody.class)))
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorBody catchBadRequestException(BadRequestException exception) {
        log.error("Bad Request Exception {}", exception.toString());
        return ErrorBody.builder().errorCode(400).errorDescription(exception.getMessage()).build();
    }

    @ApiResponse(responseCode = "403", description = "Forbidden Error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorBody.class)))
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorBody catchForbiddenException(ForbiddenException exception) {
        log.error("Forbidden Exception {}", exception.toString());
        return ErrorBody.builder().errorCode(403).errorDescription(exception.getMessage()).build();
    }

    @ApiResponse(responseCode = "404", description = "Not Found Error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorBody.class)))
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorBody catchNotFoundException(NotFoundException exception) {
        log.error("Not Found Exception {}", exception.toString());
        return ErrorBody.builder().errorCode(404).errorDescription(exception.getMessage()).build();
    }

    @ApiResponse(responseCode = "409", description = "Conflict Error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorBody.class)))
    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorBody catchConflictException(ConflictException exception) {
        log.error("Conflict Exception {}", exception.toString());
        return ErrorBody.builder().errorCode(409).errorDescription(exception.getMessage()).build();
    }

    @ApiResponse(responseCode = "412", description = "Precondition Error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorBody.class)))
    @ExceptionHandler(PreconditionException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public ErrorBody catchPreconditionException(PreconditionException exception) {
        log.error("Precondition Exception {}", exception.toString());
        return ErrorBody.builder().errorCode(412).errorDescription(exception.getMessage()).build();
    }

    @ApiResponse(responseCode = "429", description = "Too Many Request Error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorBody.class)))
    @ExceptionHandler(TooManyRequestException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ErrorBody catchPreconditionException(TooManyRequestException exception) {
        log.error("Too Many Request Exception {}", exception.toString());
        return ErrorBody.builder().errorCode(429).errorDescription(exception.getMessage()).build();
    }



    @ApiResponse(responseCode = "500", description = "Internal Error",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorBody.class)))
    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorBody catchInternalServerException(InternalServerException exception) {
        log.error("Internal Server Exception {}", exception.toString());
        return ErrorBody.builder().errorCode(500).errorDescription(exception.getMessage()).build();
    }


}
