package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.List;

public class TickstarParticipantListResponse {

    private List<TickstarParticipantListParticipant> participant;

    public List<TickstarParticipantListParticipant> getParticipant() {
        return participant;
    }

    public void setParticipant(List<TickstarParticipantListParticipant> participant) {
        this.participant = participant;
    }
}
