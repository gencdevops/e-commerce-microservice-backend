package com.fmss.basketservice.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmss.basketservice.exception.*;
import com.fmss.commondata.model.ErrorBody;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    private ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        ErrorBody message = null;
        try (InputStream bodyIs = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, ErrorBody.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }

        return switch (response.status()) {
            case 400 -> new BadRequestException(message.getErrorDescription());
            case 403 -> new ForbiddenException(message.getErrorDescription());
            case 404 -> new NotFoundException(message.getErrorDescription());
            case 409 -> new ConflictException(message.getErrorDescription());
            case 412 -> new PreconditionException(message.getErrorDescription());
            case 429 -> new TooManyRequestException(message.getErrorDescription());
            case 500 -> new InternalServerException(message.getErrorDescription());
            default -> errorDecoder.decode(methodKey, response);
        };
    }
}
