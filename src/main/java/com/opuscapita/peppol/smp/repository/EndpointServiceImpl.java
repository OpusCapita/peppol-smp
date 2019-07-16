package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.Endpoint;
import com.opuscapita.peppol.smp.entity.Smp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EndpointServiceImpl implements EndpointService {

    private static final Logger logger = LoggerFactory.getLogger(EndpointServiceImpl.class);

    private final EndpointType type;
    private final EndpointRepository repository;

    @Autowired
    public EndpointServiceImpl(EndpointRepository repository) {
        this.type = EndpointType.PROD;
        this.repository = repository;
    }

    @Override
    public Endpoint getEndpoint(Smp smp) {
        return repository.findBySmp(smp).stream().filter(e -> type.equals(e.getType())).findFirst().orElse(null);
    }

    @Override
    public void saveEndpoint(Endpoint endpoint) {
        repository.save(endpoint);
    }
}
