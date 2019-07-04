package com.opuscapita.peppol.smp.tickstar.dto;

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
}
