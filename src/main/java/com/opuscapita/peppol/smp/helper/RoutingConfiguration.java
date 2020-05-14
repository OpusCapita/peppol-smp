package com.opuscapita.peppol.smp.helper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "peppol.routing-config")
public class RoutingConfiguration {

    private List<Route> routes = new ArrayList<>();

    public List<Route> getRoutes() {
        return routes;
    }

}

