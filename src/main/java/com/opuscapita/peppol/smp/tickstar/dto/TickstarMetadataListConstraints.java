package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.List;

public class TickstarMetadataListConstraints {

    private List<String> participantIdentifierConstraint;

    public List<String> getParticipantIdentifierConstraint() {
        return participantIdentifierConstraint;
    }

    public void setParticipantIdentifierConstraint(List<String> participantIdentifierConstraint) {
        this.participantIdentifierConstraint = participantIdentifierConstraint;
    }
}
