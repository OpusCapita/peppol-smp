package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.List;

public class TickstarParticipantAddBusinessCard {

    private List<TickstarParticipantAddBusinessEntity> businessEntity;

    public List<TickstarParticipantAddBusinessEntity> getBusinessEntity() {
        return businessEntity;
    }

    public void setBusinessEntity(List<TickstarParticipantAddBusinessEntity> businessEntity) {
        this.businessEntity = businessEntity;
    }
}
