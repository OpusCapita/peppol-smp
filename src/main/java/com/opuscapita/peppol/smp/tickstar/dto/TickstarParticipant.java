package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

public class TickstarParticipant {

    private TickstarParticipantMetadata meta;
    private TickstarParticipantBusinessCard businessCard;
    private TickstarParticipantAccessPointConfigurations accessPointConfigurations;

    public TickstarParticipantMetadata getMeta() {
        return meta;
    }

    public void setMeta(TickstarParticipantMetadata meta) {
        this.meta = meta;
    }

    public TickstarParticipantBusinessCard getBusinessCard() {
        return businessCard;
    }

    public void setBusinessCard(TickstarParticipantBusinessCard businessCard) {
        this.businessCard = businessCard;
    }

    public TickstarParticipantAccessPointConfigurations getAccessPointConfigurations() {
        return accessPointConfigurations;
    }

    public void setAccessPointConfigurations(TickstarParticipantAccessPointConfigurations accessPointConfigurations) {
        this.accessPointConfigurations = accessPointConfigurations;
    }

    public static TickstarParticipant of(Participant participant) {
        TickstarParticipant addRequest = new TickstarParticipant();
        addRequest.setMeta(TickstarParticipantMetadata.of(participant));
        addRequest.setBusinessCard(TickstarParticipantBusinessCard.of(participant));
        addRequest.setAccessPointConfigurations(TickstarParticipantAccessPointConfigurations.of(participant));
        return addRequest;
    }
}
