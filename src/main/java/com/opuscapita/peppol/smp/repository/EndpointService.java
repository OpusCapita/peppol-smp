package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.Endpoint;

public interface EndpointService {

    Endpoint getEndpoint(SmpName smpName);

    Endpoint getEndpoint(SmpName smpName, EndpointType endpointType);

    void saveEndpoint(Endpoint endpoint);
}
