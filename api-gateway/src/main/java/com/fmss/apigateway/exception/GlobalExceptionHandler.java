package com.fmss.apigateway.exception;

import com.turkcell.dcs.corelib.exception.DCBusinessException;
import com.turkcell.dcs.corelib.exception.DCServiceCallerException;
import com.turkcell.dcs.corelib.managers.RedisPmManager;
import com.turkcell.dcs.gncgateway.clientdtos.response.exception.GlobalExceptionResponseDto;
import com.turkcell.dcs.gncgateway.model.response.ButtonDto;
import com.turkcell.dcs.gncgateway.model.response.EnumType;
import com.turkcell.dcs.gncgateway.model.response.PopupDto;
import com.turkcell.dcs.gncgateway.util.LogGwInformationUtil;
import com.turkcell.log.util.logService.LogGwService;
import com.turkcell.log.util.model.LogGwEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;

import java.util.Objects;

import static com.turkcell.dcs.gncgateway.constants.CommonConstants.GENEREAL_PAGE_MANAGER;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final LogGwService logGwService;

    private final RedisPmManager redisPmManager;

    private static final String ERROR_POPUP_TITLE = "gnc.error.popup.title";

    public GlobalExceptionHandler(LogGwService logGwService, RedisPmManager redisPmManager) {
        this.logGwService = logGwService;
        this.redisPmManager = redisPmManager;
    }

    @ExceptionHandler({PopupException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<GlobalExceptionResponseDto> popupException(ServerWebExchange serverWebExchange, PopupException ex) {
        PopupDto popupDto = createStandartErrorPopupDtoWithDescription(ex.getMessage());
        GlobalExceptionResponseDto baseResponseDtoRestResponse =
                new GlobalExceptionResponseDto(HttpStatus.NOT_FOUND.value(),popupDto);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponseDtoRestResponse);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<GlobalExceptionResponseDto> serviceDataNotFoundException(ServerWebExchange serverWebExchange, ServiceDataNotFoundException ex) {
        PopupDto popupDto = createStandartErrorPopupDtoWithDescription(ex.getMessage());//Bu mesaj microservice içerisinden özel olarak geliyor.
        GlobalExceptionResponseDto baseResponseDtoRestResponse =
                new GlobalExceptionResponseDto(HttpStatus.NOT_FOUND.value(), popupDto);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(baseResponseDtoRestResponse);
    }

    @ExceptionHandler(UserException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<GlobalExceptionResponseDto> userNotFoundException(ServerWebExchange serverWebExchange, UserException ex) {

        PopupDto popupDto = createStandartErrorPopupDto();
        GlobalExceptionResponseDto baseResponseDtoRestResponse =
                new GlobalExceptionResponseDto(HttpStatus.UNAUTHORIZED.value(), popupDto);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(baseResponseDtoRestResponse);
    }
    @ExceptionHandler(CrackServiceNotAvailableException.class)
    @ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
    public ResponseEntity<GlobalExceptionResponseDto> crackServiceNotAvailableException(ServerWebExchange serverWebExchange, CrackServiceNotAvailableException ex) {
        PopupDto popupDto = createStandartErrorPopupDto();
        GlobalExceptionResponseDto baseResponseDtoRestResponse =
                new GlobalExceptionResponseDto(HttpStatus.NOT_IMPLEMENTED.value(), popupDto);
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(baseResponseDtoRestResponse);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<GlobalExceptionResponseDto> handleWebClientRequestException(ServerWebExchange serverWebExchange, WebClientRequestException ex) {
        LogGwEvent logGwEvent = LogGwEvent.builder().logMessage("Exception occurred in Controller").throwable(ex).build();
        LogGwInformationUtil.setLogGwEventInformation(logGwEvent, serverWebExchange);
        logGwService.logError(logGwEvent);
        PopupDto popupDto = createStandartErrorPopupDto();
        GlobalExceptionResponseDto baseResponseDtoRestResponse =
                new GlobalExceptionResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), popupDto);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDtoRestResponse);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<GlobalExceptionResponseDto> handleWebClientResponseException(ServerWebExchange serverWebExchange, WebClientResponseException ex) {
        LogGwEvent logGwEvent = LogGwEvent.builder().logMessage("WebClientResponseException occurred in Controller").throwable(ex).build();
        LogGwInformationUtil.setLogGwEventInformation(logGwEvent, serverWebExchange);
        logGwService.logError(logGwEvent);
        PopupDto popupDto = createStandartErrorPopupDto();
        GlobalExceptionResponseDto baseResponseDtoRestResponse =
                new GlobalExceptionResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), popupDto);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDtoRestResponse);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<GlobalExceptionResponseDto> handleWebClientResponseException(ServerWebExchange serverWebExchange, WebExchangeBindException ex) {
        LogGwEvent logGwEvent = LogGwEvent.builder().logMessage("WebExchangeBindException occurred in Controller").throwable(ex).build();
        LogGwInformationUtil.setLogGwEventInformation(logGwEvent, serverWebExchange);
        logGwService.logError(logGwEvent);
        String fieldError = getFieldError(ex.getFieldError());
        PopupDto popupDto = Objects.nonNull(fieldError) ? createStandartErrorPopupDtoWithDescription(fieldError) : createStandartErrorPopupDto();
        GlobalExceptionResponseDto baseResponseDtoRestResponse =
                new GlobalExceptionResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), popupDto);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDtoRestResponse);
    }

    private String getFieldError(FieldError fieldError) {
        if(Objects.nonNull(fieldError) && Objects.nonNull(fieldError.getDefaultMessage())){
            return fieldError.getDefaultMessage();
        }
        return null;
    }
    @ExceptionHandler
    public ResponseEntity<GlobalExceptionResponseDto> crackException(ServerWebExchange serverWebExchange, CrackException ex) {
        LogGwEvent logGwEvent = LogGwEvent.builder().logMessage("Crack API occurred exception in Controller").throwable(ex).build();
        LogGwInformationUtil.setLogGwEventInformation(logGwEvent, serverWebExchange);
        logGwService.logError(logGwEvent);
        PopupDto popupDto = createCustomErrorPopupDto(ex.getMessage(), redisPmManager.getLabel(GENEREAL_PAGE_MANAGER, ERROR_POPUP_TITLE, "Hata"));
        GlobalExceptionResponseDto baseResponseDtoRestResponse =
                new GlobalExceptionResponseDto(HttpStatus.UNPROCESSABLE_ENTITY.value(), popupDto);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(baseResponseDtoRestResponse);
    }

    @ExceptionHandler(DCBusinessException.class)
    public ResponseEntity<GlobalExceptionResponseDto> dcBusinessException(ServerWebExchange serverWebExchange, DCBusinessException ex) {
        HttpStatus httpStatus = ex.getErrorCode() == 0 ? HttpStatus.BAD_REQUEST : HttpStatus.valueOf(ex.getErrorCode());
        PopupDto popupDto = createStandartErrorPopupDto();
        GlobalExceptionResponseDto baseResponseDtoRestResponse =
                new GlobalExceptionResponseDto(httpStatus.value(), popupDto);
        return ResponseEntity.status(httpStatus).body(baseResponseDtoRestResponse);
    }

    @ExceptionHandler(GncUpException.class)
    public ResponseEntity<GlobalExceptionResponseDto> gncUpException(GncUpException ex) {
        String errorMessage = redisPmManager.getError(GENEREAL_PAGE_MANAGER, "gnc.up.error.message", "GNC Up bilgilerin güncellenemedi. Daha sonra tekrar dene!");
        PopupDto popupDto = createStandartErrorPopupDtoWithDescription(errorMessage);
        GlobalExceptionResponseDto baseResponseDtoRestResponse =
                new GlobalExceptionResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), popupDto);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDtoRestResponse);
    }
    @ExceptionHandler(DCServiceCallerException.class)
    public ResponseEntity<GlobalExceptionResponseDto> dcBusinessException(ServerWebExchange serverWebExchange, DCServiceCallerException ex) {
        HttpStatus httpStatus = ex.getErrorCode() == 0 ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.valueOf(ex.getErrorCode());
        PopupDto popupDto = createStandartErrorPopupDto();
        GlobalExceptionResponseDto baseResponseDtoRestResponse =
                new GlobalExceptionResponseDto(httpStatus.value(), popupDto);
        return ResponseEntity.status(httpStatus).body(baseResponseDtoRestResponse);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalExceptionResponseDto> generalException(ServerWebExchange serverWebExchange, Exception ex) {
        LogGwEvent logGwEvent = LogGwEvent.builder().logMessage("General exception in Controller").throwable(ex).build();
        LogGwInformationUtil.setLogGwEventInformation(logGwEvent, serverWebExchange);
        logGwService.logError(logGwEvent);
        PopupDto popupDto = createCustomErrorPopupDto(ex.getMessage(), redisPmManager.getLabel(GENEREAL_PAGE_MANAGER, ERROR_POPUP_TITLE, "Hata"));
        GlobalExceptionResponseDto baseResponseDtoRestResponse =
                new GlobalExceptionResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), popupDto);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(baseResponseDtoRestResponse);
    }

    private PopupDto createStandartErrorPopupDto() {
        String errorStandartDescription = redisPmManager.getLabel(GENEREAL_PAGE_MANAGER, "gnc.error.popup.description", "Bir hata oluştu.");
        String errorStandartTitle = redisPmManager.getLabel(GENEREAL_PAGE_MANAGER, ERROR_POPUP_TITLE, "Hata");
        String deeplinkButtonUrl1 = redisPmManager.getLabel(GENEREAL_PAGE_MANAGER, "gnc.error.popup.button1.url", "http://gnc/deeplink");
        String deeplinkButtonTitle1 = redisPmManager.getLabel(GENEREAL_PAGE_MANAGER, "gnc.error.popup.button1.title", "DeeplinkButton1");

        ButtonDto button = new ButtonDto(deeplinkButtonTitle1, deeplinkButtonUrl1);
        return new PopupDto(errorStandartTitle, errorStandartDescription, EnumType.FAIL, button, null);

    }

    private PopupDto createStandartErrorPopupDtoWithDescription(String errorStandartDescription) {
        String errorStandartTitle = redisPmManager.getLabel(GENEREAL_PAGE_MANAGER, ERROR_POPUP_TITLE, "Hata");
        String deeplinkButtonUrl1 = redisPmManager.getLabel(GENEREAL_PAGE_MANAGER, "gnc.error.popup.button1.url", "http://gnc/deeplink");
        String deeplinkButtonTitle1 = redisPmManager.getLabel(GENEREAL_PAGE_MANAGER, "gnc.error.popup.button1.title", "DeeplinkButton1");

        ButtonDto button = new ButtonDto(deeplinkButtonTitle1, deeplinkButtonUrl1);
        return new PopupDto(errorStandartTitle, errorStandartDescription, EnumType.FAIL, button, null);

    }

    private PopupDto createCustomErrorPopupDto(String description, String title) {
        PopupDto standartErrorPopupDto = createStandartErrorPopupDto();
        standartErrorPopupDto.withTitleDescription(title, description);

        return standartErrorPopupDto;

    }

}
