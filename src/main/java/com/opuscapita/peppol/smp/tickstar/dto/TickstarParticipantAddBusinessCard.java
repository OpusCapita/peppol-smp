package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

import java.util.Collections;
import java.util.List;

public class TickstarParticipantAddBusinessCard {

    private List<TickstarParticipantAddBusinessEntity> businessEntity;

    public List<TickstarParticipantAddBusinessEntity> getBusinessEntity() {
        return businessEntity;
    }

    public void setBusinessEntity(List<TickstarParticipantAddBusinessEntity> businessEntity) {
        this.businessEntity = businessEntity;
    }

    public static TickstarParticipantAddBusinessCard of(Participant participant) {
        TickstarParticipantAddBusinessCard businessCard = new TickstarParticipantAddBusinessCard();
        businessCard.setBusinessEntity(Collections.singletonList(TickstarParticipantAddBusinessEntity.of(participant)));
        return businessCard;
    }
}
