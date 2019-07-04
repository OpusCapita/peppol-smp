package com.opuscapita.peppol.smp.tickstar.dto;

public class TickstarParticipantListParticipant {

    private TickstarParticipantListMetadata meta;
    private TickstarParticipantListBusinessCard businessCard;
    private TickstarParticipantListAccessPointConfigurations accessPointConfigurations;

    public TickstarParticipantListMetadata getMeta() {
        return meta;
    }

    public void setMeta(TickstarParticipantListMetadata meta) {
        this.meta = meta;
    }

    public TickstarParticipantListBusinessCard getBusinessCard() {
        return businessCard;
    }

    public void setBusinessCard(TickstarParticipantListBusinessCard businessCard) {
        this.businessCard = businessCard;
    }

    public TickstarParticipantListAccessPointConfigurations getAccessPointConfigurations() {
        return accessPointConfigurations;
    }

    public void setAccessPointConfigurations(TickstarParticipantListAccessPointConfigurations accessPointConfigurations) {
        this.accessPointConfigurations = accessPointConfigurations;
    }
}
