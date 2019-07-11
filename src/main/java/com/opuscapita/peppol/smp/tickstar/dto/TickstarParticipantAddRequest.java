package com.opuscapita.peppol.smp.tickstar.dto;

public class TickstarParticipantAddRequest {

    private TickstarParticipantListMetadata meta;
    private TickstarParticipantAddBusinessCard businessCard;
    private TickstarParticipantListAccessPointConfigurations accessPointConfigurations;

    public TickstarParticipantListMetadata getMeta() {
        return meta;
    }

    public void setMeta(TickstarParticipantListMetadata meta) {
        this.meta = meta;
    }

    public TickstarParticipantAddBusinessCard getBusinessCard() {
        return businessCard;
    }

    public void setBusinessCard(TickstarParticipantAddBusinessCard businessCard) {
        this.businessCard = businessCard;
    }

    public TickstarParticipantListAccessPointConfigurations getAccessPointConfigurations() {
        return accessPointConfigurations;
    }

    public void setAccessPointConfigurations(TickstarParticipantListAccessPointConfigurations accessPointConfigurations) {
        this.accessPointConfigurations = accessPointConfigurations;
    }
}
