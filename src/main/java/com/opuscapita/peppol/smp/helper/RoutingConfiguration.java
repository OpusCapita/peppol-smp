package com.opuscapita.peppol.smp.helper;

import com.google.gson.annotations.Since;
import com.opuscapita.peppol.smp.entity.BusinessPlatform;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "peppol.routing-config")
public class RoutingConfiguration {

    private List<Route> routes = new ArrayList<>();

    public List<Route> getRoutes() {
        return routes;
    }

    public static class Route implements Serializable {

        @Since(1.0) private String mask;
        @Since(1.0) private BusinessPlatform platform;

        public String getMask() {
            return mask;
        }

        public void setMask(String mask) {
            this.mask = mask;
        }

        public BusinessPlatform getPlatform() {
            return platform;
        }

        public void setPlatform(BusinessPlatform platform) {
            this.platform = platform;
        }
    }
}

