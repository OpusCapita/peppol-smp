package com.opuscapita.peppol.smp.tickstar.dto;

import com.opuscapita.peppol.smp.entity.Participant;

import java.util.Collections;
import java.util.List;

public class TickstarParticipantBusinessCard {

    private List<TickstarParticipantBusinessEntity> businessEntity;

    public List<TickstarParticipantBusinessEntity> getBusinessEntity() {
        return businessEntity;
    }

    public void setBusinessEntity(List<TickstarParticipantBusinessEntity> businessEntity) {
        this.businessEntity = businessEntity;
    }

    public static TickstarParticipantBusinessCard of(Participant participant) {
        TickstarParticipantBusinessCard businessCard = new TickstarParticipantBusinessCard();
        businessCard.setBusinessEntity(Collections.singletonList(TickstarParticipantBusinessEntity.of(participant)));
        return businessCard;
    }
}
