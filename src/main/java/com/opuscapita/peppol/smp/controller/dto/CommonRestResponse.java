package com.opuscapita.peppol.smp.controller.dto;

public class CommonRestResponse {

    private String message;

    public CommonRestResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
