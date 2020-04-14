package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

public class TickstarParticipantAccessPointConfiguration {

    private Integer endpointId;
    private TickstarParticipantAccessPointConfigurationMetadata metadataProfileIds;

    public TickstarParticipantAccessPointConfiguration() {
        this.metadataProfileIds = new TickstarParticipantAccessPointConfigurationMetadata();
    }

    public Integer getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(Integer endpointId) {
        this.endpointId = endpointId;
    }

    public TickstarParticipantAccessPointConfigurationMetadata getMetadataProfileIds() {
        return metadataProfileIds;
    }

    public void setMetadataProfileIds(TickstarParticipantAccessPointConfigurationMetadata metadataProfileIds) {
        this.metadataProfileIds = metadataProfileIds;
    }

    public static TickstarParticipantAccessPointConfiguration of(Participant participant) {
        TickstarParticipantAccessPointConfiguration apConfiguration = new TickstarParticipantAccessPointConfiguration();
        apConfiguration.setEndpointId(participant.getEndpoint().getId().intValue());
        apConfiguration.setMetadataProfileIds(TickstarParticipantAccessPointConfigurationMetadata.of(participant));
        return apConfiguration;
    }
}
