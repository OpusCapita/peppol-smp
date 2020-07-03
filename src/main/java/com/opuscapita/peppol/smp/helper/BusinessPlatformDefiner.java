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
        return define(participant.getIcdIdentifier());
    }

    public BusinessPlatform define(String icd, String identifier) {
        return define(icd + ":" + identifier);
    }

    public BusinessPlatform define(String icdIdentifier) {
        for (Route route : routingConfiguration.getRoutes()) {
            if (icdIdentifier.matches(route.getMask())) {
                logger.debug("BusinessPlatform assigned to " + icdIdentifier + " as: " + route.getPlatform());
                return route.getPlatform();
            }
        }
        return BusinessPlatform.A2A;
    }
}
