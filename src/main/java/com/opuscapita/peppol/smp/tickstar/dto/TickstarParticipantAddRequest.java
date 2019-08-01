package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

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

    public static TickstarParticipantAddRequest of(Participant participant) {
        TickstarParticipantAddRequest addRequest = new TickstarParticipantAddRequest();
        addRequest.setMeta(TickstarParticipantListMetadata.of(participant));
        addRequest.setBusinessCard(TickstarParticipantAddBusinessCard.of(participant));
        addRequest.setAccessPointConfigurations(TickstarParticipantListAccessPointConfigurations.of(participant));
        return addRequest;
    }
}
