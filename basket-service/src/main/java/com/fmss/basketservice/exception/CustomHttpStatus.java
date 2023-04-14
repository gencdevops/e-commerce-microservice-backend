package com.fmss.basketservice.exception;

import org.springframework.http.HttpStatus;

public enum CustomHttpStatus {
    CUSTOM_HTTP_STATUS(434, HttpStatus.Series.CLIENT_ERROR, "Customized");

    private final int value;

    private final HttpStatus.Series series;

    private final String reasonPhrase;

    CustomHttpStatus(int value, HttpStatus.Series series, String reasonPhrase) {
        this.value = value;
        this.series = series;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return value;
    }

    public boolean is1xxInformational() {
        return false;
    }

    public boolean is2xxSuccessful() {
        return false;
    }

    public boolean is3xxRedirection() {
        return false;
    }

    public boolean is4xxClientError() {
        return true;
    }

    public boolean is5xxServerError() {
        return false;
    }

    public boolean isError() {
        return true;
    }
}
