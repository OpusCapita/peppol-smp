package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.Endpoint;
import com.opuscapita.peppol.smp.entity.Smp;

public interface EndpointService {

    Endpoint getEndpoint(Smp smp);

    void saveEndpoint(Endpoint endpoint);
}
