package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.List;

public class TickstarParticipantAddAdditionalIdentifiers {

    private List<TickstarParticipantListParticipantIdentifier> additionalIdentifier;

    public List<TickstarParticipantListParticipantIdentifier> getAdditionalIdentifier() {
        return additionalIdentifier;
    }

    public void setAdditionalIdentifier(List<TickstarParticipantListParticipantIdentifier> additionalIdentifier) {
        this.additionalIdentifier = additionalIdentifier;
    }
}
