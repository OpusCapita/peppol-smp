package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.List;

public class TickstarEndpointListResponse {

    private List<TickstarEndpointListEndpoint> endpoint;

    public List<TickstarEndpointListEndpoint> getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(List<TickstarEndpointListEndpoint> endpoint) {
        this.endpoint = endpoint;
    }
}
