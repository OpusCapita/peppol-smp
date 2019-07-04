package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.List;

public class TickstarParticipantListBusinessCard {

    private List<TickstarParticipantListBusinessEntity> businessEntity;

    public List<TickstarParticipantListBusinessEntity> getBusinessEntity() {
        return businessEntity;
    }

    public void setBusinessEntity(List<TickstarParticipantListBusinessEntity> businessEntity) {
        this.businessEntity = businessEntity;
    }
}
