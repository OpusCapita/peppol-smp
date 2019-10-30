package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.List;

public class TickstarParticipantListResponse {

    private List<TickstarParticipant> participant;

    public List<TickstarParticipant> getParticipant() {
        return participant;
    }

    public void setParticipant(List<TickstarParticipant> participant) {
        this.participant = participant;
    }
}
