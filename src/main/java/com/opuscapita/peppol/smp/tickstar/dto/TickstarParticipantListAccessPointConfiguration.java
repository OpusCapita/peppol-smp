package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

public class TickstarParticipantListAccessPointConfiguration {

    private Integer endpointId;
    private TickstarParticipantListAccessPointConfigurationMetadata metadataProfileIds;

    public Integer getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(Integer endpointId) {
        this.endpointId = endpointId;
    }

    public TickstarParticipantListAccessPointConfigurationMetadata getMetadataProfileIds() {
        return metadataProfileIds;
    }

    public void setMetadataProfileIds(TickstarParticipantListAccessPointConfigurationMetadata metadataProfileIds) {
        this.metadataProfileIds = metadataProfileIds;
    }

    public static TickstarParticipantListAccessPointConfiguration of(Participant participant) {
        TickstarParticipantListAccessPointConfiguration apConfiguration = new TickstarParticipantListAccessPointConfiguration();
        apConfiguration.setEndpointId(participant.getEndpoint().getId().intValue());
        apConfiguration.setMetadataProfileIds(TickstarParticipantListAccessPointConfigurationMetadata.of(participant));
        return apConfiguration;
    }
}
