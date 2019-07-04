package com.opuscapita.peppol.smp.tickstar.dto;

import java.util.List;

public class TickstarLookupResponse {

    private List<TickstarLookupParticipant> participants;

    public List<TickstarLookupParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<TickstarLookupParticipant> participants) {
        this.participants = participants;
    }
}
