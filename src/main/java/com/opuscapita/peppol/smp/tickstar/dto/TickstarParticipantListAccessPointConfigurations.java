package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

import java.util.Collections;
import java.util.List;

public class TickstarParticipantListAccessPointConfigurations {

    private List<TickstarParticipantListAccessPointConfiguration> accessPointConfiguration;

    public List<TickstarParticipantListAccessPointConfiguration> getAccessPointConfiguration() {
        return accessPointConfiguration;
    }

    public void setAccessPointConfiguration(List<TickstarParticipantListAccessPointConfiguration> accessPointConfiguration) {
        this.accessPointConfiguration = accessPointConfiguration;
    }

    public static TickstarParticipantListAccessPointConfigurations of(Participant participant) {
        TickstarParticipantListAccessPointConfigurations apConfigurations = new TickstarParticipantListAccessPointConfigurations();
        apConfigurations.setAccessPointConfiguration(Collections.singletonList(TickstarParticipantListAccessPointConfiguration.of(participant)));
        return apConfigurations;
    }
}
