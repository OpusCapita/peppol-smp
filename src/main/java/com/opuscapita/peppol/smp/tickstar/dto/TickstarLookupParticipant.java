package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.List;

public class TickstarLookupParticipant {

    private String idType;
    private String idValue;
    private String idScheme;
    private Integer responseTime;
    private List<TickstarLookupEndpoint> endpoint;

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdValue() {
        return idValue;
    }

    public void setIdValue(String idValue) {
        this.idValue = idValue;
    }

    public String getIdScheme() {
        return idScheme;
    }

    public void setIdScheme(String idScheme) {
        this.idScheme = idScheme;
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public List<TickstarLookupEndpoint> getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(List<TickstarLookupEndpoint> endpoint) {
        this.endpoint = endpoint;
    }
}
