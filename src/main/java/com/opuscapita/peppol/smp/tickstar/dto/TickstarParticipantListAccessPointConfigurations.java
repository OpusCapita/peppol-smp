package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.List;

public class TickstarParticipantListAccessPointConfigurations {

    private List<TickstarParticipantListAccessPointConfiguration> accessPointConfiguration;

    public List<TickstarParticipantListAccessPointConfiguration> getAccessPointConfiguration() {
        return accessPointConfiguration;
    }

    public void setAccessPointConfiguration(List<TickstarParticipantListAccessPointConfiguration> accessPointConfiguration) {
        this.accessPointConfiguration = accessPointConfiguration;
    }
}
