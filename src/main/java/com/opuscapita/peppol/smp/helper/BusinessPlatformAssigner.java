package com.opuscapita.peppol.smp.helper;

import com.opuscapita.peppol.smp.entity.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessPlatformAssigner {

    private final static Logger logger = LoggerFactory.getLogger(BusinessPlatformAssigner.class);

    private final RoutingConfiguration routingConfiguration;

    @Autowired
    public BusinessPlatformAssigner(RoutingConfiguration routingConfiguration) {
        this.routingConfiguration = routingConfiguration;
    }

    public void assign(Participant participant) {
        for (RoutingConfiguration.Route route : routingConfiguration.getRoutes()) {
            if (participant.getIcdIdentifier().matches(route.getMask())) {
                participant.setBusinessPlatform(route.getPlatform());
                logger.info("BusinessPlatform assigned to " + participant.getIcdIdentifier() + " as: " + route.getPlatform());
            }
        }
    }
}
