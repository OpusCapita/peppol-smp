package com.opuscapita.peppol.smp.helper;

import com.opuscapita.peppol.smp.entity.BusinessPlatform;
import com.opuscapita.peppol.smp.entity.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessPlatformDefiner {

    private final static Logger logger = LoggerFactory.getLogger(BusinessPlatformDefiner.class);

    private final RoutingConfiguration routingConfiguration;

    @Autowired
    public BusinessPlatformDefiner(RoutingConfiguration routingConfiguration) {
        this.routingConfiguration = routingConfiguration;

        logger.info("Routing configuration loaded with " + routingConfiguration.getRoutes().size() + " routes.");
    }

    public BusinessPlatform define(Participant participant) {
        for (Route route : routingConfiguration.getRoutes()) {
            if (participant.getIcdIdentifier().matches(route.getMask())) {
                logger.debug("BusinessPlatform assigned to " + participant.getIcdIdentifier() + " as: " + route.getPlatform());
                return route.getPlatform();
            }
        }
        return BusinessPlatform.A2A;
    }
}
