package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

import java.util.Collections;
import java.util.List;

public class TickstarParticipantAccessPointConfigurations {

    private List<TickstarParticipantAccessPointConfiguration> accessPointConfiguration;

    public List<TickstarParticipantAccessPointConfiguration> getAccessPointConfiguration() {
        return accessPointConfiguration;
    }

    public void setAccessPointConfiguration(List<TickstarParticipantAccessPointConfiguration> accessPointConfiguration) {
        this.accessPointConfiguration = accessPointConfiguration;
    }

    public static TickstarParticipantAccessPointConfigurations of(Participant participant) {
        TickstarParticipantAccessPointConfigurations apConfigurations = new TickstarParticipantAccessPointConfigurations();
        apConfigurations.setAccessPointConfiguration(Collections.singletonList(TickstarParticipantAccessPointConfiguration.of(participant)));
        return apConfigurations;
    }
}
