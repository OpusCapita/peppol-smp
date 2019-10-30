package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.List;

public class TickstarParticipantAddAdditionalIdentifiers {

    private List<TickstarParticipantIdentifier> additionalIdentifier;

    public List<TickstarParticipantIdentifier> getAdditionalIdentifier() {
        return additionalIdentifier;
    }

    public void setAdditionalIdentifier(List<TickstarParticipantIdentifier> additionalIdentifier) {
        this.additionalIdentifier = additionalIdentifier;
    }
}
