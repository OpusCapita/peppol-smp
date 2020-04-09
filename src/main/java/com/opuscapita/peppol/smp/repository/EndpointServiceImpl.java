package com.opuscapita.peppol.smp.repository;

import com.opuscapita.peppol.smp.entity.Endpoint;
import com.opuscapita.peppol.smp.entity.Smp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EndpointServiceImpl implements EndpointService {

    private static final Logger logger = LoggerFactory.getLogger(EndpointServiceImpl.class);

    @Value("${type:prod}")
    private String type;

    private final SmpService smpService;
    private final EndpointType defaultType;
    private final EndpointRepository repository;

    @Autowired
    public EndpointServiceImpl(SmpService smpService, EndpointRepository repository) {
        this.smpService = smpService;
        this.repository = repository;
        this.defaultType = EndpointType.PROD;
//        this.defaultType = (type == "prod") ? EndpointType.PROD : EndpointType.TEST;

        logger.info("EndpointService started, mode: " + (type == null ? "null" : type));
    }

    @Override
    public Endpoint getEndpoint(SmpName smpName) {
        return getEndpoint(smpName, defaultType);
    }

    @Override
    public Endpoint getEndpoint(SmpName smpName, EndpointType endpointType) {
        final EndpointType eType = endpointType != null ? endpointType : defaultType;
        Smp smp = smpService.getSmp(smpName);
        return repository.findBySmp(smp).stream().filter(e -> eType.equals(e.getType())).findFirst().orElse(null);
    }

    @Override
    public void saveEndpoint(Endpoint endpoint) {
        repository.save(endpoint);
    }
}
